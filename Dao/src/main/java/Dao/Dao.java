package Dao;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Vaubourg Mandel
 * @version 0.1
 * It is a SingleTon Class used for instanciate one time the connexion to the database.
 */
public class Dao {

    private String bdd_address = "localhost"; //loopback address
    private int bdd_port = 3306;//default Mysql Port
    private String bdd_name = "minecraft";//database name

    //default logging
    private String bdd_username = "root";
    private String bdd_password = "root";
    private String ssl = "";

    //exemple jdbc:mysql://localhost:3306/boulderdash?autoReconnect=true&useSSL=false
    private String bdd_url_connection;


    private static Dao dao_instance = null;

    private Connection bdd_connection = null;

    private Dao(final String bdd_address, final int bdd_port, final String bdd_name, final String bdd_username, final String bdd_password, final boolean useSSL){

        this.bdd_address = bdd_address;
        this.bdd_port = bdd_port;
        this.bdd_name = bdd_name;
        this.bdd_username = bdd_username;
        this.bdd_password = bdd_password;
        this.ssl = (useSSL) ? "true" : "false";



        //Create url database connection
        this.connection();



    }

    public void connection() {


        this.bdd_url_connection = "jdbc:mysql://"
                + this.bdd_address + ":"
                + String.valueOf(this.bdd_port)
                + "/" + this.bdd_name + "?"
                + "autoReconnect=true&useSSL=" + ssl;
        try {
            this.bdd_connection = DriverManager.getConnection(this.bdd_url_connection, this.bdd_username, this.bdd_password);

        } catch (SQLException e) {
            System.err.println("PLEASE VERIFY THE PARAMETERS CONNECTION !");
            e.printStackTrace();
            this.bdd_connection = null;
        }
    }

    public void closeConnection(){
        if(this.bdd_connection != null){
            try {
                this.bdd_connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param bdd_address database address connection
     * @param bdd_port database port connection
     * @param bdd_name database name
     * @param bdd_username database username
     * @param bdd_password database password
     * @param useSSL if you want to secure the connection
     * @return Instance of Dao
     */
    public static Dao getInstance(final String bdd_address, final int bdd_port, final String bdd_name, final String bdd_username, final String bdd_password, final boolean useSSL){
        if(dao_instance == null){
            synchronized(Dao.class){
                if(dao_instance == null)
                    dao_instance = new Dao(bdd_address, bdd_port, bdd_name, bdd_username, bdd_password, useSSL);
            }
        }
        return dao_instance;
    }
    public static Dao getInstance(){
        if(dao_instance == null){
            synchronized(Dao.class){
                if(dao_instance == null)
                    return null;
            }
        }
        return dao_instance;
    }

    public void setBdd_address(String bdd_address) {
        this.bdd_address = bdd_address;
    }

    public void setBdd_connection(Connection bdd_connection) {
        this.bdd_connection = bdd_connection;
    }

    public void setBdd_name(String bdd_name) {
        this.bdd_name = bdd_name;
    }

    public void setBdd_password(String bdd_password) {
        this.bdd_password = bdd_password;
    }

    public void setBdd_username(String bdd_username) {
        this.bdd_username = bdd_username;
    }


    public void setBdd_port(int bdd_port) {
        this.bdd_port = bdd_port;
    }

    //GETTERS
    public String getBdd_username() {
        return bdd_username;
    }
    public String getBdd_password() {
        return bdd_password;
    }
    public Connection getBdd_connection() {
        return bdd_connection;
    }
    public int getBdd_port() {
        return bdd_port;
    }
    public String getBdd_address() {
        return bdd_address;
    }
    public String getBdd_name() {
        return bdd_name;
    }
    public String getBdd_url_connection() {
        return bdd_url_connection;
    }

    public ResultSet query(final String sql){
        try {
            PreparedStatement preparedStatement = (PreparedStatement) dao_instance.getBdd_connection().prepareStatement(sql);
            return preparedStatement.executeQuery();


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update(String request)
    {

        PreparedStatement myPreparedStatement;
        try {
            myPreparedStatement = dao_instance.getBdd_connection().prepareStatement(request);
            myPreparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    public final Optional<PreparedStatement> createStatement(String sql, final ArrayList<Parameters> parameters) {

        int i = 1;
        try{

            PreparedStatement preparedStatement = dao_instance.getBdd_connection().prepareStatement(sql);
            for(Parameters parameter : parameters){

                if(parameter.getParameter() instanceof String){

                    preparedStatement.setString(i, (String)parameter.getParameter());

                }else if(parameter.getParameter() instanceof Integer){

                    preparedStatement.setInt(i, (Integer)parameter.getParameter());

                }else if(parameter.getParameter() instanceof Double){

                    preparedStatement.setDouble(i,(Double)parameter.getParameter());


                }else if(parameter.getParameter() instanceof Boolean){

                    preparedStatement.setBoolean(i, (Boolean)parameter.getParameter());

                }else if(parameter.getParameter() instanceof Date){

                    preparedStatement.setDate(i,(Date) parameter.getParameter());

                }else if(parameter.getParameter() instanceof Short){
                    preparedStatement.setShort(i, (Short)parameter.getParameter());
                }
                i++;
            }
            return Optional.of(preparedStatement);
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static void executeStatement(final PreparedStatement statement){
        try {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
