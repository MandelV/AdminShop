package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @author MandelV
 * @version 0.2
 * It is a SingleTon Class used for instanciate one time the connexion to the database.
 */
public class Dao {

    private String bddAddress = "localhost"; //loopback address
    private int bddPort = 3306;//default Mysql Port
    private String bddName = "minecraft";//database name

    //default logging
    private String bddUsername = "root";
    private String bddPassword = "root";
    private String ssl = "";

    private String bddUrlConnection;

    private static Dao daoInstance = null;

    private Connection bddConnection = null;

    private Dao(final String bddAddress, final int bddPort, final String bddName, final String bddUsername, final String bddPassword, final boolean useSSL){

            this.bddAddress = bddAddress;
            this.bddPort = bddPort;
            this.bddName = bddName;
            this.bddUsername = bddUsername;
            this.bddPassword = bddPassword;
            this.ssl = (useSSL) ? "true" : "false";

        //Create url database connection
        this.connection();
    }

    public void connection() {




        this.bddUrlConnection = "jdbc:mysql://"
                + this.bddAddress + ":"
                + String.valueOf(this.bddPort)
                + "/" + this.bddName + "?"
                + "autoReconnect=true&useSSL=" + ssl;

        try {

            this.bddConnection = DriverManager.getConnection(this.bddUrlConnection, this.bddUsername, this.bddPassword);

        } catch (SQLException e) {
            System.err.println("PLEASE VERIFY THE PARAMETERS CONNECTION !");
            e.printStackTrace();
            this.bddConnection = null;
        }
    }

    public void closeConnection(){
        if(this.bddConnection != null){
            try {
                this.bddConnection.close();
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
        if(daoInstance == null){
            synchronized(Dao.class){
                if(daoInstance == null)
                    daoInstance = new Dao(bdd_address, bdd_port, bdd_name, bdd_username, bdd_password, useSSL);
            }
        }
        return daoInstance;
    }
    public static Dao getInstance(){
        if(daoInstance == null){
            synchronized(Dao.class){
                if(daoInstance == null)
                    return null;
            }
        }
        return daoInstance;
    }

    /**
     * @param bdd_address set a database address
     */
    public void setBddAddress(String bdd_address) {
        this.bddAddress = bdd_address;
    }

    /**
     * @param bdd_connection set a Connection
     * @see Connection
     */
    public void setBddConnection(Connection bdd_connection) {
        this.bddConnection = bdd_connection;
    }

    /**
     * @param bdd_name set database name
     */
    public void setBddName(String bdd_name) {
        this.bddName = bdd_name;
    }
    /**
     * @param bdd_password set database password
     */
    public void setBddPassword(String bdd_password) {
        this.bddPassword = bdd_password;
    }

    /**
     * @param bddUsername set database username
     */
    public void setBddUsername(String bddUsername) {
        this.bddUsername = bddUsername;
    }

    /**
     * @param bddPort set database port
     */
    public void setBddPort(int bddPort) {
        this.bddPort = bddPort;
    }

    //GETTERS

    /**
     * @return username
     */
    public String getBddUsername() {
        return bddUsername;
    }
    /**
     * @return password
     */
    public String getBddPassword() {
        return bddPassword;
    }
    /**
     * @return connection
     * @see Connection
     */
    public Connection getBddConnection() {
        return bddConnection;
    }
    /**
     * @return port
     */
    public int getBddPort() {
        return bddPort;
    }
    /**
     * @return database address
     */
    public String getBddAddress() {
        return bddAddress;
    }
    /**
     * @return database name
     */
    public String getBddName() {
        return bddName;
    }
    /**
     * @return url connection
     */
    public String getBddUrlConnection() {
        return bddUrlConnection;
    }
    /**
     * @return ResultSet which represent your request
     * @see ResultSet
     */
    private synchronized ResultSet requestQuery(final String sql){
        try {
            PreparedStatement preparedStatement = (PreparedStatement) daoInstance.getBddConnection().prepareStatement(sql);
            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @return a ResultSet which represente your request
     * @see ResultSet
     */
    public ResultSet query(final String sql) {

        Callable<ResultSet> callable = () -> {
            return requestQuery(sql);
        };
        try {
            return callable.call();
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    /**
     * @param request allow to execute update request
     */
    public void update(String request) {

        Runnable asyncUpdate = () -> {
            PreparedStatement myPreparedStatement;
            try {
                myPreparedStatement = daoInstance.getBddConnection().prepareStatement(request);
                myPreparedStatement.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();


            }
        };

        new Thread(asyncUpdate).start();

    }

    /**
     * @param sql you sql request
     * @param parameters the parameters of this request
     * @return PreparedStatement which represente your request encapsulate in Optional
     * @see PreparedStatement
     * @see Optional
     */
    public final Optional<PreparedStatement> createStatement(String sql, final ArrayList<Parameters> parameters) {

        int i = 1;
        try{

            PreparedStatement preparedStatement = daoInstance.getBddConnection().prepareStatement(sql);
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

    /**
     * @param statement your request
     */
    public static void executeStatement(final PreparedStatement statement){
        Runnable asyncStatement = () -> {
            try {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        new Thread(asyncStatement).start();
    }
}
