package com.github.MandelV.AdminShop;

import Dao.Dao;
import Dao.Dao_Categorie;
import Dao.Request;
import com.github.MandelV.AdminShop.Commands.CmdsAutoComplet;
import com.github.MandelV.AdminShop.Commands.PlayerCmds;
import com.github.MandelV.AdminShop.Economy.EcoGuiFactory;
import com.github.MandelV.AdminShop.Economy.EcoItem;
import com.github.MandelV.AdminShop.Economy.ItemStatut;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiInvRow;
import com.github.MandelV.AdminShop.GUI.GuiManager;
import com.github.MandelV.AdminShop.config.ConfigFile;
import com.github.MandelV.AdminShop.config.Message;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Main class of AdminShop plugin
 * @see JavaPlugin
 * @see org.bukkit.Bukkit
 */
public class AdminShop extends JavaPlugin{


    private ConfigFile config;
    private Message message;
    public List<Gui> categories;
    public Gui shop;
    private static AdminShop adminShop;
    private static List<UUID> playerInEditionMode;
    public static Economy econ = null;

    /**
     * When plugin is enable
     */
    @Override
    public void onEnable() {

        adminShop = this;
        ChatFormatting.getInstance();
        this.shop = new Gui(GuiInvRow.ROW2, "adminshop",  "&4AdminShop &f- &eCategories");
        this.categories = new ArrayList<>();
        playerInEditionMode =  new ArrayList<>();
        AdminShop self = this;

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
        this.getCommand("adminshop").setTabCompleter(new CmdsAutoComplet(this));
    }

    /**
     * When plugin is disable
     */
    @Override
    public void onDisable() {

        Dao.getInstance().closeConnection();
    }

    /**
     * @return instance of adminshop
     */
    public static AdminShop getInstance(){
        return adminShop;
    }

    /**
     * Init AdminShop GUI
     */
    private void initAdminShopGui(){

        AdminShop self = this;
        List<Dao_Categorie> DAOcategories = Request.getCategories();
        getServer().getPluginManager().registerEvents(new GuiManager(), this);

        DAOcategories.forEach(cat -> {
            Material item = Material.getMaterial(cat.getId_item());
            if(item != null){
                Gui temp = EcoGuiFactory.createSubGui(GuiInvRow.ROW6, cat.getName(), cat.getDisplayName(),  self.shop, cat.getDescriptions(), item, cat.getDamage(), cat.getDisplayName());

                for(int i = 0; i < cat.getItems().size(); i++){

                    Material ecoitemtype = Material.getMaterial(cat.getItems().get(i).getId_item());
                    if(ecoitemtype != null){
                        temp.addItem(new EcoItem(temp, ecoitemtype, 1, cat.getItems().get(i).getDurability(), cat.getItems().get(i).getBuy_price(),  cat.getItems().get(i).getSell_price(), ItemStatut.BOTH));
                        temp.addItem(null);
                    }else{
                        System.err.println("[AdminShop] Erreur ajout item (id incorrect) : " + cat.getItems().get(i).getId_item());
                    }
                }

                this.categories.add(temp);
            }else{
                System.err.println("[AdminShop] Erreur ajout categorie (item) : " + cat.getName());
            }
        });
    }

    /**
     * Setup economy
     * @return true if success
     */
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

    /**
     * @return return config file
     */
    public ConfigFile getConf() {
        return config;
    }

    /**
     * @return economy
     * @see Economy
     */
    public static Economy getEcon() {
        return econ;
    }

    /**
     * @return message file
     */
    public Message getMessage() {
        return message;
    }

    /**
     * @param player test player
     * @return if player is in editormode
     */
    public static Boolean playerIsEditorMode(Player player){
        return playerInEditionMode.contains(player.getUniqueId());
    }

    /**
     * @param player add player in editor
     */
    public static void setPlayerEditionMode(Player player){
        playerInEditionMode.add(player.getUniqueId());
    }

    /**
     * @param player remove player of editor
     */
    public static void removePlayerEditionMode(Player player){
        playerInEditionMode.remove(player.getUniqueId());
    }
}
