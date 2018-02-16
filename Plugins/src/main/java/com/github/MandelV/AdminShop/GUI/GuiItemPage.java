package com.github.MandelV.AdminShop.GUI;

import javafx.scene.paint.Material;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akitoshi
 * @version 0.1
 * Gestion  de la pagination.
 */
public class GuiItemPage {

    List<GuiItem> itemList;
    int sizemax;

    public GuiItemPage(final int sizeMax){

        this.sizemax = sizeMax;
        this.itemList = new ArrayList<>();

    }
    public List<GuiItem> getPage() {
        return itemList;
    }

    public void addItem(GuiItem item){

        if(this.itemList.size() < this.sizemax){
            this.itemList.add(item);
        }
    }

    public GuiItem getGuiItem(int id) {
        return this.itemList.get(id);
    }

    public int availableSlots() {
        int count = 0;
        for (GuiItem item: this.itemList) {
            if (item == null) {
                break;
            }
            count++;
        }

        return this.sizemax - count;
    }

    public void removeItem(GuiItem item){
        this.itemList.remove(item);
    }
    public void clearPage(){
        this.itemList.clear();
    }
}
