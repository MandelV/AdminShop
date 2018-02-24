package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.UUID;

/**
 * @author Hougo13
 * @version 1.0
 * This class represents one instantiated item per player
 */
public class GuiItemInstance extends ItemStack{

    private GuiItem guiItem;
    private UUID playerUUID;

    GuiItemInstance(GuiItem guiItem, Player player) {
        super(guiItem.getType(), guiItem.getPlayerAmount(player.getUniqueId()), guiItem.getDamage());

        this.guiItem = guiItem;
        this.playerUUID = player.getUniqueId();
        this.setItemMeta(guiItem.getMeta());
        this.setDurability(this.guiItem.getDamage());
        this.refreshMeta();
    }

    /**
     * Refresh the item's metaData
     */
    public void refreshMeta() {
        ItemMeta meta = super.getItemMeta();
        meta.setDisplayName(this.guiItem.getDisplayName());
        meta.setLore(this.guiItem.getPlayerDescription(this.playerUUID));

        this.setDurability(this.guiItem.getDamage());
        super.setItemMeta(meta);
    }

    /**
     * @return return the item's amount
     */
    @Override
    public int getAmount() {
        return this.guiItem.getPlayerAmount(this.playerUUID);
    }

    /**
     * @return return the item's metadata
     */
    @Override
    public ItemMeta getItemMeta() {
        this.refreshMeta();
        return super.getItemMeta();
    }
}
