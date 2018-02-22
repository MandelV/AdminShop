package com.github.MandelV.AdminShop.GUI;

import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import java.util.*;

/**
 * @author Akitoshi
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

    private GuiAction guiAction;
    private boolean oneByPlayer = false;


    /**
     *
     * @param type type of item
     * @param defaultAmount amount of this item in inventory
     * @param damage damage value
     * @param guiAction action when item is clicked
     */
    public GuiItem(Material type, int defaultAmount, short damage, GuiAction guiAction){
        this.type = type;
        this.defaultAmount = defaultAmount;
        this.damage = damage;
        this.guiAction = guiAction;

    }

    public GuiItem(Material type, int defaultAmount, short damage, boolean oneByPlayer, GuiAction guiAction){
        this.type = type;
        this.defaultAmount = defaultAmount;
        this.damage = damage;
        this.oneByPlayer = oneByPlayer;
        this.guiAction = guiAction;
    }

    protected void setGuiAction(GuiAction guiAction) {
        this.guiAction = guiAction;
    }

    public Material getType() {
        return this.type;
    }

    public short getDamage() {
        return this.damage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDefaultAmount() {
        return this.defaultAmount;
    }

    public void setDefaultAmount(int defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public List<String> getDefaultDescription() {
        return this.defaultDescription;
    }

    public void setDefaultDescription(List<String> defaultDescription) {
        this.defaultDescription = defaultDescription;
    }

    public int getPlayerAmount(UUID uuid) {
        if (this.oneByPlayer) {
            Integer amount = this.playerAmounts.get(uuid);
            if (amount != null) {
                return amount;
            }
        }

        return this.defaultAmount;
    }

    public void setPlayerAmount(Player player, int amount) {
        if (this.oneByPlayer) {
            this.playerAmounts.put(player.getUniqueId(), amount);
        }
    }

    public List<String> getPlayerDescription(UUID uuid) {
        if (this.oneByPlayer) {
            List<String> description = this.playerDescriptions.get(uuid);
            if (description != null && !description.isEmpty()) {
                return description;
            }
        }

        return this.defaultDescription;
    }

    public void setPlayerDescription(Player player, List<String> description) {
        if (this.oneByPlayer) {
            this.playerDescriptions.put(player.getUniqueId(), description);
        }
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
        this.displayName = ChatFormatting.formatText(displayName);
    }

    /**
     * Ajoute une description à l'item
     * @param newRow ligne de la description.
     */
    public void addRowToDefaultDescription(final String newRow){
        this.defaultDescription.add(ChatFormatting.formatText(newRow));
    }

    public void addRowToDescription(Player player, final String newRow){
        List<String> description = this.playerDescriptions.get(player.getUniqueId());

        if (description == null) {
            description = new ArrayList<>();
        }

        description.add(ChatFormatting.formatText(newRow));

        this.setPlayerDescription(player, description);
    }

    public void removePlayer(Player player) {
        this.playerDescriptions.remove(player.getUniqueId());
        this.playerAmounts.remove(player.getUniqueId());
    }
}
