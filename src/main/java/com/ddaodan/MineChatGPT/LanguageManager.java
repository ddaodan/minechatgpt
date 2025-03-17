package com.ddaodan.MineChatGPT;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LanguageManager {
    private final Main plugin;
    private FileConfiguration langConfig;
    private String currentLanguage;
    private File langFile;

    public LanguageManager(Main plugin, String language) {
        this.plugin = plugin;
        this.currentLanguage = language;
        loadLanguage();
    }

    public void loadLanguage() {
        langFile = new File(plugin.getDataFolder(), "lang" + File.separator + currentLanguage + ".yml");
        
        // 如果语言文件不存在，创建默认语言文件
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            plugin.saveResource("lang/" + currentLanguage + ".yml", false);
        }
        
        langConfig = YamlConfiguration.loadConfiguration(langFile);
        
        // 设置默认值，以防语言文件中缺少某些键
        InputStream defaultLangStream = plugin.getResource("lang/" + currentLanguage + ".yml");
        if (defaultLangStream != null) {
            YamlConfiguration defaultLang = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultLangStream, StandardCharsets.UTF_8));
            langConfig.setDefaults(defaultLang);
        }
    }

    public void setLanguage(String language) {
        this.currentLanguage = language;
        loadLanguage();
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    private String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage(String path) {
        return translateColorCodes(langConfig.getString("messages." + path, "Missing message: " + path));
    }

    public String getMessage(String path, String defaultValue) {
        return translateColorCodes(langConfig.getString("messages." + path, defaultValue));
    }
}