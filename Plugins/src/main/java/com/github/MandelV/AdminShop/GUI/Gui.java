package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Bukkit;
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
    private List<GuiItemPage> itemPage;

    Map<Player, Integer> currentPlayersPage;

    private GuiInvLine nbrLine;
    private String name;

    /***
     *
     * @param nbrLine Enum definnissant le nombre de ligne dans le coffre de 1 à 6
     * @param invName Nom de l'inventaire
     * @see GuiInvLine
     */
    public Gui(GuiInvLine nbrLine, String invName){

        this.itemPage = new ArrayList<>();
        this.itemPage.add(new GuiItemPage(nbrLine.getSize()));

        this.currentPlayersPage = new HashMap<>();

        this.uuid = UUID.randomUUID();
        this.nbrLine = nbrLine;
        this.name = invName;

        GuiManager.addGui(this);
    }

    /**
     *
     * @return l'UUID du GUI (identifiant Unique)
     */
    public UUID getUuid() {
        return this.uuid;
    }


    /**
     * Ajoute un item au GUI
     * Si une page est plein cela en créera une nouvelle
     * @param item Item à ajouté
     * @see GuiItem
     */
    public void addItem(GuiItem item) {

        //On regarde les emplacements disponnible dans les pages

        int availablePage = -1;//Numéro de la page disponible.
        for(int i = 0; i < this.itemPage.size(); i++){
            if(this.itemPage.get(i).getPage().size() < this.nbrLine.getSize()){
                availablePage = i;
                break;
            }
        }

        //Si aucune page n'est disponible
        if(availablePage == -1){

            GuiItemPage newPage = new GuiItemPage(this.nbrLine.getSize());
            newPage.addItem(item);
            this.itemPage.add(newPage);


        }else {//Si une page dispose d'un emplacement.
            this.itemPage.get(availablePage).addItem(item);
        }
    }

    /**
     * Permet d'aller à la page suivante
     */
    public void pageUp(Player player){

        int pageId = this.currentPlayersPage.get(player);

        if(pageId < (this.itemPage.size()-1)){
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
        return this.itemPage.size();
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

        this.currentPlayersPage.put(player,0);
        this.render(player);

    }

    public Inventory render(Player player) {

        Inventory inventory = Bukkit.createInventory(player, this.nbrLine.getSize(), ChatFormatting.formatText(this.name));

        List<GuiItem> pageContent = this.itemPage.get(this.currentPlayersPage.get(player)).getPage();

        for(int i = 0; i < pageContent.size(); i++){
            inventory.addItem(pageContent.get(i));
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
        GuiItemPage page = this.itemPage.get(pageId);

        GuiItem item = page.getGuiItem(slotId);

        item.triggerAction(player, event.getClick());
    }

}


