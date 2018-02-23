package Dao;

import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MandelV
 * @version 0.1
 * Represente a categorie in database.
 */
public class Dao_Categorie {

    private String name;
    private String displayName;
    private String id_item;
    private Short damage;
    private ResultSet result;
    private List<String> descriptions;
    private List<Dao_item> items;

    /**
     * @param name Categorie's name
     * @param displayName Categorie's displayName
     * @param id_item Item id
     * @param damage Item durability
     */
    public Dao_Categorie(final String name, final String displayName, final String id_item, final Short damage){

        this.name = name;
        this.displayName = displayName;
        this.id_item = id_item;
        this.damage = damage;
        this.descriptions = new ArrayList<>();
        this.items = new ArrayList<>();

        this.requestDescriptions();
        this.requestItemCategorie();


    }

    /**
     * @param desc Add a String row to description
     */
    public void addDescriptions(String desc) {
        this.descriptions.add(desc);
    }

    /**
     * @return id of item
     */
    public String getId_item() {
        return id_item;
    }

    /**
     * Get a description of this description
     */
    private void requestDescriptions() {

        this.descriptions.clear();

        this.result = Dao.getInstance().query("SELECT description " +
                "FROM as_description, as_categorie_description " +
                "WHERE as_description.id = as_categorie_description.id_description " +
                "AND as_categorie_description.nom_categorie ='" + this.name + "'");

        try {
            while (this.result.next()){

                this.descriptions.add(ChatFormatting.formatText(this.result.getString(1)));

            }
            this.result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * @return List of Dao_Item which represente each item in categorie
     * @see Dao_item
     */
    public List<Dao_item> getItems(){
        return this.items;
    }

    /**
     * Request all item which match with this categorie
     */
    private void requestItemCategorie(){

        this.result = Dao.getInstance().query("SELECT * FROM as_item WHERE as_item.name_categorie ='" + this.name + "'");

        try {

            while (this.result.next()){

                this.items.add(new Dao_item(
                        this.result.getString(3),
                        this.result.getShort(4),
                        this.result.getDouble(5),
                        this.result.getDouble(6),
                        this.result.getString(7)));
            }

            this.result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * @return The Categorie's description
     */
    public List<String> getDescriptions(){
        return this.descriptions;
    }

    /**
     * @return The name's description
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The description's DisplayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return Item's durability of this Categorie
     */
    public Short getDamage() {
        return damage;
    }
}
