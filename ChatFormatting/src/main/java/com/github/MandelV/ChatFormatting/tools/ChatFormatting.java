package com.github.MandelV.ChatFormatting.tools;

import org.bukkit.ChatColor;
import java.util.HashMap;
import java.util.Map;

public class ChatFormatting {

    public static Map<String, ChatColor> tabChat;
    private static ChatFormatting chatFormatting = new ChatFormatting();

    private ChatFormatting(){

        tabChat = new HashMap<>();
        tabChat.put("&0", ChatColor.BLACK);
        tabChat.put("&1", ChatColor.DARK_BLUE);
        tabChat.put("&2", ChatColor.DARK_GREEN);
        tabChat.put("&3", ChatColor.DARK_AQUA);
        tabChat.put("&4", ChatColor.DARK_RED);
        tabChat.put("&5", ChatColor.DARK_PURPLE);
        tabChat.put("&6", ChatColor.GOLD);
        tabChat.put("&7", ChatColor.GRAY);
        tabChat.put("&8", ChatColor.DARK_GRAY);
        tabChat.put("&9", ChatColor.BLUE);
        tabChat.put("&a", ChatColor.GREEN);
        tabChat.put("&b", ChatColor.AQUA);
        tabChat.put("&c", ChatColor.RED);
        tabChat.put("&d", ChatColor.LIGHT_PURPLE);
        tabChat.put("&e", ChatColor.YELLOW);
        tabChat.put("&f", ChatColor.WHITE);

        tabChat.put("&k", ChatColor.MAGIC);
        tabChat.put("&l", ChatColor.BOLD);
        tabChat.put("&m", ChatColor.STRIKETHROUGH);
        tabChat.put("&n", ChatColor.UNDERLINE);
        tabChat.put("&o", ChatColor.ITALIC);
        tabChat.put("&r", ChatColor.RESET);
    }

    public static ChatFormatting getInstance(){
        return chatFormatting;
    }

    public static String formatText(String txt) {
        if(txt != null){
            String finalString = txt;

            for (Map.Entry<String, ChatColor> element : tabChat.entrySet()) {

                finalString = finalString.replace(element.getKey(), "" + element.getValue());
            }
            return finalString;
        }
        return ChatColor.DARK_RED + "error string";
    }
}
