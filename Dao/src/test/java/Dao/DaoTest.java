package Dao;

import org.junit.Test;

import static org.junit.Assert.fail;

public class DaoTest {

    private static Dao dao = null;


    @Test
    public void initDaoTest(){

        System.out.println("[TEST-unit] init Dao");

        dao = Dao.getInstance("localhost", 3306, "minecraft", "root", "root", false);
        System.out.println(dao.getBdd_url_connection() + " " + dao.getBdd_username() + " " + dao.getBdd_password());
        if(dao == null){
            fail("[TEST-unit][FAIL] : instanciate DAO");
        }else{

            dao.closeConnection();
        }


    }
}
