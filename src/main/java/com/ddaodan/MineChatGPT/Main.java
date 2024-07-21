package com.ddaodan.MineChatGPT;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

import java.util.Objects;

public final class Main extends JavaPlugin {
    private ConfigManager configManager;
    private CommandHandler commandHandler;
    private MineChatGPTTabCompleter tabCompleter;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        commandHandler = new CommandHandler(this, configManager);
        tabCompleter = new MineChatGPTTabCompleter(configManager);
        Objects.requireNonNull(getCommand("chatgpt")).setExecutor(commandHandler);
        Objects.requireNonNull(getCommand("chatgpt")).setTabCompleter(tabCompleter);
        checkAndUpdateConfig();
        if (configManager.isDebugMode()) {
            getLogger().info( "DEBUG MODE IS TRUE!!!!!");
        }
        // Initialize bStats
        int pluginId = 22635;
        new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void checkAndUpdateConfig() {
        String currentVersion = getConfig().getString("version", "1.0");
        String pluginVersion = getDescription().getVersion();

        if (!currentVersion.equals(pluginVersion)) {
            // 加载默认配置文件
            FileConfiguration defaultConfig = getConfig();
            reloadConfig();
            FileConfiguration newConfig = getConfig();

            // 合并配置文件
            for (String key : defaultConfig.getKeys(true)) {
                if (!newConfig.contains(key)) {
                    newConfig.set(key, defaultConfig.get(key));
                }
            }

            // 更新版本号
            newConfig.set("version", pluginVersion);
            saveConfig();
        }
    }
}