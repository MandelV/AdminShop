package com.github.MandelV.AdminShop;

import com.github.MandelV.AdminShop.Commands.PlayerCmds;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiListener;
import com.github.MandelV.AdminShop.config.ConfigFile;
import com.github.MandelV.AdminShop.config.Items;
import com.github.MandelV.AdminShop.config.Message;
import com.github.MandelV.AdminShop.tools.ChatFormatting;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminShop extends JavaPlugin{


    private ConfigFile config;
    private Message message;
    private Items items;
    private Gui shop;
    public static Economy econ = null;
    @Override
    public void onEnable() {
        ChatFormatting.getInstance();
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &5By Akitoshi"));
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &5Version : 1.0-SNAPSHOT"));
        this.getServer().getConsoleSender().sendMessage("");
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &dconfig.yml"));

        //INITIALIZATION CONFIG FILES
        this.config = new ConfigFile(this);
        config.reloadCustomConfig();

        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &dmessage.yml"));
        this.message = new Message(this);
        message.reloadCustomConfig();

        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &ditems.yml"));
        this.items = new Items(this);
        items.reloadCustomConfig();

        Gui.getInstance();
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("prefix test : " + message.getCustomConfig().getString("prefix")));
        this.getCommand("adminshop").setExecutor(new PlayerCmds(this));

        getServer().getPluginManager().registerEvents(new GuiListener(), this);






        //INITIALIZATION ECONOMY (VAULT)
        //Init economy
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &eVaultAPI"));
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &2FINI"));
    }
    @Override
    public void onDisable() {


    }
        private boolean setupEconomy() {//Initialization economy method
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public ConfigFile getConf() {
        return config;
    }
    public static Economy getEcon() {
        return econ;
    }
    public Message getMessage() {
        return message;
    }

}
