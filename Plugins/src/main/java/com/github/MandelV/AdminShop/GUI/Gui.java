package com.github.MandelV.AdminShop.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Gui{

    private UUID uuid;
    private Inventory inv;

    public Gui(int invSize, String invName){

        this.uuid = UUID.randomUUID();
        this.inv = Bukkit.createInventory(null, invSize, invName);
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

    public boolean setItem(final int slot, GuiItem item) {
        if (slot >= this.inv.getSize()) {
            return false;
        }

        this.inv.setItem(slot, item);

        return true;
    }
    public boolean setItem(GuiItem item) {

        int valideSlot = 0;

        for(int i = 0; i < this.inv.getSize(); i++){
            if(!(this.inv.getItem(i) instanceof GuiItem)){
                return false;
            }else{
                valideSlot = i;

            }
        }

        this.inv.setItem(valideSlot, item);

        return true;
    }
}


