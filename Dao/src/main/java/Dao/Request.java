package Dao;


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
}


