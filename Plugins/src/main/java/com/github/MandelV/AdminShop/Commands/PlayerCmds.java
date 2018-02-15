package com.github.MandelV.AdminShop.Commands;


import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiInvLine;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.AdminShop.GUI.ItemStatut;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PlayerCmds extends Commands {

    Gui gui = new Gui(GuiInvLine.LINE2, "test");

    private boolean return_cmd = false;
    public PlayerCmds(AdminShop adminShop){
        super(adminShop);

        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND_SWORD, 1, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH));

        gui.addItem(new GuiItem(Material.WOOD, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.GLASS, 64, (short)0, 25, 30, ItemStatut.BOTH));
        gui.addItem(new GuiItem(Material.IRON_AXE, 64, (short)0, 25, 30, ItemStatut.BOTH));
    }

    @Override
    protected boolean command_help(CommandSender sender) {
        return false;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender.hasPermission("adminshop.player")) {
            if(command.getName().equalsIgnoreCase("adminshop") && args.length == 0){

                gui.open(adminShop.getServer().getPlayer(commandSender.getName()));

            }
            else if(args[0].equalsIgnoreCase("up")){

                gui.pageUp();

            }
            else if(args[0].equalsIgnoreCase("down")){

                gui.pageDown();

            }
            else if(args[0].equalsIgnoreCase("size")){

                commandSender.sendMessage("nombre de page : " + gui.getnbrPage());

            }
            else if(args[0].equalsIgnoreCase("actuel")){

                commandSender.sendMessage("Page actuelle : " + gui.getCurrentPage());

            }

        }
        return true;
    }



}
