package com.ddaodan.MineChatGPT;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.nio.charset.StandardCharsets;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
public class CommandHandler implements CommandExecutor {
    private final Main plugin;
    private final ConfigManager configManager;
    private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());
    private final Map<String, ConversationContext> userContexts;
    private final Map<String, Boolean> userContextEnabled;

    public CommandHandler(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.userContexts = new HashMap<>();
        this.userContextEnabled = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String userId = sender.getName();

        if (!userContexts.containsKey(userId)) {
            userContexts.put(userId, new ConversationContext(configManager.getMaxHistorySize()));
            userContextEnabled.put(userId, configManager.isContextEnabled());
        }

        ConversationContext conversationContext = userContexts.get(userId);
        boolean contextEnabled = userContextEnabled.get(userId);

        if (command.getName().equalsIgnoreCase("chatgpt")) {
            if (args.length == 0) {
                sendHelpMessage(sender);
                return true;
            }
            String subCommand = args[0];
            if (subCommand.equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("minechatgpt.reload")) {
                    sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.reload"));
                    return true;
                }
                configManager.reloadConfig();
                sender.sendMessage(configManager.getReloadMessage());
                return true;
            } else if (subCommand.equalsIgnoreCase("model")) {
                if (!sender.hasPermission("minechatgpt.model")) {
                    sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.model"));
                    return true;
                }
                if (args.length < 2) {
                    String currentModel = configManager.getCurrentModel();
                    sender.sendMessage(configManager.getCurrentModelInfoMessage().replace("%s", currentModel));
                    return true;
                }
                String model = args[1];
                List<String> models = configManager.getModels();
                if (models.contains(model)) {
                    configManager.setCurrentModel(model);
                    sender.sendMessage(configManager.getModelSwitchMessage().replace("%s", model));
                } else {
                    sender.sendMessage(configManager.getInvalidModelMessage());
                }
                return true;
            } else if (subCommand.equalsIgnoreCase("modellist")) {
                if (!sender.hasPermission("minechatgpt.modellist")) {
                    sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.modellist"));
                    return true;
                }
                List<String> models = configManager.getModels();
                sender.sendMessage(configManager.getAvailableModelsMessage());
                for (String model : models) {
                    sender.sendMessage("- " + model);
                }
                return true;
            } else if (args.length > 0 && args[0].equalsIgnoreCase("context")) {
                contextEnabled = !contextEnabled;
                userContextEnabled.put(userId, contextEnabled);
                String status = contextEnabled ? configManager.getContextToggleEnabledMessage() : configManager.getContextToggleDisabledMessage();
                sender.sendMessage(configManager.getContextToggleMessage().replace("%s", status));
                return true;
            } else if (subCommand.equalsIgnoreCase("clear")) {
                if (!sender.hasPermission("minechatgpt.clear")) {
                    sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.clear"));
                    return true;
                }
                conversationContext.clearHistory();
                sender.sendMessage(configManager.getClearMessage());
                return true;
            } else if (subCommand.equalsIgnoreCase("character")) {
                    if (!sender.hasPermission("minechatgpt.character")) {
                        sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.character"));
                        return true;
                    }
                    Map<String, String> characters = configManager.getCharacters();
                    if (args.length < 2) {
                        sender.sendMessage(configManager.getAvailableCharactersMessage());
                        for (String character : characters.keySet()) {
                            sender.sendMessage("- " + character);
                        }
                        return true;
                    }
                    String character = args[1];
                    if (characters.containsKey(character)) {
                        configManager.setCurrentCharacter(userId, character);
                        sender.sendMessage(configManager.getCharacterSwitchedMessage().replace("%s", character));
                    } else {
                        sender.sendMessage(configManager.getInvalidCharacterMessage());
                    }
                    return true;
            } else {
                if (!sender.hasPermission("minechatgpt.use")) {
                    sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.use"));
                    return true;
                }
                String question = String.join(" ", args);
                if (contextEnabled) {
                    conversationContext.addMessage(question);
                }
                sender.sendMessage(configManager.getQuestionMessage().replace("%s", question));
                askChatGPT(sender, question, conversationContext, contextEnabled, userId);
                return true;
            }
        }
        return false;
    }

    private void askChatGPT(CommandSender sender, String question, ConversationContext conversationContext, boolean contextEnabled, String userId) {
        String utf8Question = convertToUTF8(question);
        JSONObject json = new JSONObject();
        json.put("model", configManager.getDefaultModel());
        JSONArray messages = new JSONArray();
        // 添加自定义 prompt
        String currentCharacter = configManager.getCurrentCharacter(userId);
        String customPrompt = configManager.getCharacters().get(currentCharacter);
        if (customPrompt != null && !customPrompt.isEmpty()) {
            JSONObject promptMessage = new JSONObject();
            promptMessage.put("role", "system");
            promptMessage.put("content", customPrompt);
            messages.put(promptMessage);
        }
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", utf8Question);
        messages.put(message);
        if (contextEnabled) {
            String history = conversationContext.getConversationHistory();
            if (!history.isEmpty()) {
                JSONObject historyMessage = new JSONObject();
                historyMessage.put("role", "system");
                historyMessage.put("content", history);
                messages.put(historyMessage);
            }
        }
        json.put("messages", messages);
        json.put("model", configManager.getCurrentModel());
        if (configManager.isDebugMode()) {
            logger.info("Built request: " + json.toString());
        }

        HttpRequest request = HttpRequest.post(configManager.getBaseUrl() + "/chat/completions")
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + configManager.getApiKey())
                .bodyText(json.toString());

        if (configManager.isDebugMode()) {
            logger.info("Sending request to ChatGPT: " + request.toString());
        }

        //HttpResponse response = request.send();
        CompletableFuture.supplyAsync(() -> request.send())
                .thenAccept(response -> {
                    if (configManager.isDebugMode()) {
                        logger.info("Received response from ChatGPT: " + response.toString());
                    }
                    if (response.statusCode() == 200) {
                        String responseBody = response.bodyText();
                        String utf8ResponseBody = new String(responseBody.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        JSONObject jsonResponse = new JSONObject(utf8ResponseBody);
                        String answer = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                        sender.sendMessage(configManager.getChatGPTResponseMessage().replaceFirst("%s", currentCharacter).replaceFirst("%s", answer));
                        if (contextEnabled) {
                            conversationContext.addMessage(answer); // 仅在启用上下文时添加AI响应到历史记录
                        }
                    } else {
                        String errorBody = response.bodyText();
                        logger.log(Level.SEVERE, "Failed to get a response from ChatGPT: " + errorBody);
                        sender.sendMessage(configManager.getChatGPTErrorMessage());
                    }
                })
                .exceptionally(e -> {
                    logger.log(Level.SEVERE, "Exception occurred while processing request: " + e.getMessage(), e);
                    sender.sendMessage(configManager.getChatGPTErrorMessage());
                    return null;
                });
    }

    private String convertToUTF8(String input) {
        try {
            // 尝试将输入字符串转换为 UTF-8 编码
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.severe("Failed to convert input to UTF-8: " + e.getMessage());
            return input; // 如果转换失败，返回原始输入
        }
    }
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(configManager.getHelpMessage());
        sender.sendMessage(configManager.getHelpAskMessage());
        sender.sendMessage(configManager.getHelpReloadMessage());
        sender.sendMessage(configManager.getHelpModelMessage());
        sender.sendMessage(configManager.getHelpModelListMessage());
        sender.sendMessage(configManager.getHelpContextMessage());
        sender.sendMessage(configManager.getHelpClearMessage());
        sender.sendMessage(configManager.getHelpCharacterMessage());
    }
}