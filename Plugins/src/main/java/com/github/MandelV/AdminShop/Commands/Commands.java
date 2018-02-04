package com.github.MandelV.AdminShop.Commands;

import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class Commands implements CommandExecutor {



    protected AdminShop grade;
    protected String prefix;


    protected Commands(AdminShop grade){

        this.grade = grade;
        this.prefix = ChatFormatting.formatText(this.grade.getMessage().getCustomConfig().getString("prefix"));
    }


    protected abstract boolean command_help(CommandSender sender);

}
