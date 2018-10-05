package me.studying.snippethub.utils;

import java.sql.*;
import java.util.HashMap;

public class DBUtils {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "SYSTEM", "jerala");
    }

}
