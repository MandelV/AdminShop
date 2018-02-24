package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author MandelV, Hougo13
 * @version 0.1
 * Représente un item dans le GUI
 * L'item se voit ajouté une action.
 */
public class GuiItem {
    // Item fixed property
    private Material type;
    private short damage;
    private String displayName;
    // Item default values
    private int defaultAmount;
    private List<String> defaultDescription = new ArrayList<>();
    // Player specific values
    private Map<UUID, Integer> playerAmounts = new HashMap<>();
    private Map<UUID, List<String>> playerDescriptions = new HashMap<>();
    private ItemMeta meta;
    private GuiAction guiAction;
    private boolean oneByPlayer = false;

    /**
     * @param type type of item
     * @param defaultAmount amount of this item in inventory
     * @param damage damage value
     * @param guiAction action when item is clicked
     */
    public GuiItem(Material type, int defaultAmount, short damage, ItemMeta meta, GuiAction guiAction){
        this.type = type;
        this.defaultAmount = defaultAmount;
        this.damage = damage;
        this.guiAction = guiAction;
        this.meta = meta;
    }

    public GuiItem(Material type, int defaultAmount, short damage, ItemMeta meta, boolean oneByPlayer, GuiAction guiAction){
        this.type = type;
        this.defaultAmount = defaultAmount;
        this.damage = damage;
        this.oneByPlayer = oneByPlayer;
        this.guiAction = guiAction;
        this.meta = meta;
    }

    public ItemMeta getMeta(){
        return this.meta;
    }

    /**
     * @param guiAction add a new action
     * @see GuiAction
     */
    public void setGuiAction(GuiAction guiAction) {
        this.guiAction = guiAction;
    }

    /**
     * @return return the type of this item
     * @see Material
     */
    public Material getType() {
        return this.type;
    }

    /**
     * @return return durability
     */
    public short getDamage() {
        return this.damage;
    }

    /**
     * @return return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return return default amount
     */
    public int getDefaultAmount() {
        return this.defaultAmount;
    }

    /**
     * @param defaultAmount set default amount
     */
    public void setDefaultAmount(int defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    /**
     * @return the default description
     */
    public List<String> getDefaultDescription() {
        return this.defaultDescription;
    }

    /**
     * @param defaultDescription set default description
     */
    public void setDefaultDescription(List<String> defaultDescription) {
        this.defaultDescription = defaultDescription;
    }

    /**
     * @param uuid uuid of player
     * @return return amount of item by player
     * @see UUID
     */
    public int getPlayerAmount(UUID uuid) {
        if (this.oneByPlayer) {
            Integer amount = this.playerAmounts.get(uuid);
            if (amount != null) {
                return amount;
            }
        }
        return this.defaultAmount;
    }

    /**
     * @param player player which one you want change amount
     * @param amount new amount
     */
    public void setPlayerAmount(Player player, int amount) {
        if (this.oneByPlayer) {
            this.playerAmounts.put(player.getUniqueId(), amount);
        }
    }

    /**
     * @param uuid UUID of player
     * @return list by uuid player
     */
    public List<String> getPlayerDescription(UUID uuid) {
        if (this.oneByPlayer) {
            List<String> description = this.playerDescriptions.get(uuid);
            if (description != null && !description.isEmpty()) {
                return description;
            }
        }
        return this.defaultDescription;
    }

    /**
     * @param player the player who will have the description
     * @param description Add description per player
     */
    public void setPlayerDescription(Player player, List<String> description) {
        if (this.oneByPlayer) {
            this.playerDescriptions.put(player.getUniqueId(), description);
        }
    }

    /**
     * @param player Who triggered the action?
     * @param clickType Event triggered
     * @return true to refresh render
     */
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
        this.displayName = ChatFormatting.formatText(displayName);
    }

    /**
     * Ajoute une description à l'item
     * @param newRow ligne de la description.
     */
    public void addRowToDefaultDescription(final String newRow){
        this.defaultDescription.add(ChatFormatting.formatText(newRow));
    }

    /**
     * @param player Add a row to the player's description
     * @param newRow the new row
     */
    public void addRowToDescription(Player player, final String newRow){
        List<String> description = this.playerDescriptions.get(player.getUniqueId());

        if (description == null) {
            description = new ArrayList<>();
        }
        description.add(ChatFormatting.formatText(newRow));
        this.setPlayerDescription(player, description);
    }

    /**
     * @param player remove player of instance gui.
     */
    public void removePlayer(Player player) {
        this.playerDescriptions.remove(player.getUniqueId());
        this.playerAmounts.remove(player.getUniqueId());
    }
}
