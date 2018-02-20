package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Akitoshi
 * @version 0.1
 * Représente un item dans le GUI
 * L'item se voit ajouté une action.
 */
public class GuiItem extends ItemStack{

    private ItemMeta dataItem;
    private List<String> description;
    private boolean uniqueAmount;
    private Map<Player, Integer> playerAmount = new HashMap<>();

    private GuiAction guiAction;

    /**
     *
     * @param type type of item
     * @param amount amount of this item in inventory
     * @param damage damage value
     * @param guiAction action when item is clicked
     */
    public GuiItem(Material type, int amount, boolean uniqueAmount, short damage, GuiAction guiAction){
        super(type, amount, damage);

        this.uniqueAmount = uniqueAmount;
        this.description = new ArrayList<>();
        this.guiAction = guiAction;
        this.dataItem = this.getItemMeta();
    }

    public int getAmount(Player player) {
        if (this.uniqueAmount) {
            return this.getAmount();
        } else {
            Integer amount = this.playerAmount.get(player);

            if (amount != null) {
                return amount;
            } else {
                return this.getAmount();
            }
        }
    }

    public void setAmount(Player player, int amount) {
        this.playerAmount.put(player, amount);
    }

    public void setGuiAction(GuiAction guiAction) {
        this.guiAction = guiAction;
    }

    public boolean triggerAction(Player player, ClickType clickType) {

        if(this.guiAction != null){
            if (clickType == ClickType.LEFT) {

                return this.guiAction.onLeftClick(player);

            } else if (clickType == ClickType.RIGHT) {

               return this.guiAction.onRightClick(player);

            }else if(clickType == ClickType.MIDDLE){

                return this.guiAction.onMiddleClick(player);

            }else if(clickType == ClickType.SHIFT_LEFT){

                return this.guiAction.onShiftLeftClick(player);

            }else if(clickType == ClickType.SHIFT_RIGHT){

                return  this.guiAction.onShiftRightClick(player);



            }
        }

        return false;
    }


    /**
     * Ajoute un nom à l'item
     * @param displayName Nom a afficher
     */
    public void setName(String displayName){
        this.dataItem.setDisplayName(ChatFormatting.formatText(displayName));
        this.setItemMeta(this.dataItem);
    }

    /**
     * Ajoute une description à l'item
     * @param description ligne de la description.
     */
    public void addLineDescription(final String description){
        this.description.add(ChatFormatting.formatText(description));
        this.dataItem.setLore(this.description);
        this.setItemMeta(this.dataItem);
    }
}
