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
    public String getConfigVersion() {
        return config.getString("version", "1.0");
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
}