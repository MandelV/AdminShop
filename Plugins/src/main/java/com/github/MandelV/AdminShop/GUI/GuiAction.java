package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.HumanEntity;

public interface GuiAction {

    void onRightClick(HumanEntity player);

    void onLeftClick(HumanEntity player);
}
