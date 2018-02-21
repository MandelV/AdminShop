package com.github.MandelV.AdminShop.Economy;

/**
 * @author Akitoshi
 * @version 0.1
 * Représente le statut economique d'un item
 * Si vendable, achetable ou les deux.
 */
public enum ItemStatut {
    SELL("SELL"), //vendable
    BUY("BUY"), //Achetable
    BOTH("BOTH"); //Les deux

    private String  statut;

    ItemStatut(String statut) {
        this.statut = statut;
    }

    /**
     *
     * @return le nombre de slot total du coffre.
     */
    public String getName(){
        return statut;
    }

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
