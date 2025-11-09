package repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/fashion_store";
    private static final String USER = "root";  // đổi tùy máy bạn
    private static final String PASS = "29012012";      // đổi nếu có mật khẩu

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
