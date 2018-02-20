package com.github.MandelV.AdminShop.Commands;


import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import net.milkbowl.vault.chat.Chat;
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
            else if(args[0].equalsIgnoreCase("listcategorie")){

                this.listCategorie(commandSender);

            }

        }
        return true;
    }

    private void listCategorie(CommandSender sender){
        sender.sendMessage(ChatFormatting.formatText("[&6AdminShop&f] &2Nombre de categorie : &6" + adminShop.categories.size()));
        adminShop.categories.forEach(cat ->{
            sender.sendMessage(ChatFormatting.formatText("&6"+ cat.getName()));
        });
    }


}
