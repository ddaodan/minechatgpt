package com.ddaodan.MineChatGPT.service;

import com.ddaodan.MineChatGPT.ConfigManager;
import com.ddaodan.MineChatGPT.ConversationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户会话管理类，负责管理用户的会话状态和上下文
 */
public class UserSessionManager {
    private final ConfigManager configManager;
    private final Map<String, ConversationContext> userContexts;
    private final Map<String, Boolean> userContextEnabled;
    private final Map<String, String> userCurrentCharacter;

    public UserSessionManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.userContexts = new HashMap<>();
        this.userContextEnabled = new HashMap<>();
        this.userCurrentCharacter = new HashMap<>();
    }

    /**
     * 获取用户的对话上下文
     *
     * @param userId 用户ID
     * @return 对话上下文
     */
    public ConversationContext getConversationContext(String userId) {
        if (!userContexts.containsKey(userId)) {
            userContexts.put(userId, new ConversationContext(configManager.getMaxHistorySize()));
            userContextEnabled.put(userId, configManager.isContextEnabled());
        }
        return userContexts.get(userId);
    }

    /**
     * 判断用户是否启用了上下文
     *
     * @param userId 用户ID
     * @return 是否启用上下文
     */
    public boolean isContextEnabled(String userId) {
        if (!userContextEnabled.containsKey(userId)) {
            userContextEnabled.put(userId, configManager.isContextEnabled());
        }
        return userContextEnabled.get(userId);
    }

    /**
     * 设置用户的上下文启用状态
     *
     * @param userId 用户ID
     * @param enabled 是否启用
     */
    public void setContextEnabled(String userId, boolean enabled) {
        userContextEnabled.put(userId, enabled);
    }

    /**
     * 清除用户的对话历史
     *
     * @param userId 用户ID
     */
    public void clearConversationHistory(String userId) {
        ConversationContext context = getConversationContext(userId);
        context.clearHistory();
    }

    /**
     * 获取用户当前使用的角色
     *
     * @param userId 用户ID
     * @return 角色名称
     */
    public String getCurrentCharacter(String userId) {
        if (!userCurrentCharacter.containsKey(userId)) {
            userCurrentCharacter.put(userId, configManager.getCurrentCharacter(userId));
        }
        return userCurrentCharacter.get(userId);
    }

    /**
     * 设置用户当前使用的角色
     *
     * @param userId 用户ID
     * @param character 角色名称
     */
    public void setCurrentCharacter(String userId, String character) {
        userCurrentCharacter.put(userId, character);
        configManager.setCurrentCharacter(userId, character);
    }
}