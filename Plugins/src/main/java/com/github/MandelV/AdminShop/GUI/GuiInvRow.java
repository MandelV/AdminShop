package com.github.MandelV.AdminShop.GUI;

/**
 * @author MandelV
 * @version 0.1
 * Enumeration which represent each row in Gui
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
     * @return le size of the row
     */
    public int getSize(){
        return size;
    }
}
