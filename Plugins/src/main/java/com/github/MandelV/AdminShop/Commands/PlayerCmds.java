package com.github.MandelV.AdminShop.Commands;


import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.Economy.EcoItem;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiInvLine;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.AdminShop.Economy.ItemStatut;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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


                System.out.println("Open inventory");
                adminShop.shop.open(adminShop.getServer().getPlayer(commandSender.getName()));

            }
            else if(args[0].equalsIgnoreCase("up")){

                adminShop.shop.pageUp();

            }
            else if(args[0].equalsIgnoreCase("down")){

                adminShop.shop.pageDown();

            }
            else if(args[0].equalsIgnoreCase("size")){

                commandSender.sendMessage("nombre de page : " + adminShop.shop.getnbrPage());

            }
            else if(args[0].equalsIgnoreCase("actuel")){

                commandSender.sendMessage("Page actuelle : " + adminShop.shop.getCurrentPage());

            }

        }
        return true;
    }



}
