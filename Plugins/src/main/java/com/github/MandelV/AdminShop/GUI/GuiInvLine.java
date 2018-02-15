package com.github.MandelV.AdminShop.GUI;

/**
 * @author Akitoshi
 * @version 0.1
 * Enumeration représentant chaque ligne possible d'un inventaire
 */
public enum GuiInvLine {
    LINE1( 9 ),
    LINE2( 18),
    LINE3( 27),
    LINE4( 36),
    LINE5( 45),
    LINE6( 54),;

    private int size;

    GuiInvLine(int size) {
        this.size = size;
    }

    /**
     *
     * @return le nombre de slot total du coffre.
     */
    public int getSize(){
        return size;
    }
}
