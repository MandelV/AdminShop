package com.github.MandelV.AdminShop.Economy;

import com.github.MandelV.AdminShop.GUI.GuiAction;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class EcoItem extends GuiItem {

    private double buy_price;
    private double sell_price;
    private ItemStatut statut;


    public EcoItem(Material type, int amount, short damage, final double buy_price, final double sell_price, ItemStatut statut){

        super(type, amount, damage);

        EcoItem self = this;

        this.setGuiAction(new GuiAction() {
            @Override
            public void onRightClick(Player player) {
                self.buy_price--;
                System.out.println("Prix item : " + self.buy_price);
            }

            @Override
            public void onLeftClick(Player player) {
                self.buy_price++;
                System.out.println("++" + self.buy_price);
            }
        });

        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.statut = statut;

    }


    public double getBuy_price() {
        return buy_price;
    }
    public double getSell_price() {
        return sell_price;
    }
    public ItemStatut getStatut() {
        return statut;
    }


    public void setBuy_price(double buy_price) {
        this.buy_price = buy_price;
    }
    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }
    public void setStatut(ItemStatut statut) {
        this.statut = statut;
    }
}
