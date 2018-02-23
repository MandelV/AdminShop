package Dao;




import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MandelV
 * @version 0.2
 * This class allow to make different request to database.
 */
public abstract class Request {

    public static ResultSet result = null;

    /**
     * @return a list of categorie found in database
     * @see Dao_Categorie
     */
    public static final List<Dao_Categorie> getCategories() {

        List<Dao_Categorie> categories = new ArrayList<>();

        result = Dao.getInstance().query("SELECT * FROM as_categorie");

        //GET CATEGORIE
        try {
            while (result.next()) {

                categories.add(new Dao_Categorie(result.getString(1), result.getString(2), result.getString(3), result.getShort(4)));

            }
            result.close();

        } catch (SQLException e) {
            System.err.println("[GET CATEGORIE]");
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * @param categorie add categorie in database
     * @see Dao_Categorie
     */
    public static void addCategorie(Dao_Categorie categorie) {
        if (categorie != null) {


            ArrayList<Parameters> parameters = new ArrayList<>();

            Parameters<String> catname = new Parameters<>(categorie.getName());
            Parameters<String> catdisplayName = new Parameters<>(categorie.getDisplayName());
            Parameters<String> id_item = new Parameters<>(categorie.getId_item());
            Parameters<Short> durability = new Parameters<Short>(categorie.getDamage());


            parameters.add(catname);
            parameters.add(catdisplayName);
            parameters.add(id_item);
            parameters.add(durability);


            Dao.getInstance().createStatement("INSERT INTO as_categorie VALUE (?,?,?,?)", parameters).ifPresent(Dao::executeStatement);

        }
    }

    /**
     * @param name remove categorie from database
     */
    public static void removeCategorie(String name) {
        if (name != null) {

            ArrayList<Parameters> parameters = new ArrayList<>();
            Parameters<String> pname = new Parameters<String>(name);
            parameters.add(pname);

            Dao.getInstance().createStatement("DELETE FROM as_categorie WHERE name=?", parameters).ifPresent(Dao::executeStatement);
        }
    }

    /**
     * @param categorie name of categorie where you want add the new item
     * @param item      item which you want add
     * @see Dao_item
     */
    public static void addItemIntoCategorie(String categorie, Dao_item item) {

        ArrayList<Parameters> parameters = new ArrayList<>();
        Parameters<String> nameCategorie = new Parameters<>(categorie);
        Parameters<String> id_item = new Parameters<>(item.getId_item());
        Parameters<Short> damage = new Parameters<>(item.getDurability());
        Parameters<Double> buy_price = new Parameters<>(item.getBuy_price());
        Parameters<Double> sell_price = new Parameters<>(item.getSell_price());
        Parameters<String> type_vente = new Parameters<>(item.getStatut());
        parameters.add(nameCategorie);
        parameters.add(id_item);
        parameters.add(damage);
        parameters.add(buy_price);
        parameters.add(sell_price);
        parameters.add(type_vente);

        Dao.getInstance().createStatement("INSERT INTO as_item(name_categorie, id_item, damage, prix_achat, prix_vente, type_vente) VALUE (?,?,?,?,?,?)", parameters).ifPresent(Dao::executeStatement);
    }

    /**
     * @param categorie name of categorie which contain the item
     * @param item      item which you want remove
     */
    public static void removeItemFromCategorie(String categorie, Dao_item item) {

        ArrayList<Parameters> parameters = new ArrayList<>();

        Parameters<String> nameCategorie = new Parameters<>(categorie);
        Parameters<String> id_item = new Parameters<>(item.getId_item());
        Parameters<Short> damage = new Parameters<>(item.getDurability());

        parameters.add(nameCategorie);
        parameters.add(id_item);
        parameters.add(damage);
        Dao.getInstance().createStatement("DELETE FROM as_item WHERE name_categorie=? AND id_item=? AND damage=?", parameters).ifPresent(Dao::executeStatement);
    }

    public static void addEntryHistory(String pseudoPlayer, String material, int amount, String statut) {

        ArrayList<Parameters> parameters = new ArrayList<>();

        Parameters<String> pseudo = new Parameters<>(pseudoPlayer);
        Parameters<String> id_item = new Parameters<>(material);
        Parameters<Integer> quantity = new Parameters<>(amount);
        Parameters<String> statutitem = new Parameters<>(statut);


        parameters.add(pseudo);
        parameters.add(id_item);
        parameters.add(quantity);
        parameters.add(statutitem);

        Dao.getInstance().createStatement("INSERT INTO as_history(pseudo_joueur, id_item, quantity, action, date) VALUE (?,?,?,?, NOW() )", parameters).ifPresent(Dao::executeStatement);
    }

    public static Double getTotalEco(String action) {

        Double total = null;
        result = Dao.getInstance().query("SELECT SUM(total_per) FROM (SELECT (SUM(prix_achat) * quantity) as total_per FROM as_item, (SELECT id_item, SUM(quantity) as quantity FROM as_history WHERE action = '" + action + "' GROUP BY as_history.id_item) as hist WHERE as_item.id_item = hist.id_item GROUP BY hist.id_item) as hist_per");
        try {
            if (result.next()) {
                total = result.getDouble(1);
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}



