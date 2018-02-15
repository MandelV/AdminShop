package com.github.MandelV.AdminShop.Commands;


import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.AdminShop.GUI.ItemStatut;
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
            if(command.getName().equalsIgnoreCase("adminshop")){

                Gui gui = new Gui(9, "test");

                gui.setItem(0, new GuiItem(Material.DIAMOND, 10, (short)0, 25, 30, ItemStatut.BOTH));

                gui.open(adminShop.getServer().getPlayer(commandSender.getName()));

            }

        }
        return true;
    }



}
