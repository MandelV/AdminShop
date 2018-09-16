package Dao;

/**
 * @author MandelV
 * Represente Item in dataBase
 */
public class DaoItem {

    private String id_item;
    private String item_serial;
    private Double buy_price;
    private Double sell_price;
    private Short durability;
    private String statut;


    /**
     * @param id_item id of item
     * @param durability Its durability
     * @param buy_price Its buy price
     * @param sell_price Its sell price
     * @param statut Its statut
     */
    public DaoItem(final String id_item, final Short durability, String item_serial, final double buy_price, final double sell_price, final String statut){

        this.id_item = id_item;
        this.durability = durability;
        this.buy_price = buy_price;
        this.sell_price = sell_price;
        this.statut = statut;
        this.item_serial = item_serial;
    }

    public String getItem_serial() {
        return item_serial;
    }

    /**
     * @return Its sell price
     */
    public Double getSell_price() {
        return sell_price;
    }
    /**
     * @return Its buy price
     */
    public Double getBuy_price() {
        return buy_price;
    }
    /**
     * @return Its durability
     */
    public Short getDurability() {
        return durability;
    }
    /**
     * @return Its id
     */
    public String getId_item() {
        return id_item;
    }
    /**
     * @return Its statut
     */
    public String getStatut() {
        return statut;
    }
}
