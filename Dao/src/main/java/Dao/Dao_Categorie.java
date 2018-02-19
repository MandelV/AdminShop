package Dao;

import com.github.MandelV.ChatFormatting.tools.ChatFormatting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dao_Categorie {

    private String name;
    private String id_item;
    private ResultSet result;

    private List<String> descriptions;

    public Dao_Categorie(final String name, final String id_item){

        this.name = name;
        this.id_item = id_item;
        this.descriptions = new ArrayList<>();
        this.requestDescriptions();
    }

    public void addDescriptions(String desc) {
        this.descriptions.add(desc);
    }

    public String getId_item() {
        return id_item;
    }


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

    public List<String> getDescriptions(){
        return this.descriptions;
    }

    public String getName() {
        return this.name;
    }
}
