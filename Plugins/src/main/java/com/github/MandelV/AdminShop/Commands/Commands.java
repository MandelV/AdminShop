package com.github.MandelV.AdminShop.Commands;

import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class Commands implements CommandExecutor {



    protected AdminShop adminShop;
    protected String prefix;


    protected Commands(AdminShop adminShop){

        this.adminShop = adminShop;
        this.prefix = ChatFormatting.formatText(this.adminShop.getMessage().getCustomConfig().getString("prefix"));
    }


    protected abstract boolean command_help(CommandSender sender);

}
