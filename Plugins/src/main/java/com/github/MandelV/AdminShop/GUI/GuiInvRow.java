package com.github.MandelV.AdminShop.GUI;

/**
 * @author Akitoshi
 * @version 0.1
 * Enumeration représentant chaque ligne possible d'un inventaire
 */
public enum GuiInvRow {
    ROW1( 9 ),
    ROW2( 18),
    ROW3( 27),
    ROW4( 36),
    ROW5( 45),
    ROW6( 54);

    private int size;

    GuiInvRow(int size) {
        this.size = size;
    }

    /**
     * @return le nombre de slot total du coffre.
     */
    public int getSize(){
        return size;
    }
}
