package com.github.MandelV.AdminShop.Economy;

import Dao.Dao_item;
import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiAction;
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class EcoItem extends GuiItem {

    private double buy_price;
    private double sell_price;
    private ItemStatut statut;
    private Gui parent;

    public EcoItem(Gui parent, Material type, int amount, short damage, final double buy_price, final double sell_price, ItemStatut statut){

        super(type, amount, damage, true, null);
        EcoItem self = this;

        this.setGuiAction(new GuiAction() {
            @Override
            public boolean onLeftClick(Player player) {

                if(AdminShop.getEcon().has(player, self.buy_price * self.getPlayerAmount(player.getUniqueId()))) {

                    ItemStack giveItem = new ItemStack(self.getType(), self.getPlayerAmount(player.getUniqueId()), self.getDamage());
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
                    player.sendMessage(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("player_has_no_money")));
                }
                return false;
            }
            @Override
            public boolean onRightClick(Player player) {

                for(int i = 0; i < player.getInventory().getSize(); i++){
                    if(player.getInventory().getItem(i) != null){
                        if(player.getInventory().getItem(i).getType() == self.getType() && player.getInventory().getItem(i).getDurability() == self.getDamage()){

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
                                player.getInventory().clear(i);
                            }else{
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
                   v = v.replace("{BUY_PRICE}", String.valueOf(buy_price * self.getPlayerAmount(player.getUniqueId())));
                   v = v.replace("{SELL_PRICE}", String.valueOf(sell_price * self.getPlayerAmount(player.getUniqueId())));
                   lore.add(ChatFormatting.formatText(v));
                });
                setPlayerDescription(player, lore);
                return true;
            }
            @Override
            public boolean onMiddleClick(Player player) {
                if(AdminShop.playerIsEditorMode(player) && player.hasPermission("adminshop.edit")){

                    parent.removeItem(self);

                    Dao_item requestItem = new Dao_item(self.getType().toString(), self.getDamage(), self.buy_price, self.sell_price, self.getStatut().getName());
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
                   v = v.replace("{BUY_PRICE}", String.valueOf(buy_price * self.getPlayerAmount(player.getUniqueId())));
                    v = v.replace("{SELL_PRICE}", String.valueOf(sell_price * self.getPlayerAmount(player.getUniqueId())));
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
                   v = v.replace("{BUY_PRICE}", String.valueOf(buy_price * self.getPlayerAmount(player.getUniqueId())));
                    v = v.replace("{SELL_PRICE}", String.valueOf(sell_price * self.getPlayerAmount(player.getUniqueId())));
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
