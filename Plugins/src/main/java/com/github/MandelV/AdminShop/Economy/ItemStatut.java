package com.github.MandelV.AdminShop.Economy;

/**
 * @author MandelV
 * @version 1.0
 * Represent the Economic statut of an item.
 */
public enum ItemStatut {
    SELL("SELL"),
    BUY("BUY"),
    BOTH("BOTH");

    private String  statut;

    ItemStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @return the name of Enum's elements
     */
    public String getName(){
        return statut;
    }

    /**
     * get ItemStatut by String
     * @param name name of statut
     * @return ItemStatut
     */
    public static ItemStatut getStatut(String name){
        name = name.toUpperCase();
        for (ItemStatut statut : ItemStatut.values()) {
            if(statut.getName().equalsIgnoreCase(name)){
                return ItemStatut.valueOf(name);
            }
        }
        return null;
    }
}
