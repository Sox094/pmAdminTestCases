package Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnector {

    private Connection policydbCore = null;
    private PreparedStatement scorePrepareStat = null;

    public DatabaseConnector() {
        makeJDBCConnection();
    }

    // Simple log utility
    private static void log(String string) {
        System.out.println(string);

    }

    private void makeJDBCConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            policydbCore = DriverManager.getConnection("jdbc:mysql://localhost:3306/policydb_core", "root", "pmAdmin");
        } catch (SQLException e) {
            log("MySQL Connection Failed!");
            e.printStackTrace();
            return;
        }
    }

    public void closeConnection() {
        try {
            policydbCore.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
