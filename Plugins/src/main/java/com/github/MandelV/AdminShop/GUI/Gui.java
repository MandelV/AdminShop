package com.github.MandelV.AdminShop.GUI;


import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * @author Akitoshi
 * @version 0.1
 * Class principale de gestion des GUIs
 **/
public  class Gui {

    private UUID uuid;
    private List<GuiItemPage> itemPages;
    private List<GuiItem> customNavbar;

    Map<Player, Integer> currentPlayersPage;
    Map<Player, Boolean> playerChangingPage;

    private GuiInvRow nbrLine;
    private String name;
    private boolean navbar = true;



    /***
     *
     * @param nbrLine Enum definnissant le nombre de ligne dans le coffre de 1 à 6
     * @param invName Nom de l'inventaire
     * @see GuiInvRow
     */
    public Gui(GuiInvRow nbrLine, String invName){

        this.itemPages = new ArrayList<>();
        this.customNavbar = new ArrayList<>();
        for (int i=0; i < 7; i++) {
            this.customNavbar.add(null);
        }

        this.playerChangingPage = new HashMap<>();
        this.currentPlayersPage = new HashMap<>();

        this.uuid = UUID.randomUUID();
        this.nbrLine = nbrLine;
        this.name = invName;

        GuiManager.addGui(this);
    }

    public void enableNavbar(boolean b) {
        this.navbar = b;
    }

    /**
     *
     * @return l'UUID du GUI (identifiant Unique)
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
    public void addItem(GuiItem item) {

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
    }

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
            page.getPage().add(new GuiItem(Material.PAPER, 1, (short) 0, new GuiAction() {
                @Override
                public void onRightClick(Player player) {

                }

                @Override
                public void onLeftClick(Player player) {
                    self.pageDown(player);
                }
            }));

            // Set next button on previous page
            this.itemPages.get(pageIndex-1).getPage().add(new GuiItem(Material.PAPER, 1, (short) 0, new GuiAction() {
                @Override
                public void onRightClick(Player player) {

                }

                @Override
                public void onLeftClick(Player player) {
                    self.pageUp(player);
                }
            }));
        } else {
            page.getPage().add(null);
        }

        page.getPage().addAll(this.customNavbar);
    }

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
    public void pageUp(Player player){

        int pageId = this.currentPlayersPage.get(player);

        if(pageId < (this.itemPages.size()-1)){
            this.currentPlayersPage.put(player, pageId + 1);
            this.render(player);
        }

    }

    /**
     * Permet d'aller à la page précédente
     */
    public void pageDown(Player player){

        int pageId = this.currentPlayersPage.get(player);

        if(pageId > 0){
            this.currentPlayersPage.put(player, pageId - 1);
            this.render(player);
        }

    }

    /**
     *
     * @return Retourne le nombre de page
     */
    public int getnbrPage(){
        return this.itemPages.size();
    }

    /**
     *
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
        this.currentPlayersPage.put(player,0);
        if(!this.currentPlayersPage.isEmpty()){
            this.render(player, isStart);
        }

    }

    public Inventory render(Player player) {
        return this.render(player, false);
    }

    public Inventory render(Player player, boolean start) {

        Inventory inventory = Bukkit.createInventory(player, this.nbrLine.getSize(), ChatFormatting.formatText(this.name));

        if(this.itemPages.isEmpty()){
            player.sendMessage(ChatFormatting.formatText(AdminShop.getInstance().getMessage().getCustomConfig().getString("prefix") + AdminShop.getInstance().getMessage().getCustomConfig().getString("no_categories")));
        }else{
            List<GuiItem> pageContent = this.itemPages.get(this.currentPlayersPage.get(player)).getPage();


            for(int i = 0; i < pageContent.size(); i++){
                GuiItem item = pageContent.get(i);
                if (item != null) {
                    inventory.setItem(i, pageContent.get(i));
                }
            }
        }

        if (start) {
            this.playerChangingPage.put(player, false);
        } else {
            this.playerChangingPage.put(player, true);
        }

        player.openInventory(inventory);

        return inventory;
    }

    public boolean hasPlayer(Player player) {
        return this.currentPlayersPage.get(player) != null;
    }

    public void dispatchEvent(Player player, InventoryClickEvent event) {

        int slotId = event.getRawSlot();

        int pageId = this.currentPlayersPage.get(player);


        if(!this.itemPages.isEmpty()){
            GuiItemPage page = this.itemPages.get(pageId);


            if(slotId < page.getPage().size()){
                GuiItem item = page.getGuiItem(slotId);
                if(item != null){
                    item.triggerAction(player, event.getClick());
                }
            }
        }
    }

    public void exit(Player player) {
        if (this.playerChangingPage.get(player)) {
            this.playerChangingPage.put(player, false);
        } else {
            this.playerChangingPage.remove(player);
            this.currentPlayersPage.remove(player);
            System.out.println("Exit");
        }
    }

}


