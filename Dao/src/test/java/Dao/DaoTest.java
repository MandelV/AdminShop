package Dao;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import static org.junit.Assert.fail;

public class DaoTest {

    private static Dao dao = null;


    @BeforeClass
    public static void initDaoTest(){

        System.out.println("[TEST-unit] init Dao");
        dao = Dao.getInstance("localhost", 1306, "adminshop", "root", "root", false);
        if(dao == null){
            fail("[TEST-unit][FAIL] : instanciate DAO");
        }
    }

    @Test
    public void daoTestConnection(){

        System.out.println("[TEST-unit] test connection");
        Connection connection = dao.getBdd_connection();
        if(connection == null){
            fail("[TEST-unit][FAIL] : connection null");
        }

    }
}
