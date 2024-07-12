package com.ddaodan.MineChatGPT;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

public class CommandHandler implements CommandExecutor {
    private final Main plugin;
    private final ConfigManager configManager;
    private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());

    public CommandHandler(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
                    sender.sendMessage(configManager.getUsageMessage());
                    return true;
                }
                String model = args[1];
                List<String> models = configManager.getModels();
                if (models.contains(model)) {
                    // Logic to switch model
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
            } else {
                if (!sender.hasPermission("minechatgpt.use")) {
                    sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.use"));
                    return true;
                }
                String question = String.join(" ", args);
                // Logic to send question to ChatGPT
                sender.sendMessage(configManager.getQuestionMessage().replace("%s", question));
                askChatGPT(sender, question);
                return true;
            }
        }
        return false;
    }

    private void askChatGPT(CommandSender sender, String question) {
        JSONObject json = new JSONObject();
        json.put("model", configManager.getDefaultModel());
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", question);
        messages.put(message);
        json.put("messages", messages);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(configManager.getBaseUrl() + "/chat/completions");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Bearer " + configManager.getApiKey());
            request.setEntity(new StringEntity(json.toString(), "UTF-8"));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String answer = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    sender.sendMessage(configManager.getChatGPTResponseMessage().replace("%s", answer));
                } else {
                    String errorBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    logger.log(Level.SEVERE, "Failed to get a response from ChatGPT: " + errorBody);
                    sender.sendMessage(configManager.getChatGPTErrorMessage());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to contact ChatGPT: " + e.getMessage(), e);
            sender.sendMessage(configManager.getChatGPTErrorMessage());
        }
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(configManager.getHelpMessage());
        sender.sendMessage(configManager.getHelpAskMessage());
        sender.sendMessage(configManager.getHelpReloadMessage());
        sender.sendMessage(configManager.getHelpModelMessage());
        sender.sendMessage(configManager.getHelpModelListMessage());
    }
}