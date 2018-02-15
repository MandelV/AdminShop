package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GuiItem extends ItemStack{

    private ItemMeta dataItem;
    private List<String> description;

    private double prix_achat;
    private double prix_vente;
    private ItemStatut statut;

    public GuiItem(Material type, int amount, short damage, double prix_achat, double prix_vente, ItemStatut statut){

        super(type, amount, damage);


        this.prix_achat = prix_achat;
        this.prix_vente = prix_vente;
        this.statut = statut;

        this.description = new ArrayList<>();

        this.setItemMeta(this.dataItem);
    }

    public void execAction(CommandSender commandSender){
        commandSender.sendMessage("YOLO");

    }


    public void setName(final String displayName){
        this.dataItem.setDisplayName(displayName);
    }

    public void addLineDescription(final String description){
        this.description.add(ChatFormatting.formatText(description));
        this.dataItem.setLore(this.description);
    }
}
