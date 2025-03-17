package com.ddaodan.MineChatGPT.util;

import org.bukkit.ChatColor;

public class ChatUtils {
    public static String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}