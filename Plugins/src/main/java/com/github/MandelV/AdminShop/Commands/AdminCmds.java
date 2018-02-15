package com.github.MandelV.AdminShop.Commands;

import Dao.Dao;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AdminCmds extends Commands {

    private boolean return_cmd = false;
    public AdminCmds(AdminShop adminShop){
        super(adminShop);
    }

    @Override
    protected boolean command_help(CommandSender sender) {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender.hasPermission("adminshop.admin")){

            if(args.length == 0)
                return this.command_help(commandSender);

            if(args[0].equalsIgnoreCase("reload")){
                this.return_cmd =  this.command_reload(commandSender);
            }

        }else {
            commandSender.sendMessage(this.prefix + ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
        }
        if(!this.return_cmd){
            this.command_help(commandSender);
        }
        return true;
    }

    private boolean command_reload(CommandSender sender){

        //Reload config
        adminShop.getConf().reloadCustomConfig();
        adminShop.getMessage().reloadCustomConfig();

        //Reload database
        Dao.getInstance().closeConnection();

        Dao.getInstance().setBdd_address(adminShop.getConf().getCustomConfig().getString("Database.address"));
        Dao.getInstance().setBdd_port(adminShop.getConf().getCustomConfig().getInt("Database.port"));
        Dao.getInstance().setBdd_name(adminShop.getConf().getCustomConfig().getString("Database.name"));
        Dao.getInstance().setBdd_username(adminShop.getConf().getCustomConfig().getString("Database.username"));
        Dao.getInstance().setBdd_password(adminShop.getConf().getCustomConfig().getString("Database.password"));

        Dao.getInstance().connection();
        if(Dao.getInstance().getBdd_connection() == null){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("plugin_reload_failure")));
        }else{
            sender.sendMessage(this.prefix + ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("plugin_reload_success")));
        }
        return true;
    }
}
