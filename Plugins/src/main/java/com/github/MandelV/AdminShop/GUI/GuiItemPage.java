package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Akitoshi & Hougo13
 * @version 0.1
 * Virtual page that contains GuiItem
 */
public class GuiItemPage {

    List<GuiItem> itemList;
    int sizemax;

    /**
     * @param sizeMax size of this page
     */
    public GuiItemPage(final int sizeMax){

        this.sizemax = sizeMax;
        this.itemList = new ArrayList<>();
    }

    /**
     * @return the list of items
     */
    public List<GuiItem> getPage() {
        return itemList;
    }

    /**
     * @param item add item
     * @see GuiItem
     */
    public void addItem(GuiItem item){
        if(this.availableSlots() > 0){
            int nextAvailableIndex = this.getNextAvailableIndex();
            if (nextAvailableIndex > -1) {
                this.itemList.set(nextAvailableIndex, item);
            } else  {
                this.itemList.add(item);
            }
        }
    }

    /**
     * @return The next available index
     */
    private int getNextAvailableIndex() {
        for (int i=0; i < this.itemList.size(); i++) {
            if (this.itemList.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param id return GuiItem by its id
     * @return GuiItem
     * @see GuiItem
     */
    public GuiItem getGuiItem(int id) {
        return this.itemList.get(id);
    }

    /**
     * @return number of availableSlots
     */
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

    /**
     * @param item remove item
     * @return true if item is removed
     * @see GuiItem
     */
    public boolean removeItem(GuiItem item){
        int index = this.itemList.indexOf(item);

        if (index == -1) {
            return false;
        } else {
            this.itemList.set(index, null);
            return true;
        }
    }

    /**
     * Clear this page
     */
    public void clearPage(){
        this.itemList.clear();
    }
}
