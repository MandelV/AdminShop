package Dao;

public class Dao_item {

    private String id_item;
    private Double buy_price;//achat
    private Double sell_price;//vente
    private String statut;


    public Dao_item(final String id_item, final double buy_price, final double sell_price, final String statut){

        this.id_item = id_item;
        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.statut = statut;
    }

    public Double getSell_price() {
        return sell_price;
    }

    public Double getBuy_price() {
        return buy_price;
    }

    public String getId_item() {
        return id_item;
    }

    public String getStatut() {
        return statut;
    }
}
