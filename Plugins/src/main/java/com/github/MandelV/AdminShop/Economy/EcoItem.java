package com.github.MandelV.AdminShop.Economy;

import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.GUI.GuiAction;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EcoItem extends GuiItem {

    private double buy_price;
    private double sell_price;
    private ItemStatut statut;


    public EcoItem(Material type, int amount, short damage, final double buy_price, final double sell_price, ItemStatut statut, GuiAction action){

        super(type, amount, damage, action);

        EcoItem self = this;


        this.setGuiAction(new GuiAction() {
            @Override
            public boolean onRightClick(Player player) {

                if(AdminShop.getEcon().has(player, self.buy_price * self.getAmount())){
                    player.sendMessage(ChatFormatting.formatText("&2Vous avez acheter :" + self.getType().toString()));
                    AdminShop.getEcon().withdrawPlayer(player, self.buy_price * self.getAmount());

                    ItemStack giveItem = self.clone();
                    ItemMeta meta = giveItem.getItemMeta();
                    meta.setLore(new ArrayList<>());
                    giveItem.setItemMeta(meta);
                    player.getInventory().addItem(giveItem);
                }else{
                    player.sendMessage(ChatFormatting.formatText("&4Vous n'avez pas les fonds nécessaire"));
                }

                return false;
            }

            @Override
            public boolean onLeftClick(Player player) {

                int amount = self.getAmount();
                amount++;
                self.setAmount(amount);
                System.err.println(amount);
                ItemMeta meta = self.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(ChatFormatting.formatText("&cPrix Achat : " + String.valueOf(buy_price * self.getAmount())));
                lore.add(ChatFormatting.formatText("&2Prix Vente : " + String.valueOf(sell_price)));
                lore.add(ChatFormatting.formatText("&a&oClic droit pour acheter"));
                lore.add(ChatFormatting.formatText("&a&oClic gauche pour augmenter le nombre d'item"));

                meta.setLore(lore);

                self.setItemMeta(meta);

                return true;

            }
        });

        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.statut = statut;
        ItemMeta meta = this.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatFormatting.formatText("&cPrix Achat : " + String.valueOf(buy_price)));
        lore.add(ChatFormatting.formatText("&2Prix Vente : " + String.valueOf(sell_price)));
        lore.add(ChatFormatting.formatText("&a&oClic droit pour acheter"));
        lore.add(ChatFormatting.formatText("&a&oClic gauche pour augmenter le nombre d'item"));

        meta.setLore(lore);
        this.setItemMeta(meta);

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
