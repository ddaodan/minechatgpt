package com.ddaodan.MineChatGPT.service;

import com.ddaodan.MineChatGPT.ConfigManager;
import com.ddaodan.MineChatGPT.ConversationContext;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

/**
 * 命令处理服务类，负责处理各种命令的业务逻辑
 */
public class CommandService {
    private final ConfigManager configManager;
    private final ApiService apiService;
    private final UserSessionManager sessionManager;

    public CommandService(ConfigManager configManager, ApiService apiService, UserSessionManager sessionManager) {
        this.configManager = configManager;
        this.apiService = apiService;
        this.sessionManager = sessionManager;
    }

    /**
     * 处理重载配置命令
     *
     * @param sender 命令发送者
     * @return 是否成功处理
     */
    public boolean handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("minechatgpt.reload")) {
            sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.reload"));
            return true;
        }
        configManager.reloadConfig();
        sender.sendMessage(configManager.getReloadMessage());
        return true;
    }

    /**
     * 处理模型切换命令
     *
     * @param sender 命令发送者
     * @param args 命令参数
     * @return 是否成功处理
     */
    public boolean handleModelCommand(CommandSender sender, String[] args) {
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
    }

    /**
     * 处理模型列表命令
     *
     * @param sender 命令发送者
     * @return 是否成功处理
     */
    public boolean handleModelListCommand(CommandSender sender) {
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
    }

    /**
     * 处理上下文切换命令
     *
     * @param sender 命令发送者
     * @param userId 用户ID
     * @return 是否成功处理
     */
    public boolean handleContextCommand(CommandSender sender, String userId) {
        boolean contextEnabled = !sessionManager.isContextEnabled(userId);
        sessionManager.setContextEnabled(userId, contextEnabled);
        String status = contextEnabled ? configManager.getContextToggleEnabledMessage() : configManager.getContextToggleDisabledMessage();
        sender.sendMessage(configManager.getContextToggleMessage().replace("%s", status));
        return true;
    }

    /**
     * 处理清除历史记录命令
     *
     * @param sender 命令发送者
     * @param userId 用户ID
     * @return 是否成功处理
     */
    public boolean handleClearCommand(CommandSender sender, String userId) {
        if (!sender.hasPermission("minechatgpt.clear")) {
            sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.clear"));
            return true;
        }
        sessionManager.clearConversationHistory(userId);
        sender.sendMessage(configManager.getClearMessage());
        return true;
    }

    /**
     * 处理角色切换命令
     *
     * @param sender 命令发送者
     * @param args 命令参数
     * @param userId 用户ID
     * @return 是否成功处理
     */
    public boolean handleCharacterCommand(CommandSender sender, String[] args, String userId) {
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
            sessionManager.setCurrentCharacter(userId, character);
            sender.sendMessage(configManager.getCharacterSwitchedMessage().replace("%s", character));
        } else {
            sender.sendMessage(configManager.getInvalidCharacterMessage());
        }
        return true;
    }

    /**
     * 处理向ChatGPT提问的命令
     *
     * @param sender 命令发送者
     * @param args 命令参数
     * @param userId 用户ID
     * @return 是否成功处理
     */
    public boolean handleAskCommand(CommandSender sender, String[] args, String userId) {
        if (!sender.hasPermission("minechatgpt.use")) {
            sender.sendMessage(configManager.getNoPermissionMessage().replace("%s", "minechatgpt.use"));
            return true;
        }
        String question = String.join(" ", args);
        ConversationContext conversationContext = sessionManager.getConversationContext(userId);
        boolean contextEnabled = sessionManager.isContextEnabled(userId);
        
        if (contextEnabled) {
            conversationContext.addMessage(question);
        }
        
        sender.sendMessage(configManager.getQuestionMessage().replace("%s", question));
        apiService.askChatGPT(sender, question, conversationContext, contextEnabled, userId);
        return true;
    }

    /**
     * 发送帮助信息
     *
     * @param sender 命令发送者
     */
    public void sendHelpMessage(CommandSender sender) {
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