package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;

public class Gui{
    private Inventory inv;
    private String name;


    private Gui(final String name){

        this.name = name;
        this.inv = Bukkit.createInventory(null, 9, ChatFormatting.formatText(name));

        this.inv.addItem(new ItemStack(1, 2));


    }

    public void setName(final String name){

    }

    public Inventory getInv() {
        return inv;
    }

    public String getName() {
        return this.inv.getName();
    }

    private static Gui gui = new Gui("&4AdminShop");

    public static Gui getInstance(){

        return gui;
    }




}


