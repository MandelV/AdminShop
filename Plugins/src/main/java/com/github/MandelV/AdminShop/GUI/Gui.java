package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Gui{
    private UUID uuid;
    private Inventory inv;
    private Map<Integer, GuiAction> actions;
    private Map<UUID, Gui> GUIChildren;

    public Gui(int invSize, String invName){

        this.uuid = UUID.randomUUID();
        this.inv = Bukkit.createInventory(null, invSize, invName);
        this.actions = new HashMap<>();
        this.GUIChildren = new HashMap<>();
    }

    public Inventory getYourInventory() {
        return this.inv;
    }

    public String getName(){
        return this.inv.getName();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void addChildren(Gui gui){
        this.GUIChildren.put(gui.getUuid(), gui);
    }

    public void removeChildren(String name){
        for(Map.Entry<UUID, Gui> element : this.GUIChildren.entrySet()) {
            if (element.getValue().getName().equalsIgnoreCase(name)) {
                this.GUIChildren.remove(element.getKey());
            }
        }
    }

    public void removeChildren(UUID uuid){
        this.GUIChildren.remove(uuid);
    }

    public boolean setItem(final int slot, final ItemStack stack, GuiAction action){
        if(slot >= this.inv.getSize()){
            return false;
        }

        this.inv.setItem(slot, stack);
        if(action != null){
            this.actions.put(slot, action);

            return true;
        }

        return false;
    }

    public void open(Player p){
        p.openInventory(this.inv);
    }
}


