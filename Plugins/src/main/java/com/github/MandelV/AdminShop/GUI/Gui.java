package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * @author Akitoshi
 * @version 0.1
 * Class principale de gestion des GUIs
 **/
public  class Gui {

    private UUID uuid;
    private Inventory inv;
    private List<GuiItemPage> itemPage;

    private int currentPage;

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

        this.currentPage = 0;
        this.currentPlayersPage = new HashMap<>();

        this.uuid = UUID.randomUUID();
        this.nbrLine = nbrLine;
        this.name = invName;
    }

    /****
     *
     * @return L'inventaire du GUI
     */
    public Inventory getYourInventory() {
        return this.inv;
    }

    /**
     *
     * @return Le nom du GUI
     */
    public String getName(){
        return this.inv.getName();
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
     * Permet de remplir l'inventaire du GUI avec la page actuelle
     */
    private void fillInventory(){


        this.inv.clear();
        for(int i = 0; i < this.itemPage.get(this.currentPage).getPage().size(); i++){

            this.inv.addItem((GuiItem)this.itemPage.get(this.currentPage).getPage().get(i));
        }

    }

    /**
     * Permet d'aller à la page suivante
     */
    public void pageUp(){

        if(this.currentPage < (this.itemPage.size()-1)){
            this.currentPage++;
        }
    }

    /**
     * Permet d'aller à la page précédente
     */
    public void pageDown(){

        if(this.currentPage > 0){
            this.currentPage--;
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
    public int getCurrentPage(){
        return this.currentPage;
    }

    /**
     * Permet de choisir la page voulu
     * @param page Page souhaité.
     */
    public void selectPage(final int page){
        this.currentPage = page;
    }

    /**
     * Permet à un joueur d'ouvrir le GUI
     * @param player Joueur qui ouvre le GUI
     */
    public void open(Player player){


        this.inv = Bukkit.createInventory(null, this.nbrLine.getSize(), ChatFormatting.formatText(this.name));
        this.fillInventory();
        player.openInventory(this.inv);

    }
}


