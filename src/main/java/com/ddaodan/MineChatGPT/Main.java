package com.ddaodan.MineChatGPT;

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
}