package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Hougo13
 * GuiManager, event listener
 */
public class GuiManager implements Listener {

    private static List<Gui> guiList = new ArrayList<>();

    /**
     * @param gui add Gui that will be managed
     */
    public static void addGui(Gui gui) {
        GuiManager.guiList.add(gui);
    }

    /**
     * refresh all Gui
     */
    public static void refreshAll() {
        for (Gui gui: GuiManager.guiList) {
            gui.refreshAll();
        }
    }

    /**
     * @param event When player click in inventory
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked(); // The player that clicked the item
        Inventory inventory = event.getInventory(); // The inventory that was clicked in

        if(event.getRawSlot() >= 0){
            for (Gui gui: GuiManager.guiList) {
                if (gui.hasPlayer(player)) {
                    event.setCancelled(true);
                    gui.dispatchEvent(player, event);
                    break;
                }
            }
        }
    }

    /**
     * @param e when player close inventory
     */
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        for (Gui gui: GuiManager.guiList) {
            if (gui.hasPlayer(player)) {
                gui.exit(player);
            }
        }
    }
}
