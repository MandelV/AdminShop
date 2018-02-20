package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.Player;

public interface GuiAction {

    boolean onRightClick(Player player);

    boolean onLeftClick(Player player);
}
