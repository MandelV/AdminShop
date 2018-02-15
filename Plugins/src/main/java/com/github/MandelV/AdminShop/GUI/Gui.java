package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public  class Gui{

    private UUID uuid;
    private Inventory inv;
    private List<GuiItemPage> itemPage;
    private int currentPage;


    public Gui(GuiInvLine nbrLine, String invName){



        this.itemPage = new ArrayList<>();
        this.itemPage.add(new GuiItemPage(nbrLine.getSize()));
        this.currentPage = 0;

        this.uuid = UUID.randomUUID();

        this.inv = Bukkit.createInventory(null, nbrLine.getSize(), ChatFormatting.formatText(invName));
    }

    public Inventory getYourInventory() {
        return this.inv;
    }

    public String getName(){
        return this.inv.getName();
    }

    public UUID getUuid() {
        return this.uuid;
    }


    public void addItem(GuiItem item) {

        //On regarde les emplacements disponnible dans les pages

        int availablePage = -1;//Numéro de la page disponible.
        for(int i = 0; i < this.itemPage.size(); i++){
            if(this.itemPage.get(i).getPage().size() < this.inv.getSize()){
                availablePage = i;
                break;
            }
        }

        //Si aucune page n'est disponible
        if(availablePage == -1){

            GuiItemPage newPage = new GuiItemPage(this.inv.getSize());
            newPage.addItem(item);
            this.itemPage.add(newPage);


        }else {//Si une page dispose d'un emplacement.
            this.itemPage.get(availablePage).addItem(item);
        }
    }

    private void fillInventory(){


        this.inv.clear();
        for(int i = 0; i < this.itemPage.get(this.currentPage).getPage().size(); i++){

            this.inv.addItem(this.itemPage.get(this.currentPage).getPage().get(i));
        }

    }
    public void pageUp(){

        if(this.currentPage < (this.itemPage.size()-1)){
            this.currentPage++;
        }
    }

    public void pageDown(){

        if(this.currentPage > 0){
            this.currentPage--;
        }
    }

    public int getnbrPage(){
        return this.itemPage.size();
    }
    public int getCurrentPage(){
        return this.currentPage;
    }
    public void selectPage(final int page){
        this.currentPage = page;
    }

    public void open(Player player){


        this.fillInventory();
        player.openInventory(this.inv);
    }
}


