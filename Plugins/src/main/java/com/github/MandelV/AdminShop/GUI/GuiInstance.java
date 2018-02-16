package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.Player;

public class GuiInstance {

    private Player player;
    private int pageId = 0;

    GuiInstance(Player player) {
        this.player = player;
    }

    GuiInstance(Player player, int pageId) {
        this.player = player;
        this.pageId = pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPageId() {
        return pageId;
    }

    boolean isPlayer(Player player) {
        return player == this.player;
    }

    public Player getPlayer() {
        return player;
    }
}
