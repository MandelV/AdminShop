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
import com.github.MandelV.AdminShop.GUI.GuiItem;
import com.github.MandelV.AdminShop.GUI.GuiManager;
import com.github.MandelV.AdminShop.config.ConfigFile;
import com.github.MandelV.AdminShop.config.Message;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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
        this.shop = new Gui(GuiInvRow.ROW3, "adminshop",  "&4AdminShop &f- &eCategories");
        this.categories = new ArrayList<>();
        playerInEditionMode =  new ArrayList<>();
        AdminShop self = this;

        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &5By Akitoshi and Hougo13"));
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &5Version : 1.0-SNAPSHOT"));
        this.getServer().getConsoleSender().sendMessage("");
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &dconfig.yml"));


        if(!this.getServer().getVersion().contains("1.12.2")){
            getServer().getPluginManager().disablePlugin(this);
        }
        //INITIALIZATION CONFIG FILES
        this.config = new ConfigFile(this);
        config.reloadCustomConfig();

        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &dmessage.yml"));
        this.message = new Message(this);
        message.reloadCustomConfig();

        //INITIALIZATION DAO
        this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &bBase de donnée"));
        if(Dao.getInstance(
                this.config.getCustomConfig().getString("Database.address"),
                this.config.getCustomConfig().getInt("Database.port"),
                this.config.getCustomConfig().getString("Database.name"),
                this.config.getCustomConfig().getString("Database.username"),
                this.config.getCustomConfig().getString("Database.password"),
                this.config.getCustomConfig().getBoolean("Database.useSSL")
        ).getBdd_connection() == null){
            this.getServer().getConsoleSender().sendMessage(ChatFormatting.formatText("&f[ &6AdminShop &f] &aInitialisation : &bBase de données &f: &4ERREUR !"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //INTIALISATION GUI
        getServer().getPluginManager().registerEvents(new GuiManager(), this);
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
    public synchronized void initAdminShopGui(){

        AdminShop self = this;
        List<GuiItem> items = new ArrayList<>();
        items.add(null);
        items.add(null);
        items.add(null);
        GuiItem iteminfo = new GuiItem(Material.REDSTONE_TORCH_ON, 1, (short)0, null, null );
        iteminfo.setName("" + ChatFormatting.formatText("&eplugin created by :"));
        List<String> copyright = new ArrayList<>();
        copyright.add(ChatFormatting.formatText("&4Akitoshi"));
        copyright.add(ChatFormatting.formatText("&4Hougo13"));

        iteminfo.setDefaultDescription(copyright);

        items.add(iteminfo);
        items.add(null);
        items.add(null);
        items.add(null);
        this.shop.setCustomNavbar(items);
        List<Dao_Categorie> DAOcategories = Request.getCategories();
        DAOcategories.forEach(cat -> {
            Material item = Material.getMaterial(cat.getId_item());
            if(item != null){
                Gui temp = EcoGuiFactory.createSubGui(GuiInvRow.ROW6, cat.getName(), cat.getDisplayName(),  self.shop, cat.getDescriptions(), item, cat.getDamage(), cat.getDisplayName());

                for(int i = 0; i < cat.getItems().size(); i++){
                    Material ecoitemtype = Material.getMaterial(cat.getItems().get(i).getId_item());
                    try{
                        ItemStack item_serial = AdminShop.itemDeserialization(cat.getItems().get(i).getItem_serial());
                        if(ecoitemtype != null){
                            temp.addItem(new EcoItem(temp, ecoitemtype, 1, cat.getItems().get(i).getDurability(),
                                    item_serial.getItemMeta(), cat.getItems().get(i).getBuy_price(),  cat.getItems().get(i).getSell_price(),
                                    ItemStatut.getStatut(cat.getItems().get(i).getStatut())));

                            temp.addItem(null);
                        }else{
                            System.err.println("[AdminShop] Erreur ajout item (id incorrect) : " + cat.getItems().get(i).getId_item());
                        }

                    }catch (Exception e){
                        System.out.println("" + e);
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

    /**
     *
     * @param itemStack
     * @return Serialize item
     *
     */
    public static String itemSerialization(ItemStack itemStack)  throws IllegalStateException{

        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());

        }catch (Exception e){
            throw new IllegalStateException("itemSerialization() - Unable to serialize itemstack : ", e);
        }
    }
    public static ItemStack itemDeserialization(String base64Item) throws IOException {
        try{
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64Item));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack retriveItemStack =  (ItemStack)dataInput.readObject();
            dataInput.close();
            return retriveItemStack;

        }catch (Exception e){
            throw new IOException("ItemDeserialization() - Unable to retrive itemstack : ", e);
        }
    }
}
