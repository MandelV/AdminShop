package com.github.MandelV.AdminShop.GUI;

public abstract class GuiFactory {

    public Gui createGUI(final GuiInvLine nbrLine, final String name){

        return new Gui(nbrLine, name);

    }
}
