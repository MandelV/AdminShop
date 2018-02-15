package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiItem extends ItemStack{

    private ItemMeta dataItem;
    private List<String> description;

    private double prix_achat = 0.0;
    private double prix_vente = 0.0;
    private ItemStatut statut;





    public GuiItem(Material type, int amount, short damage, double prix_achat, double prix_vente, ItemStatut statut){

        super(type, amount, damage);

        this.prix_achat = prix_achat;
        this.prix_vente = prix_vente;

        this.statut = statut;

        this.description = new ArrayList<>();
        this.dataItem.setLore(this.description);
        this.setItemMeta(this.dataItem);
    }


    public void setDisplayName(final String displayName){
        this.dataItem.setDisplayName(displayName);
    }

    public void addLineLore(final String description){
        this.description.add(ChatFormatting.formatText(description));
    }



    public void execAction(){

        /**
         *
         * **/
    }


}
