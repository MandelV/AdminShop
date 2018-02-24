package com.github.MandelV.AdminShop.GUI;


import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MandelV, Hougo13
 * @version 0.1
 * Class principale de gestion des GUIs
 **/
public  class Gui {

    private UUID uuid;
    private List<GuiItemPage> itemPages;
    private List<GuiItem> customNavbar;
    private ConcurrentHashMap<UUID, Integer> currentPlayersPage;
    private ConcurrentHashMap<UUID, Boolean> playerChangingPage;
    private GuiInvRow nbrLine;
    private String name;
    private String displayName;
    private boolean navbar = true;

    /***
     * @param nbrLine Enum definnissant le nombre de ligne dans le coffre de 1 à 6
     * @param name Nom de l'inventaire
     * @see GuiInvRow
     */
    public Gui(GuiInvRow nbrLine, String name, String displayName){

        this.itemPages = new ArrayList<>();
        this.customNavbar = new ArrayList<>();

        for (int i=0; i < 7; i++) {
            this.customNavbar.add(null);
        }

        this.playerChangingPage = new ConcurrentHashMap<>();
        this.currentPlayersPage = new ConcurrentHashMap<>();

        this.uuid = UUID.randomUUID();
        this.nbrLine = nbrLine;
        this.name = name;
        this.displayName = displayName;

        GuiManager.addGui(this);
    }

    /**
     * @return return name
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return return display name
     */
    public String getDisplayName(){
        return this.displayName;
    }

    /**
     * @param b enable or disable navBar
     */
    public void enableNavbar(boolean b) {
        this.navbar = b;
    }

    /**
     * @return l'UUID du GUI (identifiant Unique)
     * @see UUID
     */
    public UUID getUuid() {
        return this.uuid;
    }

    public void setCustomNavbar(List<GuiItem> customNavbar) {
        this.customNavbar = customNavbar;
    }

    /**
     * Ajoute un item au GUI
     * Si une page est plein cela en créera une nouvelle
     * @param item Item à ajouté
     * @see GuiItem
     */
    public void addItem(GuiItem item, boolean refresh) {

        GuiItemPage page;

        if (this.hasAvailableSlot()) {
            page = this.itemPages.get(this.itemPages.size()-1);
            page.addItem(item);
        } else {
            page = new GuiItemPage(this.nbrLine.getSize());
            this.itemPages.add(page);
            page.addItem(item);
            if (this.navbar) {
                this.addNavbar(page);
            }
        }
        if (refresh) {
            this.refreshAll();
        }
    }

    /**
     * @param item Add new item
     * @see GuiItem
     */
    public void addItem(GuiItem item) {
        this.addItem(item, true);
    }

    /**
     * @param item remove item
     * @see GuiItem
     */
    public void removeItem(GuiItem item){
        this.itemPages.forEach(page -> {
            page.removeItem(item);
        });

        int prevNbrPages = this.itemPages.size();
        List<GuiItem> items = this.getAllItems();

        this.itemPages.clear();

        for (GuiItem itm: items) {
            this.addItem(itm, false);
        }

        if (prevNbrPages > this.itemPages.size()) {
            for (UUID uuid: this.currentPlayersPage.keySet()) {
                if (this.currentPlayersPage.get(uuid) == prevNbrPages-1) {
                    int newIndex = this.itemPages.size()-1;
                    if (newIndex < 0) {
                        newIndex = 0;
                    }
                    this.currentPlayersPage.put(uuid, newIndex);
                }
            }
        }

        this.refreshAll();
    }

    /**
     * @return all items in Gui
     * @see GuiItem
     */
    public List<GuiItem> getAllItems() {
        List<GuiItem> items = new ArrayList<>();

        int iMax = this.nbrLine.getSize();

        if (this.navbar) {
            iMax -= 9;
        }

        for (GuiItemPage page: this.itemPages) {
            for (int i = 0; i < iMax; i++) {
                GuiItem item = page.getPage().get(i);
                if (item != null) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    /**
     * @param page Add NavBar
     */
    private void addNavbar(GuiItemPage page) {
        if (nbrLine.getSize() <= 9) {
            throw new Error("Cannot add navbar on small inventory");
        }
        int pageIndex = this.itemPages.indexOf(page);
        // Fill of null until the navbar
        for (int i = page.getPage().size(); i < this.nbrLine.getSize()-9; i++) {
            page.getPage().add(null);
        }
        Gui self = this;
        if (pageIndex > 0) {
            // Set previous button
            GuiItem prevButton = new GuiItem(Material.PAPER, 1, (short) 0, null, new GuiAction() {
                @Override
                public boolean onRightClick(Player player) {
                    return false;
                }
                @Override
                public boolean onLeftClick(Player player) {
                    self.pageDown(player);
                    return false;
                }

                @Override
                public boolean onMiddleClick(Player player) {
                    return false;
                }

                @Override
                public boolean onShiftLeftClick(Player player) {
                    return false;
                }

                @Override
                public boolean onShiftRightClick(Player player) {
                    return false;
                }
            });

            prevButton.setName(AdminShop.getInstance().getMessage().getCustomConfig().getString("next_page"));

            page.getPage().add(prevButton);

            // Set next button on previous page
            GuiItem nextButton = new GuiItem(Material.PAPER, 1, (short) 0, null, new GuiAction() {
                @Override
                public boolean onRightClick(Player player) {
                    return false;
                }
                @Override
                public boolean onLeftClick(Player player) {
                    self.pageUp(player);
                    return false;
                }
                @Override
                public boolean onMiddleClick(Player player) {
                    return false;
                }
                @Override
                public boolean onShiftLeftClick(Player player) {
                    return false;
                }
                @Override
                public boolean onShiftRightClick(Player player) {
                    return false;
                }
            });

            nextButton.setName(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("previous_page")));
            this.itemPages.get(pageIndex-1).getPage().add(nextButton);

        } else {
            page.getPage().add(null);
        }
        page.getPage().addAll(this.customNavbar);
    }

    /**
     * @return true if the gui has avaible slot
     */
    private boolean hasAvailableSlot() {
        boolean result = false;
        int minSlots = 0;

        if (this.navbar) {
            minSlots = 9;
        }

        for (GuiItemPage itemPage: this.itemPages) {
            if (itemPage.availableSlots() > minSlots) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Permet d'aller à la page suivante
     */
    private void pageUp(Player player){

        int pageId = this.currentPlayersPage.get(player.getUniqueId());

        if(pageId < (this.itemPages.size()-1)){
            this.currentPlayersPage.put(player.getUniqueId(), pageId + 1);
            this.render(player);
        }

    }

    /**
     * Permet d'aller à la page précédente
     */
    private void pageDown(Player player){
        int pageId = this.currentPlayersPage.get(player.getUniqueId());
        if(pageId > 0){
            this.currentPlayersPage.put(player.getUniqueId(), pageId - 1);
            this.render(player);
        }
    }

    /**
     * @return Retourne le nombre de page
     */
    public int getnbrPage(){
        return this.itemPages.size();
    }

    /**
     * @return Retourne la page actuellement affichée.
     */
    public int getCurrentPage(Player player){
        return this.currentPlayersPage.get(player);
    }

    /**
     * Permet à un joueur d'ouvrir le GUI
     * @param player Joueur qui ouvre le GUI
     */
    public void open(Player player){
        this.open(player, true);
    }

    public void open(Player player, boolean isStart){
        this.currentPlayersPage.put(player.getUniqueId(),0);
        if(!this.currentPlayersPage.isEmpty()){
            this.render(player, isStart);
            System.out.println("[AdminShop]" + player.getName() + " action : " + "OPEN");
        }
    }

    /**
     * @param player you render to player
     * @return return a new inventory will opened by player
     */
    private Inventory render(Player player) {
        return this.render(player, false);
    }
    private Inventory render(Player player, boolean start) {

        Inventory inventory = Bukkit.createInventory(player, this.nbrLine.getSize(), ChatFormatting.formatText(this.displayName));

        if(this.itemPages.isEmpty()){
            player.sendMessage(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix") + AdminShop.getInstance().getMessage().getCustomConfig().getString("no_categories")));
        }else{

            List<GuiItem> pageContent = this.itemPages.get(this.currentPlayersPage.get(player.getUniqueId())).getPage();

            for(int i = 0; i < pageContent.size(); i++){
                GuiItem item = pageContent.get(i);
                if (item != null) {
                    inventory.setItem(i, new GuiItemInstance(item, player));
                }
            }
        }
        if (start) {
            this.playerChangingPage.put(player.getUniqueId(), false);
        } else {
            this.playerChangingPage.put(player.getUniqueId(), true);
        }

        player.openInventory(inventory);

        return inventory;
    }

    /**
     * refresh
     */
    public void refreshAll() {
        for (UUID uuid: this.currentPlayersPage.keySet()) {
            Player player = AdminShop.getInstance().getServer().getPlayer(uuid);
            this.render(player);
        }
    }

    /**
     * @param player player
     * @return return true if gui has player
     */
    public boolean hasPlayer(Player player) {
        return this.currentPlayersPage.get(player.getUniqueId()) != null;
    }

    /**
     * @param player player
     * @param event dispatch event when player click
     */
    public void dispatchEvent(Player player, InventoryClickEvent event) {

        int slotId = event.getRawSlot();

        int pageId = this.currentPlayersPage.get(player.getUniqueId());


        if(!this.itemPages.isEmpty()){
            GuiItemPage page = this.itemPages.get(pageId);


            if(slotId < page.getPage().size()){
                GuiItem item = page.getGuiItem(slotId);
                if(item != null){
                    boolean refresh = item.triggerAction(player, event.getClick());


                    if(refresh){
                        this.render(player);
                    }
                }
            }
        }
    }

    /**
     * @param player exit gui
     */
    public synchronized void exit(Player player) {
        if (this.playerChangingPage.get(player.getUniqueId())) {
            this.playerChangingPage.put(player.getUniqueId(), false);
        } else {
            this.playerChangingPage.remove(player.getUniqueId());
            this.currentPlayersPage.remove(player.getUniqueId());

            for (GuiItemPage page: this.itemPages) {
                for (GuiItem item: page.getPage()) {
                    if (item != null) {
                        item.removePlayer(player);
                    }
                }
            }
            System.out.println("[AdminShop]" + player.getName() + " action : " + "EXIT");
        }
    }

    public void exitAll(){

        for (UUID uuid: this.currentPlayersPage.keySet()) {
            Player player = AdminShop.getInstance().getServer().getPlayer(uuid);
            this.exit(player);
            player.closeInventory();
        }
    }
}


