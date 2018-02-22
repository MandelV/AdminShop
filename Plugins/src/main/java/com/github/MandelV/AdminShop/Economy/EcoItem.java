package com.github.MandelV.AdminShop.Economy;

import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.GUI.GuiAction;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EcoItem extends GuiItem {

    private double buy_price;
    private double sell_price;
    private ItemStatut statut;

    public EcoItem(Material type, int amount, short damage, final double buy_price, final double sell_price, ItemStatut statut){

        super(type, amount, damage, true, null);

        EcoItem self = this;


        this.setGuiAction(new GuiAction() {
            @Override
            public boolean onLeftClick(Player player) {

                if(AdminShop.getEcon().has(player, self.buy_price * self.getPlayerAmount(player))){
                    player.sendMessage(ChatFormatting.formatText("&2Vous avez acheter :" + self.getType().toString()));
                    AdminShop.getEcon().withdrawPlayer(player, self.buy_price * self.getPlayerAmount(player));
                    ItemStack giveItem = new ItemStack(self.getType(), self.getPlayerAmount(player), self.getDamage());
                    player.getInventory().addItem(giveItem);
                }else{
                    player.sendMessage(ChatFormatting.formatText("&4Vous n'avez pas les fonds nécessaire"));
                }
                return false;
            }
            @Override
            public boolean onRightClick(Player player) {


                for(int i = 0; i < player.getInventory().getSize(); i++){
                    if(player.getInventory().getItem(i) != null){
                        if(player.getInventory().getItem(i).getType() == self.getType() && player.getInventory().getItem(i).getDurability() == self.getDamage()){

                            int amount = self.getPlayerAmount(player);
                            int invAmount = player.getInventory().getItem(i).getAmount();
                            int newAmount = 0;

                            if(amount > invAmount){
                                newAmount = invAmount;
                                self.setPlayerAmount(player, newAmount);

                            }else if(amount < invAmount){
                                newAmount = amount;

                            }else if(amount == invAmount){
                                newAmount = invAmount;
                            }
                            invAmount -= newAmount;
                            if(amount < 1){
                                player.getInventory().clear(i);
                            }else{
                                player.getInventory().getItem(i).setAmount(invAmount);
                            }
                            player.sendMessage("Vous avez recu : " + self.getSell_price() * newAmount);
                            AdminShop.getEcon().depositPlayer(player, self.getSell_price() * newAmount);
                            break;
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onShiftRightClick(Player player) {

                int amount = self.getPlayerAmount(player);
                amount++;
                if(amount < 64){
                    self.setPlayerAmount(player, amount);
                }else{
                    amount = 64;
                    self.setPlayerAmount(player, amount);
                }
                List<String> lore = new ArrayList<>();

               AdminShop.getInstance().getMessage().getCustomConfig().getStringList("item_lore").forEach(v ->{
                   v = v.replace("{BUY_PRICE}", String.valueOf(buy_price * self.getPlayerAmount(player)));
                   v = v.replace("{SELL_PRICE}", String.valueOf(sell_price * self.getPlayerAmount(player)));
                   lore.add(ChatFormatting.formatText(v));
                });
                setPlayerDescription(player, lore);
                return true;

            }

            @Override
            public boolean onMiddleClick(Player player) {
                int amount = self.getPlayerAmount(player);
                amount--;
                if(amount > 0){
                    self.setPlayerAmount(player, amount);
                }else{
                    amount = 1;
                    self.setPlayerAmount(player, amount);
                }

                System.err.println(amount);
                List<String> lore = new ArrayList<>();
                AdminShop.getInstance().getMessage().getCustomConfig().getStringList("item_lore").forEach(v ->{
                   v = v.replace("{BUY_PRICE}", String.valueOf(buy_price * self.getPlayerAmount(player)));
                    v = v.replace("{SELL_PRICE}", String.valueOf(sell_price * self.getPlayerAmount(player)));
                    lore.add(ChatFormatting.formatText(v));
                });
                setPlayerDescription(player, lore);
                return true;
            }

            @Override
            public boolean onShiftLeftClick(Player player) {

                int amount = self.getPlayerAmount(player);
                amount += 10;
                if(amount < 64){
                    self.setPlayerAmount(player, amount);
                }else{
                    amount = 64;
                    self.setPlayerAmount(player, amount);
                }

                System.err.println(amount);
                List<String> lore = new ArrayList<>();
                AdminShop.getInstance().getMessage().getCustomConfig().getStringList("item_lore").forEach(v ->{
                   v = v.replace("{BUY_PRICE}", String.valueOf(buy_price * self.getPlayerAmount(player)));
                    v = v.replace("{SELL_PRICE}", String.valueOf(sell_price * self.getPlayerAmount(player)));
                    lore.add(ChatFormatting.formatText(v));
                });

                setPlayerDescription(player, lore);

                return true;

            }


        });

        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.statut = statut;
        List<String> lore = new ArrayList<>();
        AdminShop.getInstance().getMessage().getCustomConfig().getStringList("item_lore").forEach(v ->{
           v = v.replace("{BUY_PRICE}", String.valueOf(buy_price));
            v = v.replace("{SELL_PRICE}", String.valueOf(sell_price));
            lore.add(ChatFormatting.formatText(v));
        });

        this.setDefaultDescription(lore);

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
