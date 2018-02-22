package com.github.MandelV.AdminShop.Commands;


import Dao.Dao_Categorie;
import Dao.Dao_item;
import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.Economy.EcoGuiFactory;
import com.github.MandelV.AdminShop.Economy.EcoItem;
import com.github.MandelV.AdminShop.Economy.ItemStatut;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiInvRow;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerCmds extends Commands {


    private boolean return_cmd = false;

    public PlayerCmds(AdminShop adminShop) {
        super(adminShop);

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (command.getName().equalsIgnoreCase("adminshop") && args.length == 0) {

            this.openAdminShop(commandSender);


        } else if (args[0].equalsIgnoreCase("listcategorie")) {

            this.listCategorie(commandSender);

        } else if (args[0].equalsIgnoreCase("categorie") && (args.length > 1)) {

            if(args[1].equalsIgnoreCase("add")){
                this.addCategorie(commandSender, args);
            }

        }else if(args[0].equalsIgnoreCase("item") && (args.length > 1)){
            this.addItemIntoCategorie(commandSender, args);

        }else if(args[0].equalsIgnoreCase("editmode")){
            this.toggleEditMode(commandSender);
        }

        return true;
}
    @Override
    protected boolean command_help(CommandSender sender) {
        return false;
    }

    private boolean openAdminShop(CommandSender sender){

        if(!sender.hasPermission("adminshop.open")){
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        adminShop.shop.open(adminShop.getServer().getPlayer(sender.getName()));

        return true;
    }

    private boolean listCategorie(CommandSender sender){
        if(!sender.hasPermission("adminshop.categorie.listcategorie")){
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }

        sender.sendMessage(ChatFormatting.formatText("[&6AdminShop&f] &2Nombre de categorie : &6" + adminShop.categories.size()));
        adminShop.categories.forEach(cat ->{
            sender.sendMessage(ChatFormatting.formatText("&6"+ cat.getName()));
        });
        return true;
    }

    private boolean addCategorie(CommandSender sender, String[] args){
        if(!sender.hasPermission("adminshop.categorie.add")){
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        if(args.length == 4){
            ItemStack itemHolder = sender.getServer().getPlayer(sender.getName()).getInventory().getItemInMainHand();
            sender.getServer().getPlayer(sender.getName()).getOpenInventory().getBottomInventory().remove(itemHolder);
            if(itemHolder.getType() != Material.AIR){
                String name = (!args[2].contains("&")) ? args[2] : null;
                String displayname = args[3];

                if(name != null){

                    Gui gu = EcoGuiFactory.createSubGui(GuiInvRow.ROW6, name, displayname, adminShop.shop, null, itemHolder.getType(), itemHolder.getDurability(), displayname);
                    adminShop.categories.add(gu);

                    if(gu != null){
                        Dao_Categorie prepareSqlCategorie = new Dao_Categorie(name, displayname, itemHolder.getType().toString(), itemHolder.getDurability());
                        Request.addCategorie(prepareSqlCategorie);
                    }
                }else{
                    sender.sendMessage("&4Attention le nom d'une categorie est différent du nom affiché. ce nom ne doit contenir aucun '&'");
                }
            }




        }else{
            this.command_help(sender);
        }
        return true;
    }

    private boolean addItemIntoCategorie(CommandSender sender, String[] args){
        if(!sender.hasPermission("adminshop.item.add")){
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        if(args.length == 6){
            ItemStack itemHolder = sender.getServer().getPlayer(sender.getName()).getInventory().getItemInMainHand();
           // sender.getServer().getPlayer(sender.getName()).getOpenInventory().getBottomInventory().remove(itemHolder);
            if(itemHolder.getType() != Material.AIR){
                String catname = (!args[2].contains("&")) ? args[2] : null;

                Double buy_price;
                Double sell_price;
                try{
                    buy_price = Double.valueOf(args[3]);
                    sell_price = Double.valueOf(args[4]);
                }catch (NumberFormatException e){
                    sender.sendMessage("Prix incorrect");
                    return false;
                }
                ItemStatut statut = ItemStatut.getStatut(args[5]);
                if(statut == null){
                    sender.sendMessage("Mauvais statut");
                    return false;
                }
                if(catname != null){
                    for(int i = 0; i < adminShop.categories.size(); i++){
                        if(adminShop.categories.get(i).getName().equalsIgnoreCase(catname)){
                            adminShop.categories.get(i).addItem(new EcoItem(adminShop.categories.get(i), itemHolder.getType(), 1, (short)itemHolder.getDurability(), buy_price, sell_price, statut));
                            break;
                        }
                    }

                    Dao_item sqlitem = new Dao_item(itemHolder.getType().toString(), itemHolder.getDurability(), buy_price, sell_price,statut.getName());

                    Request.addItemIntoCategorie(catname, sqlitem);

                    sender.sendMessage("Item Ajouté");
                }
            }
        }
        return true;
    }

    private boolean toggleEditMode(CommandSender sender){
        if(!sender.hasPermission("adminshop.edit")){
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        if(!AdminShop.playerIsEditorMode(((Player) sender))){
            AdminShop.setPlayerEditionMode(((Player) sender));
            sender.sendMessage("Mode edition activé");
        }else{
            AdminShop.removePlayerEditionMode(((Player) sender));
            sender.sendMessage("Mode edition désactivé");
        }
        return true;
    }


}
