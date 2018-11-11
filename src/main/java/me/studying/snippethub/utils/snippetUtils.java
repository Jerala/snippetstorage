package me.studying.snippethub.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;

public class snippetUtils {
    public static HashMap<String, Object> getSnippetData(int user_id, String snippet_name) {
        HashMap<String, Object> snippetData = new HashMap<>();
        try {
            Connection con = DBUtils.getConnection();
            String queryText = "SELECT * FROM public.SNIPPETS WHERE USER_ID=" + user_id + " " +
                    "AND SNIPPET_NAME=\'" + snippet_name + "\'";
            PreparedStatement stat = con.prepareStatement(queryText);
            ResultSet rs = stat.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    snippetData.put(column_name, rs.getObject(column_name));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return snippetData;
    }

    public static String getSnippetText(String pl_name, int user_id, String snippet_name) {
        String path = "./snippets/" + pl_name + "/" + user_id + "-" + snippet_name + ".txt";
        String text = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            text = new String(encoded, "UTF-8");
        }
        catch(IOException e) {
            System.out.println(e);
        }
        return text;
    }
}
