package Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Request {

    public static ResultSet result = null;
    public static Date currentTime = new Date();


    public static boolean addGrade(final String pseudo, final int grade) {


        int idJoueur = -1;

        //Get player ID
        result = Dao.getInstance().query("SELECT id FROM users WHERE pseudo='" + pseudo + "'");
        try {
            idJoueur = (result.next()) ? result.getInt("id") : -1;
            result.close();
        } catch (SQLException e) {
            return false;
        }

        if (idJoueur == -1) {
            return false;
        }
        //insert new grade.
        Dao.getInstance().update("INSERT INTO shop__items_buy_histories(item_id, user_id, created) " + "VALUES (" + String.valueOf(grade) + "," + String.valueOf(idJoueur) + ", NOW())");

        return true;

    }


    /**
     *
     * @param pseudo player pseudo
     * @return no_account, no_grade
     */
    public static Map<String, String> getGrade(final String pseudo) {


        Map<String, String> gradeInfo = new HashMap<>();
        SimpleDateFormat formatter  = new SimpleDateFormat ("dd/MM/yyyy");
        int idJoueur = -1, idItem = -1;
        Date created = null;

        result = Dao.getInstance().query("SELECT id FROM users WHERE pseudo='" + pseudo + "'");
        try {
            idJoueur = (result.next()) ? result.getInt("id") : -1;
            result.close();
        } catch (SQLException e) {
        }
        if (idJoueur == -1) {
            return null;
        }
        result = Dao.getInstance().query("SELECT * FROM shop__items_buy_histories WHERE user_id=" + String.valueOf(idJoueur));
        try {
            if (result.next()) {
                idItem = result.getInt("item_id");
                created = (Date) result.getDate("created");
            } else {
                idItem = -1;
            }
            result.close();
        } catch (SQLException e) {
            return null;
        }

        if(idItem == -1){
            return null;
        }
        result = Dao.getInstance().query("SELECT name, id, timedCommand_time FROM shop__items WHERE id=" + String.valueOf(idItem));
        try {
            if(result.next())
            {
                String name = result.getString("name");
                int idGrade = result.getInt("id");
                int tempsGrade = result.getInt("timedCommand_time");
                long TimeOut = 60000l;
                TimeOut *= tempsGrade;
                TimeOut += created.getTime();
                Date DateFin = new Date(TimeOut);
                long jourRestant = DateFin.getTime() - currentTime.getTime();
                jourRestant /= 60000;
                jourRestant /= 60*24;
                jourRestant += 1;


                gradeInfo.put("nameGrade", name);
                gradeInfo.put("idGrade", String.valueOf(idGrade));
                gradeInfo.put("dateStart", formatter.format(created));
                gradeInfo.put("dateEnd", formatter.format(DateFin));
                gradeInfo.put("DayRemaining", String.valueOf(jourRestant));



                result.next();
            }
            result.close();
        } catch (SQLException e) {
            return null;
        }
        return gradeInfo;
    }


    public static boolean hasSubscribe(String pseudo){

        int idJoueur = -1, size = 0;


        result = Dao.getInstance().query("SELECT id FROM users WHERE pseudo='" + pseudo + "'");
        try {
            idJoueur = (result.next()) ? result.getInt("id") : -1;
            result.close();
        } catch (SQLException e) {
        }
        if (idJoueur == -1) {
            return false;
        }
        result = Dao.getInstance().query("SELECT * FROM shop__items_buy_histories WHERE user_id=" + String.valueOf(idJoueur));
        try {
            result.last();
            size = result.getRow();
            result.beforeFirst();

            result.close();
        } catch (SQLException e) {
            return false;
        }

        return (size != 0) ? true : false;
    }

    public static  boolean remove(String pseudo){
        int idJoueur = -1, size = 0;

        result = Dao.getInstance().query("SELECT id FROM users WHERE pseudo='" + pseudo + "'");
        try {
            idJoueur = (result.next()) ? result.getInt("id") : -1;
            result.close();
        } catch (SQLException e) {
            return false;
        }
        if (idJoueur == -1) {
            return false;
        }

        Dao.getInstance().update("DELETE FROM shop__items_buy_histories WHERE user_id=" + String.valueOf(idJoueur));

        return true;

    }

}


