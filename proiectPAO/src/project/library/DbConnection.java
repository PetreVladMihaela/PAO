package project.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static Connection connection;

    public static Connection getInstance() throws SQLException {
        if(connection == null) {
            String url = "jdbc:mysql://localhost:3306/proiect_pao";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }
}
