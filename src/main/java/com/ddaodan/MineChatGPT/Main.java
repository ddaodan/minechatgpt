package com.ddaodan.MineChatGPT;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    private ConfigManager configManager;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        commandHandler = new CommandHandler(this, configManager);
        Objects.requireNonNull(getCommand("chatgpt")).setExecutor(commandHandler);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
