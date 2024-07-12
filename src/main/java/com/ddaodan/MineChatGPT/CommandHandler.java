package com.ddaodan.MineChatGPT;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
                    sender.sendMessage(ChatColor.RED + String.format(configManager.getNoPermissionMessage(), "minechatgpt.reload"));
                    return true;
                }
                configManager.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + configManager.getReloadMessage());
                return true;
            } else if (subCommand.equalsIgnoreCase("model")) {
                if (!sender.hasPermission("minechatgpt.model")) {
                    sender.sendMessage(ChatColor.RED + String.format(configManager.getNoPermissionMessage(), "minechatgpt.model"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + configManager.getUsageMessage());
                    return true;
                }
                String model = args[1];
                List<String> models = configManager.getModels();
                if (models.contains(model)) {
                    // Logic to switch model
                    sender.sendMessage(ChatColor.GREEN + String.format(configManager.getModelSwitchMessage(), model));
                } else {
                    sender.sendMessage(ChatColor.RED + configManager.getInvalidModelMessage());
                }
                return true;
            } else if (subCommand.equalsIgnoreCase("modellist")) {
                if (!sender.hasPermission("minechatgpt.modellist")) {
                    sender.sendMessage(ChatColor.RED + String.format(configManager.getNoPermissionMessage(), "minechatgpt.modellist"));
                    return true;
                }
                List<String> models = configManager.getModels();
                sender.sendMessage(ChatColor.YELLOW + configManager.getAvailableModelsMessage());
                for (String model : models) {
                    sender.sendMessage(ChatColor.YELLOW + "- " + model);
                }
                return true;
            } else {
                if (!sender.hasPermission("minechatgpt.use")) {
                    sender.sendMessage(ChatColor.RED + String.format(configManager.getNoPermissionMessage(), "minechatgpt.use"));
                    return true;
                }
                String question = String.join(" ", args);
                // Logic to send question to ChatGPT
                sender.sendMessage(ChatColor.AQUA + String.format(configManager.getQuestionMessage(), question));
                askChatGPT(sender, question);
                return true;
            }
        }
        return false;
    }

    private void askChatGPT(CommandSender sender, String question) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        json.put("model", configManager.getDefaultModel());
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", question);
        messages.put(message);
        json.put("messages", messages);

        RequestBody body = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .url(configManager.getBaseUrl() + "/chat/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + configManager.getApiKey())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "Failed to contact ChatGPT: " + e.getMessage(), e);
                sender.sendMessage(ChatColor.RED + configManager.getChatGPTErrorMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String answer = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    sender.sendMessage(ChatColor.AQUA + String.format(configManager.getChatGPTResponseMessage(), answer));
                } else {
                    String errorBody = response.body().string();
                    logger.log(Level.SEVERE, "Failed to get a response from ChatGPT: " + errorBody);
                    sender.sendMessage(ChatColor.RED + configManager.getChatGPTErrorMessage());
                }
                response.close();
            }
        });
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + configManager.getHelpMessage());
        sender.sendMessage(ChatColor.YELLOW + configManager.getHelpAskMessage());
        sender.sendMessage(ChatColor.YELLOW + configManager.getHelpReloadMessage());
        sender.sendMessage(ChatColor.YELLOW + configManager.getHelpModelMessage());
        sender.sendMessage(ChatColor.YELLOW + configManager.getHelpModelListMessage());
    }
}