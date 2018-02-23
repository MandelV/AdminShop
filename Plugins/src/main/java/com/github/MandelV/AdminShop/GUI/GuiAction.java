package com.github.MandelV.AdminShop.GUI;

import org.bukkit.entity.Player;

/**
 * @author Mandel & Hugo
 * Interface des events permettant d'interagir avec les GUIs
 */

public interface GuiAction {

    boolean onRightClick(Player player);

    boolean onLeftClick(Player player);

    boolean onMiddleClick(Player player);

    boolean onShiftLeftClick(Player player);

    boolean onShiftRightClick(Player player);
}
