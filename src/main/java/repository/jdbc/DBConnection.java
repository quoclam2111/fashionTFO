package repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3307/fashion_store?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "";
        
        return DriverManager.getConnection(url, username, password);
    }
}
