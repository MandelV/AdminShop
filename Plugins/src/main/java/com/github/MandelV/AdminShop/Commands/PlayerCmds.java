package com.github.MandelV.AdminShop.Commands;


import com.github.MandelV.AdminShop.AdminShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PlayerCmds extends Commands {

    private boolean return_cmd = false;
    public PlayerCmds(AdminShop grade){
        super(grade);
    }

    @Override
    protected boolean command_help(CommandSender sender) {
        return false;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        return true;
    }


}
