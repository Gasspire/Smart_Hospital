package it.unict.smarthospital;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseAuthHelper {
    private String url = "jdbc:mariadb://localhost:3306/smart_hospital_auth";
    private String user = "*"; 
    private String password = "*"; 

    public DataBaseAuthHelper(){

    }


    public Connection getConnection() throws SQLException{
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println("DataBase collegato correttamente");

        return conn;
    }

}
