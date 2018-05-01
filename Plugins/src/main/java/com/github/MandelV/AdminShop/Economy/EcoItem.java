package com.github.MandelV.AdminShop.Economy;

import Dao.Dao_item;
import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiAction;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.MonsterEggs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MandelV, Hougo13
 * @version 1.0
 * Representation of Economic item
 */
public class EcoItem extends GuiItem {

    private double buy_price;
    private double sell_price;
    private ItemStatut statut;
    private Gui parent;

    /**
     * @param parent Gui parent of this item
     * @param type item's type
     * @param amount amount
     * @param damage (sub-id for block and damage for item)
     * @param buy_price .
     * @param sell_price .
     * @param statut {sell, buy, both}
     * @see Material
     * @see ItemStack
     * @see ItemStatut
     */
    public EcoItem(Gui parent, Material type, int amount, short damage, ItemMeta meta, final double buy_price, final double sell_price, ItemStatut statut){

        super(type, amount, damage, meta, true, null);
        EcoItem self = this;

        this.setGuiAction(new GuiAction() {
            @Override
            public boolean onLeftClick(Player player) {

                if(self.getStatut() == ItemStatut.BUY || self.getStatut() == ItemStatut.BOTH){
                    if(AdminShop.getEcon().has(player, self.buy_price * self.getPlayerAmount(player.getUniqueId()))) {

                        ItemStack giveItem = new ItemStack(self.getType(), self.getPlayerAmount(player.getUniqueId()), self.getDamage());
                        giveItem.setItemMeta(self.getMeta());
                        int emptyInvSpace = 0;
                        int availableItemSpace = 0;
                        for(ItemStack item : player.getInventory().getStorageContents()){
                            if(item == null){
                                emptyInvSpace++;
                                break;
                            }else{
                                if((item.getAmount() + giveItem.getAmount()) <= 64
                                        && item.getType() == giveItem.getType()
                                        && item.getDurability() == giveItem.getDurability()
                                        && item.getEnchantments() == giveItem.getEnchantments()){
                                    availableItemSpace++;
                                    break;
                                }
                            }
                        }

                        if(emptyInvSpace > 0 || availableItemSpace > 0){
                            AdminShop.getEcon().withdrawPlayer(player, self.buy_price * self.getPlayerAmount(player.getUniqueId()));
                            player.getInventory().addItem(giveItem);

                            //History
                            Request.addEntryHistory(player.getName(), giveItem.getType().name(), self.getPlayerAmount(player.getUniqueId()), "BUY");

                            String successBuy = AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix");
                            successBuy += AdminShop.getInstance().getMessage().getCustomConfig().getString("buy_message");
                            successBuy = successBuy.replace("{ITEM}", self.getType().toString().toLowerCase());
                            successBuy = successBuy.replace("{AMOUNT}", String.valueOf(self.getPlayerAmount(player.getUniqueId())));
                            successBuy = successBuy.replace("{BUY_PRICE}", String.valueOf(self.buy_price * self.getPlayerAmount(player.getUniqueId())));

                            player.sendMessage(ChatFormatting.formatText(successBuy));
                        }else{
                            player.sendMessage(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix") +
                                    AdminShop.getInstance().getMessage().getCustomConfig().getString("player_has_full_inventory")));

                        }
                    }else{
                        player.sendMessage(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix") +
                                AdminShop.getInstance().getMessage().getCustomConfig().getString("player_has_no_money")));
                    }

                }else{

                    player.sendMessage(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix") +
                            AdminShop.getInstance().getMessage().getCustomConfig().getString("player_cannot_buy")));
                }

                return false;
            }
            @Override
            public boolean onRightClick(Player player) {

                if(self.getStatut() == ItemStatut.SELL || self.getStatut() == ItemStatut.BOTH){
                    for(int i = 0; i < player.getInventory().getSize(); i++){
                        if(player.getInventory().getItem(i) != null){



                            if(player.getInventory().getItem(i).getType() == self.getType()
                                    && player.getInventory().getItem(i).getDurability() == self.getDamage()
                                    && player.getInventory().getItem(i).getItemMeta().toString().equalsIgnoreCase(self.getMeta().toString())){

                                int amount = self.getPlayerAmount(player.getUniqueId());
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
                                    Request.addEntryHistory(player.getName(), player.getInventory().getItem(i).getType().name(), newAmount, "SELL");
                                    player.getInventory().clear(i);

                                }else{
                                    Request.addEntryHistory(player.getName(), player.getInventory().getItem(i).getType().name(), newAmount, "SELL");
                                    player.getInventory().getItem(i).setAmount(invAmount);

                                }
                                AdminShop.getEcon().depositPlayer(player, self.getSell_price() * newAmount);

                                String successSell = AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix");
                                successSell += AdminShop.getInstance().getMessage().getCustomConfig().getString("sell_message");
                                successSell = successSell.replace("{ITEM}", self.getType().toString().toLowerCase());
                                successSell = successSell.replace("{AMOUNT}", String.valueOf(self.getPlayerAmount(player.getUniqueId())));
                                successSell = successSell.replace("{SELL_PRICE}", String.valueOf(self.sell_price * self.getPlayerAmount(player.getUniqueId())));
                                player.sendMessage(ChatFormatting.formatText(successSell));

                                break;
                            }
                        }
                    }
                }else {
                    player.sendMessage(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix") +
                            AdminShop.getInstance().getMessage().getCustomConfig().getString("player_cannot_sell")));
                }

                return true;
            }
            @Override
            public boolean onShiftRightClick(Player player) {

                int amount = self.getPlayerAmount(player.getUniqueId());
                int x16 = amount / 16 - 1;
                amount = 16 * x16;
                if(amount < 1){
                    amount = 1;
                    self.setPlayerAmount(player, amount);
                }else{
                    self.setPlayerAmount(player, amount);
                }
                List<String> lore = new ArrayList<>();

               AdminShop.getInstance().getMessage().getCustomConfig().getStringList("item_lore").forEach(v ->{
                   if(self.getStatut() == ItemStatut.BUY || self.getStatut() == ItemStatut.BOTH){
                       v = v.replace("{BUY_PRICE}", String.valueOf(buy_price));
                   }else{
                       v = v.replace("{BUY_PRICE}", "&cNon achetable");
                   }

                   if(self.getStatut() == ItemStatut.SELL || self.getStatut() == ItemStatut.BOTH){
                       v = v.replace("{SELL_PRICE}", String.valueOf(sell_price));
                   }else{
                       v = v.replace("{SELL_PRICE}", "&cNon vendable");
                   }
                   lore.add(ChatFormatting.formatText(v));
                });
                setPlayerDescription(player, lore);
                return true;
            }
            @Override
            public boolean onMiddleClick(Player player) {
                if(AdminShop.playerIsEditorMode(player) && player.hasPermission("adminshop.edit")){

                    parent.removeItem(self);
                    
                    Dao_item requestItem = new Dao_item(self.getType().toString(), self.getDamage(), AdminShop.itemSerialization(self.getItemStack()), self.buy_price, self.sell_price, self.getStatut().getName());
                    Request.removeItemFromCategorie(parent.getName(), requestItem);
                    //success_remove_item
                    String successRemoveItem = AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix");
                    successRemoveItem += AdminShop.getInstance().getMessage().getCustomConfig().getString("success_remove_item");
                    successRemoveItem = successRemoveItem.replace("{ITEM}", self.getType().toString());
                    successRemoveItem = successRemoveItem.replace("{CAT}", parent.getName());
                    player.sendMessage(ChatFormatting.formatText(successRemoveItem.toLowerCase()));
                    parent.refreshAll();
                    return true;
                }
                int amount = self.getPlayerAmount(player.getUniqueId());
                amount++;
                if(amount > 64){
                    amount = 64;
                    self.setPlayerAmount(player, amount);
                }else{
                    self.setPlayerAmount(player, amount);
                }

                List<String> lore = new ArrayList<>();
                AdminShop.getInstance().getMessage().getCustomConfig().getStringList("item_lore").forEach(v ->{
                    if(self.getStatut() == ItemStatut.BUY || self.getStatut() == ItemStatut.BOTH){
                        v = v.replace("{BUY_PRICE}", String.valueOf(buy_price));
                    }else{
                        v = v.replace("{BUY_PRICE}", "&cNon achetable");
                    }

                    if(self.getStatut() == ItemStatut.SELL || self.getStatut() == ItemStatut.BOTH){
                        v = v.replace("{SELL_PRICE}", String.valueOf(sell_price));
                    }else{
                        v = v.replace("{SELL_PRICE}", "&cNon vendable");
                    }
                    lore.add(ChatFormatting.formatText(v));
                });
                setPlayerDescription(player, lore);
                return true;
            }
            @Override
            public boolean onShiftLeftClick(Player player) {

                int amount = self.getPlayerAmount(player.getUniqueId());
                int x16 = amount / 16 + 1;
                amount = 16 * x16;
                if(amount < 64){
                    self.setPlayerAmount(player, amount);
                }else{
                    amount = 64;
                    self.setPlayerAmount(player, amount);
                }
                List<String> lore = new ArrayList<>();
                AdminShop.getInstance().getMessage().getCustomConfig().getStringList("item_lore").forEach(v ->{
                    if(self.getStatut() == ItemStatut.BUY || self.getStatut() == ItemStatut.BOTH){
                        v = v.replace("{BUY_PRICE}", String.valueOf(buy_price));
                    }else{
                        v = v.replace("{BUY_PRICE}", "&cNon achetable");
                    }

                    if(self.getStatut() == ItemStatut.SELL || self.getStatut() == ItemStatut.BOTH){
                        v = v.replace("{SELL_PRICE}", String.valueOf(sell_price));
                    }else{
                        v = v.replace("{SELL_PRICE}", "&cNon vendable");
                    }
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

            if(this.getStatut() == ItemStatut.BUY || this.getStatut() == ItemStatut.BOTH){
                v = v.replace("{BUY_PRICE}", String.valueOf(buy_price));
            }else{
                v = v.replace("{BUY_PRICE}", "Non achetable");
            }

           if(this.getStatut() == ItemStatut.SELL || this.getStatut() == ItemStatut.BOTH){
               v = v.replace("{SELL_PRICE}", String.valueOf(sell_price));
           }else{
               v = v.replace("{SELL_PRICE}", "Non vendable");
           }

            lore.add(ChatFormatting.formatText(v));
        });

        this.setDefaultDescription(lore);
    }

    /**
     * @return buy price
     */
    public double getBuy_price() {
        return buy_price;
    }

    /**
     * @return sell price
     */
    public double getSell_price() {
        return sell_price;
    }

    /**
     * @return statut
     * @see ItemStatut
     */
    public ItemStatut getStatut() {
        return statut;
    }

    /**
     * @param buy_price set buy price
     */
    public void setBuy_price(double buy_price) {
        this.buy_price = buy_price;
    }

    /**
     * @param sell_price set sell price
     */
    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    /**
     * @param statut set statut
     * @see ItemStatut
     */
    public void setStatut(ItemStatut statut) {
        this.statut = statut;
    }
}
