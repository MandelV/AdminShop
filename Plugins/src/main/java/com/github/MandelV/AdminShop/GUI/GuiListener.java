package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.Economy.EcoItem;
import javafx.beans.Observable;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked(); // The player that clicked the item
        ItemStack clicked = event.getCurrentItem();

        Inventory inventory = event.getInventory(); // The inventory that was clicked in*





        if (inventory.getName().equals("test")) {
            event.setCancelled(true);


        }
    }
}
