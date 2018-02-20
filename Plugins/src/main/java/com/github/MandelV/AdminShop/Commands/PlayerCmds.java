package com.github.MandelV.AdminShop.Commands;


import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PlayerCmds extends Commands {


    private boolean return_cmd = false;
    public PlayerCmds(AdminShop adminShop){
        super(adminShop);


    }

    @Override
    protected boolean command_help(CommandSender sender) {
        return false;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("adminshop.player")) {
            if(command.getName().equalsIgnoreCase("adminshop") && args.length == 0){



                adminShop.shop.open(adminShop.getServer().getPlayer(commandSender.getName()));


            }
            else if(args[0].equalsIgnoreCase("up")){

                adminShop.shop.pageUp(adminShop.getServer().getPlayer(commandSender.getName()));

            }
            else if(args[0].equalsIgnoreCase("down")){

                adminShop.shop.pageDown(adminShop.getServer().getPlayer(commandSender.getName()));

            }
            else if(args[0].equalsIgnoreCase("size")){

                commandSender.sendMessage("nombre de page : " + adminShop.shop.getnbrPage());

            }
            else if(args[0].equalsIgnoreCase("actuel")){

                commandSender.sendMessage("Page actuelle : " + adminShop.shop.getCurrentPage(adminShop.getServer().getPlayer(commandSender.getName())));

            }

        }
        return true;
    }



}
