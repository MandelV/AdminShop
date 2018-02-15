package com.github.MandelV.AdminShop.GUI;

public abstract class GuiFactory {

    public Gui createGUI(final int invSize, final String name){

        return new Gui(invSize, name);

    }
}
