package com.ddaodan.MineChatGPT;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class ConfigManager {
    private final Main plugin;
    private FileConfiguration config;
    private String currentModel;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        reloadConfig();
    }
    public boolean isDebugMode() {
        return config.getBoolean("debug", false);
    }
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        currentModel = config.getString("default_model");
    }

    private String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public String getCurrentModel() {
        return currentModel;
    }
    public void setCurrentModel(String model) {
        currentModel = model;
    }
    public String getApiKey() {
        return config.getString("api.key");
    }
    public String getBaseUrl() {
        return config.getString("api.base_url");
    }
    public String getDefaultModel() {
        return config.getString("default_model");
    }
    public String getReloadMessage() {
        return translateColorCodes(config.getString("messages.reload"));
    }
    public List<String> getModels() {
        return config.getStringList("models");
    }
    public String getCustomPrompt() {
        return config.getString("prompt", "You are a helpful assistant.");
    }
    public String getHelpMessage() {
        return translateColorCodes(config.getString("messages.help"));
    }
    public String getHelpAskMessage() {
        return translateColorCodes(config.getString("messages.help_ask"));
    }
    public String getHelpReloadMessage() {
        return translateColorCodes(config.getString("messages.help_reload"));
    }
    public String getHelpModelMessage() {
        return translateColorCodes(config.getString("messages.help_model"));
    }
    public String getHelpModelListMessage() {
        return translateColorCodes(config.getString("messages.help_modellist"));
    }
    public String getHelpContextMessage() {
        return translateColorCodes(config.getString("messages.help_context", "/chatgpt context - Toggle context mode."));
    }
    public String getHelpClearMessage() {
        return translateColorCodes(config.getString("messages.help_clear", "/chatgpt clear - Clear conversation history."));
    }
    public String getModelSwitchMessage() {
        return translateColorCodes(config.getString("messages.model_switch"));
    }
    public String getChatGPTErrorMessage() {
        return translateColorCodes(config.getString("messages.chatgpt_error"));
    }
    public String getChatGPTResponseMessage() {
        return translateColorCodes(config.getString("messages.chatgpt_response"));
    }
    public String getQuestionMessage() {
        return translateColorCodes(config.getString("messages.question"));
    }
    public String getInvalidModelMessage() {
        return translateColorCodes(config.getString("messages.invalid_model"));
    }
    public String getAvailableModelsMessage() {
        return translateColorCodes(config.getString("messages.available_models"));
    }
    public String getNoPermissionMessage() {
        return translateColorCodes(config.getString("messages.no_permission"));
    }
    public String getCurrentModelInfoMessage() {
        return translateColorCodes(config.getString("messages.current_model_info"));
    }
    public int getMaxHistorySize() {
        return config.getInt("conversation.max_history_size", 10);
    }
    public boolean isContextEnabled() {
        return config.getBoolean("conversation.context_enabled", false);
    }
    public String getContextToggleMessage() {
        return translateColorCodes(config.getString("messages.context_toggle", "Context is now %s."));
    }
    public String getContextToggleEnabledMessage() {
        return translateColorCodes(config.getString("messages.context_toggle_enabled", "enabled"));
    }
    public String getContextToggleDisabledMessage() {
        return translateColorCodes(config.getString("messages.context_toggle_disabled", "disabled"));
    }
    public String getClearMessage() {
        return translateColorCodes(config.getString("messages.clear", "Conversation history has been cleared."));
    }
}