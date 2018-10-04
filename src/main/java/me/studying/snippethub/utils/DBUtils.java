package me.studying.snippethub.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "SYSTEM", "jerala");
    }
}
