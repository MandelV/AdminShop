package com.github.MandelV.AdminShop.GUI;

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

    public int getSize(){
        return size;
    }
}
