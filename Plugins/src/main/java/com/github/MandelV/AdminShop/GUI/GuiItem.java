package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akitoshi
 * @version 0.1
 * Représente un item dans le GUI
 * L'item se voit ajouté une action.
 */
public class GuiItem extends ItemStack{

    private ItemMeta dataItem;
    private List<String> description;

    private GuiAction guiAction;

    public GuiItem(Material type, int amount, short damage, GuiAction guiAction){

        super(type, amount, damage);

        this.description = new ArrayList<>();
        this.guiAction = guiAction;

        this.setItemMeta(this.dataItem);
    }

    public void triggerAction(HumanEntity player, ClickType clickType) {
        if (clickType == ClickType.LEFT) {
            this.guiAction.onLeftClick(player);
        } else if (clickType == ClickType.RIGHT) {
            this.guiAction.onRightClick(player);
        }
    }


    /**
     * Ajoute un nom à l'item
     * @param displayName Nom a afficher
     */
    public void setName(final String displayName){
        this.dataItem.setDisplayName(displayName);
    }

    /**
     * Ajoute une description à l'item
     * @param description ligne de la description.
     */
    public void addLineDescription(final String description){
        this.description.add(ChatFormatting.formatText(description));
        this.dataItem.setLore(this.description);
    }
}
