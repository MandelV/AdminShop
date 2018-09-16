package com.github.MandelV.AdminShop.Commands;

import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author MandelV
 * @version 1.0
 * @see PlayerCmds
 */
abstract class Commands implements CommandExecutor {

    AdminShop adminShop;
    String prefix;


     Commands(AdminShop adminShop){
        this.adminShop = adminShop;
        this.prefix = ChatFormatting.formatText(this.adminShop.getMessage().getCustomConfig().getString("prefix"));
    }

    protected abstract void commandHelp(CommandSender sender);
}
