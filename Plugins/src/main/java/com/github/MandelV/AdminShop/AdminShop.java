package com.github.MandelV.AdminShop;

import Dao.Dao;
import Dao.Dao_Categorie;
import Dao.Request;
import com.github.MandelV.AdminShop.Commands.PlayerCmds;
import com.github.MandelV.AdminShop.Economy.EcoItem;
import com.github.MandelV.AdminShop.Economy.ItemStatut;
import com.github.MandelV.AdminShop.GUI.*;
import com.github.MandelV.AdminShop.config.ConfigFile;
import com.github.MandelV.AdminShop.config.Message;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AdminShop extends JavaPlugin{


    private ConfigFile config;
    private Message message;
    public List<Gui> categories;
    public Gui shop;
    private static AdminShop adminShop;
    public static Economy econ = null;

    @Override
    public void onEnable() {


        adminShop = this;
        ChatFormatting.getInstance();
        this.shop = new Gui(GuiInvRow.ROW2, "&4AdminShop &f- &eCategories");
        this.categories = new ArrayList<>();

        AdminShop self = this;


        /*shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
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
        //List<String> desc = new ArrayList<>();

        //desc.add("&aCatégorie minerai");
        //desc.add("&aYOLO test");
        //Gui Minerai = EcoGuiFactory.createSubGui(GuiInvRow.ROW3, "Minerais", this.shop,desc,Material.DIAMOND_ORE, "&bMinerai");
        /*Minerai.addItem(new  EcoItem(Material.DIAMOND_ORE, 64, (short)0, 25, 30, ItemStatut.BOTH, null));

        for(int i = 0; i < 100; i++){
            Minerai.addItem(new  EcoItem(Material.IRON_ORE, 64, (short)0, 25, 30, ItemStatut.BOTH, null));
        }*/



       // shop.addItem(new EcoItem(Material.DIAMOND, 64, (short)0, 25, 30, ItemStatut.BOTH, null));

        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &5By Akitoshi and Hougo13"));
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

        //INTIALISATION GUI
        this.initAdminShopGui();






        //INITIALIZATION ECONOMY (VAULT)
        //Init economy
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &eVaultAPI"));
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &2FINI"));


        //INITIALISATION COMMANDE
        this.getCommand("adminshop").setExecutor(new PlayerCmds(this));
    }

    @Override
    public void onDisable() {


    }

    public static AdminShop getInstance(){
        return adminShop;
    }

    private void initAdminShopGui(){

        AdminShop self = this;

        List<Dao_Categorie> DAOcategories = Request.getCategories();
        //Gui.getInstance();
        getServer().getPluginManager().registerEvents(new GuiManager(), this);

        DAOcategories.forEach(cat -> {
            Material item = Material.getMaterial(cat.getId_item());
            System.out.println(item.toString());


            GuiItem tempItem = new GuiItem(item, 1, (short)0, null);

            ItemMeta dataItem = tempItem.getItemMeta();
            dataItem.setDisplayName(ChatFormatting.formatText(cat.getName()));
            dataItem.setLore(cat.getDescriptions());
            tempItem.setItemMeta(dataItem);
            self.shop.addItem(tempItem);
            self.shop.addItem(null);
        });

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
