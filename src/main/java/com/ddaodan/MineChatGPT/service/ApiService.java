package com.ddaodan.MineChatGPT.service;

import com.ddaodan.MineChatGPT.ConfigManager;
import com.ddaodan.MineChatGPT.ConversationContext;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * API服务类，负责处理与OpenAI API的通信
 */
public class ApiService {
    private final ConfigManager configManager;
    private static final Logger logger = Logger.getLogger(ApiService.class.getName());

    public ApiService(ConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * 向ChatGPT发送请求
     *
     * @param sender 命令发送者
     * @param question 问题内容
     * @param conversationContext 对话上下文
     * @param contextEnabled 是否启用上下文
     * @param userId 用户ID
     */
    public void askChatGPT(CommandSender sender, String question, ConversationContext conversationContext, boolean contextEnabled, String userId) {
        String utf8Question = convertToUTF8(question);
        JSONObject json = new JSONObject();
        json.put("model", configManager.getCurrentModel());
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

        CompletableFuture.supplyAsync(() -> request.send())
                .thenAccept(response -> {
                    if (configManager.isDebugMode()) {
                        logger.info("Received response from ChatGPT: " + response.toString());
                    }
                    if (response.statusCode() == 200) {
                        processSuccessResponse(response, sender, conversationContext, contextEnabled, currentCharacter);
                    } else {
                        processErrorResponse(response, sender);
                    }
                })
                .exceptionally(e -> {
                    logger.log(Level.SEVERE, "Exception occurred while processing request: " + e.getMessage(), e);
                    sender.sendMessage(configManager.getChatGPTErrorMessage());
                    return null;
                });
    }

    /**
     * 处理成功的API响应
     *
     * @param response HTTP响应
     * @param sender 命令发送者
     * @param conversationContext 对话上下文
     * @param contextEnabled 是否启用上下文
     * @param currentCharacter 当前角色
     */
    private void processSuccessResponse(HttpResponse response, CommandSender sender, 
                                       ConversationContext conversationContext, 
                                       boolean contextEnabled, String currentCharacter) {
        String responseBody = response.bodyText();
        String utf8ResponseBody = new String(responseBody.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        try {
            JSONObject jsonResponse = new JSONObject(utf8ResponseBody);
            String answer = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            sender.sendMessage(configManager.getChatGPTResponseMessage().replaceFirst("%s", currentCharacter).replaceFirst("%s", answer));
            if (contextEnabled) {
                conversationContext.addMessage(answer); // 仅在启用上下文时添加AI响应到历史记录
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to parse ChatGPT response: " + e.getMessage(), e);
            sender.sendMessage(configManager.getChatGPTErrorMessage());
        }
    }

    /**
     * 处理错误的API响应
     *
     * @param response HTTP响应
     * @param sender 命令发送者
     */
    private void processErrorResponse(HttpResponse response, CommandSender sender) {
        String errorBody = response.bodyText();
        logger.log(Level.SEVERE, "Failed to get a response from ChatGPT: " + errorBody);
        sender.sendMessage(configManager.getChatGPTErrorMessage());
    }

    /**
     * 将字符串转换为UTF-8编码
     *
     * @param input 输入字符串
     * @return UTF-8编码的字符串
     */
    private String convertToUTF8(String input) {
        try {
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.severe("Failed to convert input to UTF-8: " + e.getMessage());
            return input; // 如果转换失败，返回原始输入
        }
    }
}