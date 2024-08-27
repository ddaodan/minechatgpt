package com.ddaodan.MineChatGPT;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return translateColorCodes(config.getString("messages.help_context", "&e/chatgpt context - Toggle context mode."));
    }
    public String getHelpClearMessage() {
        return translateColorCodes(config.getString("messages.help_clear", "&e/chatgpt clear - Clear conversation history."));
    }
    public String getHelpCharacterMessage() {
        return translateColorCodes(config.getString("messages.help_character", "&e/chatgpt character [character_name] - List or switch to a character."));
    }
    public String getModelSwitchMessage() {
        return translateColorCodes(config.getString("messages.model_switch"));
    }
    public String getChatGPTErrorMessage() {
        return translateColorCodes(config.getString("messages.chatgpt_error"));
    }
    public String getChatGPTResponseMessage() {
        return translateColorCodes(config.getString("messages.chatgpt_response", "&b%s: %s"));
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
        return translateColorCodes(config.getString("messages.context_toggle", "&eContext is now %s."));
    }
    public String getContextToggleEnabledMessage() {
        return translateColorCodes(config.getString("messages.context_toggle_enabled", "&aenabled"));
    }
    public String getContextToggleDisabledMessage() {
        return translateColorCodes(config.getString("messages.context_toggle_disabled", "&edisabled"));
    }
    public String getClearMessage() {
        return translateColorCodes(config.getString("messages.clear", "&aConversation history has been cleared."));
    }
    public String getCharacterSwitchedMessage() {
        return translateColorCodes(config.getString("messages.character_switched", "&aSwitched to character: %s"));
    }
    public String getAvailableCharactersMessage() {
        return translateColorCodes(config.getString("messages.available_characters", "&eAvailable characters:"));
    }
    public String getInvalidCharacterMessage() {
        return translateColorCodes(config.getString("messages.invalid_character", "&cInvalid character. Use /chatgpt character to list available characters."));
    }
    public Map<String, String> getCharacters() {
        Map<String, String> characters = new HashMap<>();
        config.getConfigurationSection("characters").getKeys(false).forEach(key -> {
            characters.put(key, config.getString("characters." + key));
        });
        return characters;
    }
    public String getCurrentCharacter(String userId) {
        return config.getString("users." + userId + ".character", "ChatGPT");
    }
    public void setCurrentCharacter(String userId, String character) {
        config.set("users." + userId + ".character", character);
        plugin.saveConfig();
    }
}