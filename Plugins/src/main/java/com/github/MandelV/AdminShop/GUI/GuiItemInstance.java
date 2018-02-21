package com.github.MandelV.AdminShop.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiItemInstance extends ItemStack{

    private GuiItem guiItem;
    private Player player;

    GuiItemInstance(GuiItem guiItem, Player player) {
        super(guiItem.getType(), guiItem.getPlayerAmount(player), guiItem.getDamage());

        this.guiItem = guiItem;
        this.player = player;
        this.setDurability(this.guiItem.getDamage());

        this.refreshMeta();
    }

    public void refreshMeta() {
        ItemMeta meta = super.getItemMeta();
        meta.setDisplayName(this.guiItem.getDisplayName());
        meta.setLore(this.guiItem.getPlayerDescription(this.player));

        this.setDurability(this.guiItem.getDamage());

        super.setItemMeta(meta);
    }

    @Override
    public int getAmount() {
        return this.guiItem.getPlayerAmount(this.player);
    }

    @Override
    public ItemMeta getItemMeta() {
        this.refreshMeta();
        return super.getItemMeta();
    }
}
