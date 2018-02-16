package com.github.MandelV.AdminShop.Economy;

import com.github.MandelV.AdminShop.GUI.GuiItem;
import org.bukkit.Material;

public class EcoItem extends GuiItem {

    private double buy_price;
    private double sell_price;
    private ItemStatut statut;


    public EcoItem(Material type, int amount, short damage, final double buy_price, final double sell_price, ItemStatut statut){

        super(type, amount, damage);

        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.statut = statut;

    }
}
