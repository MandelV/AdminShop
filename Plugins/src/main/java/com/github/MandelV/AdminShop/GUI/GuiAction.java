package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public interface GuiAction {

    void onRightClick(Player player);

    void onLeftClick(Player player);
}
