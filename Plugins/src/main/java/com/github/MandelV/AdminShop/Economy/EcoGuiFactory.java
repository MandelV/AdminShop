package com.github.MandelV.AdminShop.Economy;

import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.GUI.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public abstract class EcoGuiFactory {


    /**
     *
     * @param nbrLine Enum qui représente chaque ligne possible 6 max
     * @param name le nom de l'inventaire
     * @return le nouveau Gui
     * @see GuiInvRow
     * @see Gui
     */
    public static Gui createGui(GuiInvRow nbrLine, String name, String displayName){
        return new Gui(nbrLine, name, displayName);
    }

    /**
     *Permet de rajouter à un Gui un sous Gui.
     * @param nrgLine Nombre de ligne du GUI enfant
     * @param name Nom du sous-Gui
     * @param parentGui Gui parent
     * @param description Description de l'item permettant d'acceder au GUI enfant (Dans le parent)
     * @param icon Icon (ItemGui) qui représentera le sous GUI dans le parent
     * @param nameIcon Nom de l'icon.
     * @return Le sous gui
     * @see GuiInvRow
     * @see Material
     * @see Gui
     */
    public static Gui createSubGui(GuiInvRow nrgLine, String name, String displayName, Gui parentGui, List<String> description, Material icon, final String nameIcon){

        if(nrgLine == null || name == null || displayName == null || parentGui == null || icon == null || nameIcon == null){
            return null;
        }
        Gui childGui = new Gui(nrgLine, name, displayName);

        //Creation de l'icon permettant d'aller au sous Gui
        GuiItem iconItem = new GuiItem(icon, 1, (short) 0, new GuiAction() {
            @Override
            public boolean onRightClick(Player player) { return false; }
            @Override
            public boolean onLeftClick(Player player) {
                childGui.open(player, false);
                return false;
            }

            @Override
            public boolean onMiddleClick(Player player) {

                if(AdminShop.playerIsEditorMode(player) && player.hasPermission("adminshop.edit")){

                    System.err.println("SALUT");
                }
                return true;
            }

            @Override
            public boolean onShiftLeftClick(Player player) {
                return false;
            }

            @Override
            public boolean onShiftRightClick(Player player) {
                return false;
            }
        });

        //Set Display Name
        iconItem.setName(nameIcon);
        //Set description
       iconItem.setDefaultDescription(description);

        //Ajout de l'item permettant d'aller au sous GUI
        parentGui.addItem(iconItem);


        // Create custom Navbar
        List<GuiItem> customNavbar = new ArrayList<>();

        customNavbar.add(null);
        customNavbar.add(null);
        customNavbar.add(null);


        GuiItem backtocategories = new GuiItem(Material.BARRIER, 1, (short) 0, new GuiAction() {
            @Override
            public boolean onRightClick(Player player) {
                return false;
            }

            @Override
            public boolean onLeftClick(Player player) {
                parentGui.open(player, false);
                return false;
            }

            @Override
            public boolean onMiddleClick(Player player) {
                return false;
            }

            @Override
            public boolean onShiftLeftClick(Player player) {
                return false;
            }

            @Override
            public boolean onShiftRightClick(Player player) {
                return false;
            }
        });
        backtocategories.setName(AdminShop.getInstance().getMessage().getCustomConfig().getString("back_to_categories_name"));
        backtocategories.addRowToDefaultDescription(AdminShop.getInstance().getMessage().getCustomConfig().getString("back_to_categories"));
        customNavbar.add(backtocategories);
        customNavbar.add(null);
        customNavbar.add(null);
        customNavbar.add(null);


        childGui.setCustomNavbar(customNavbar);

        //Return du nouveau Gui
        return childGui;
    }
}
