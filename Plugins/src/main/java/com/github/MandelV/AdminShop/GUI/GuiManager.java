package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiManager implements Listener {

    private static List<Gui> guiList = new ArrayList<>();

    public static void addGui(Gui gui) {
        GuiManager.guiList.add(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked(); // The player that clicked the item
        Inventory inventory = event.getInventory(); // The inventory that was clicked in*

        for (Gui gui: GuiManager.guiList) {
            if (gui.hasPlayer(player)) {
                event.setCancelled(true);
                gui.dispatchEvent(player, event);
            }
        }

        if (inventory.getName().equals("test")) {
            event.setCancelled(true);


        }
    }
}
