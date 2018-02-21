package Dao;


import javax.lang.model.type.NullType;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Request {

    public static ResultSet result = null;
    public static Date currentTime = new Date();



    public static final List<Dao_Categorie> getCategories(){


        List<Dao_Categorie> categories = new ArrayList<>();

        result = Dao.getInstance().query("SELECT * FROM as_categorie");

        //GET CATEGORIE
        try {
            while(result.next()){

                categories.add(new Dao_Categorie(result.getString(1),result.getString(2), result.getString(3)));

            }
            result.close();

        }catch (SQLException e){
            System.err.println("[GET CATEGORIE]");
            e.printStackTrace();
        }
        return categories;
    }

    public static void addCategorie(Dao_Categorie categorie) throws NullPointerException{
        if(categorie != null){


            ArrayList<Parameters> parameters = new ArrayList<>();

            Parameters<String> catname = new Parameters<>(categorie.getName());
            Parameters<String> catdisplayName = new Parameters<>(categorie.getDisplayName());
            Parameters<String> id_item = new Parameters<String>(categorie.getId_item());

            parameters.add(catname);
            parameters.add(catdisplayName);
            parameters.add(id_item);



            Dao.getInstance().createStatement("INSERT INTO as_categorie VALUE (?,?,?)",parameters).ifPresent(Dao::executeStatement);









        }else{
            throw new NullPointerException();
        }
    }
}


