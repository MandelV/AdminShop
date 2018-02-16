package com.github.MandelV.AdminShop;

import Dao.Dao;
import com.github.MandelV.AdminShop.Commands.PlayerCmds;
import com.github.MandelV.AdminShop.Economy.EcoGuiFactory;
import com.github.MandelV.AdminShop.Economy.EcoItem;
import com.github.MandelV.AdminShop.Economy.ItemStatut;
import com.github.MandelV.AdminShop.GUI.*;
import com.github.MandelV.AdminShop.config.ConfigFile;
import com.github.MandelV.AdminShop.config.Message;
import com.github.MandelV.AdminShop.tools.ChatFormatting;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AdminShop extends JavaPlugin{


    private ConfigFile config;
    private Message message;
    public Gui shop;
    public static Economy econ = null;
    @Override
    public void onEnable() {


        this.shop = new Gui(GuiInvRow.ROW2, "test");

        ChatFormatting.getInstance();
        this.shop = new Gui(GuiInvRow.ROW1, "test");


        AdminShop self = this;

        shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        shop.addItem(null);
        shop.addItem(new GuiItem(Material.DIAMOND_SWORD, 64, (short) 0, new GuiAction() {
            @Override
            public void onRightClick(Player player) {
                self.shop.pageUp(player);
            }

            @Override
            public void onLeftClick(Player player) {

            }
        }));
        shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));

        List<String> desc = new ArrayList<>();

        desc.add("&aCatégorie minerai");
        desc.add("&aYOLO test");
       Gui Minerai = EcoGuiFactory.createSubGui(GuiInvRow.ROW6, "Minerais", this.shop,desc,Material.DIAMOND_ORE, "&bMinerai");
        Minerai.addItem(new  EcoItem(Material.DIAMOND_ORE, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        Minerai.addItem(new  EcoItem(Material.IRON_ORE, 64, (short)0, 25, 30, ItemStatut.BOTH, null));


        shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));

        shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        shop.addItem(new EcoItem(Material.WOOD, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        shop.addItem(new EcoItem(Material.GLASS, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        shop.addItem(new EcoItem(Material.IRON_AXE, 64, (short)0, 25, 30, ItemStatut.BOTH, null));

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

        //INITIALIZATION DAO
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &bBase de donnée"));
        Dao.getInstance(
                this.config.getCustomConfig().getString("Database.address"),
                this.config.getCustomConfig().getInt("Database.port"),
                this.config.getCustomConfig().getString("Database.name"),
                this.config.getCustomConfig().getString("Database.username"),
                this.config.getCustomConfig().getString("Database.password"),
                this.config.getCustomConfig().getBoolean("Database.useSSL"));

        if(Dao.getInstance().getBdd_connection() == null){
            this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &bBase de données &f: &4ERREUR !"));
            getServer().getPluginManager().disablePlugin(this);
        }

        //Gui.getInstance();
        getServer().getPluginManager().registerEvents(new GuiManager(), this);

        this.getCommand("adminshop").setExecutor(new PlayerCmds(this));



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
