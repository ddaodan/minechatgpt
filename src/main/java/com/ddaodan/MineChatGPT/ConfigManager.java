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
    private LanguageManager languageManager;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        reloadConfig();
        // 获取语言设置
        String language = config.getString("language", "en");
        this.languageManager = new LanguageManager(plugin, language);
    }
    public boolean isDebugMode() {
        return config.getBoolean("debug", false);
    }
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        currentModel = config.getString("default_model");
        
        // 重新加载语言文件
        if (languageManager != null) {
            String language = config.getString("language", "en");
            languageManager.setLanguage(language);
        }
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
    private int currentKeyIndex = 0;
    
    public String getApiKey() {
        List<String> keys = config.getStringList("api.keys");
        if (keys.isEmpty()) {
            // 向后兼容：如果没有找到keys列表，尝试使用旧的单一key配置
            String legacyKey = config.getString("api.key");
            if (legacyKey != null && !legacyKey.isEmpty()) {
                return legacyKey;
            }
            return "";
        }
        
        String selectionMethod = config.getString("api.selection_method", "round_robin");
        
        if ("random".equalsIgnoreCase(selectionMethod)) {
            // 随机选择一个key
            int randomIndex = (int) (Math.random() * keys.size());
            return keys.get(randomIndex);
        } else {
            // 默认使用轮询方式
            String key = keys.get(currentKeyIndex);
            currentKeyIndex = (currentKeyIndex + 1) % keys.size();
            return key;
        }
    }
    public String getBaseUrl() {
        return config.getString("api.base_url");
    }
    public String getDefaultModel() {
        return config.getString("default_model");
    }
    public String getReloadMessage() {
        return languageManager.getMessage("reload");
    }
    public List<String> getModels() {
        return config.getStringList("models");
    }
    public String getHelpMessage() {
        return languageManager.getMessage("help");
    }
    public String getHelpAskMessage() {
        return languageManager.getMessage("help_ask");
    }
    public String getHelpReloadMessage() {
        return languageManager.getMessage("help_reload");
    }
    public String getHelpModelMessage() {
        return languageManager.getMessage("help_model");
    }
    public String getHelpModelListMessage() {
        return languageManager.getMessage("help_modellist");
    }
    public String getHelpContextMessage() {
        return languageManager.getMessage("help_context");
    }
    public String getHelpClearMessage() {
        return languageManager.getMessage("help_clear");
    }
    public String getHelpCharacterMessage() {
        return languageManager.getMessage("help_character");
    }
    public String getModelSwitchMessage() {
        return languageManager.getMessage("model_switch");
    }
    public String getChatGPTErrorMessage() {
        return languageManager.getMessage("chatgpt_error");
    }
    public String getChatGPTResponseMessage() {
        return languageManager.getMessage("chatgpt_response", "&b%s: %s");
    }
    public String getQuestionMessage() {
        return languageManager.getMessage("question");
    }
    public String getInvalidModelMessage() {
        return languageManager.getMessage("invalid_model");
    }
    public String getAvailableModelsMessage() {
        return languageManager.getMessage("available_models");
    }
    public String getNoPermissionMessage() {
        return languageManager.getMessage("no_permission");
    }
    public String getCurrentModelInfoMessage() {
        return languageManager.getMessage("current_model_info");
    }
    public int getMaxHistorySize() {
        return config.getInt("conversation.max_history_size", 10);
    }
    public boolean isContextEnabled() {
        return config.getBoolean("conversation.context_enabled", false);
    }
    public String getContextToggleMessage() {
        return languageManager.getMessage("context_toggle", "&eContext is now %s.");
    }
    public String getContextToggleEnabledMessage() {
        return languageManager.getMessage("context_toggle_enabled", "&aenabled");
    }
    public String getContextToggleDisabledMessage() {
        return languageManager.getMessage("context_toggle_disabled", "&edisabled");
    }
    public String getClearMessage() {
        return languageManager.getMessage("clear", "&aConversation history has been cleared.");
    }
    public String getCharacterSwitchedMessage() {
        return languageManager.getMessage("character_switched", "&aSwitched to character: %s");
    }
    public String getAvailableCharactersMessage() {
        return languageManager.getMessage("available_characters", "&eAvailable characters:");
    }
    public String getInvalidCharacterMessage() {
        return languageManager.getMessage("invalid_character", "&cInvalid character. Use /chatgpt character to list available characters.");
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