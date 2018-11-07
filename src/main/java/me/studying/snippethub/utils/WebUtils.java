package me.studying.snippethub.utils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.json.*;

public class WebUtils {

    public static String toString(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("UserName:").append(user.getUsername());

        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" (");
            boolean first = true;
            for (GrantedAuthority a : authorities) {
                if (first) {
                    sb.append(a.getAuthority());
                    first = false;
                } else {
                    sb.append(", ").append(a.getAuthority());
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public static Long getUserID(String userName) {

        if(userName.equals(null))
            userName = getCurrentUserName();

        try
        {
            System.out.println(userName);
            Connection con = DBUtils.getConnection();
            CallableStatement stmt = con.prepareCall("{? = call GETUSERID(?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setObject(2, userName);
            stmt.execute();

            return (long)stmt.getInt(1);
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        return 0L;
    }

    public static String getCurrentUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String principal = auth.getPrincipal().toString();
        String substr = principal.substring(principal.indexOf("Username: "));
        int firstIdx = 10, secondIdx = substr.indexOf(";");
        String userName = substr.substring(firstIdx, secondIdx);
        return userName;
    }

    private static HashMap<Integer,String> getPlangsMap() {
        HashMap<Integer, String> hm = new HashMap<>();
        try {
            Connection con = DBUtils.getConnection();
            PreparedStatement stat = con.prepareStatement(
                    "SELECT * FROM PLANGS");
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                hm.put(rs.getInt(1), rs.getString(2));
            }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        return hm;
    }

    public static JSONArray getJSONofUserSnippets(Long id) {
        HashMap<Integer, String> pLangs = getPlangsMap();
       // Long userID = getUserID();
        try {
            Connection con = DBUtils.getConnection();
            PreparedStatement stat = con.prepareStatement(
                    "SELECT * from public.snippets where user_id=" + Long.toString(id));
            ResultSet rs = stat.executeQuery();

            JSONArray json = new JSONArray();
            ResultSetMetaData rsmd = rs.getMetaData();

            Integer pl_id = 0;
            while(rs.next()) {
                int numColumns = rsmd.getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i=1; i<=numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    obj.put(column_name, rs.getObject(column_name));
                    if(column_name.toLowerCase().equals("pl_id"))
                        pl_id = (int)rs.getObject(column_name);
                }
                obj.put("pl_name", pLangs.get(pl_id));
                json.put(obj);
            }
            return json;
        }
        catch(SQLException | JSONException e) {
            System.out.println(e);
        }
        return new JSONArray();
    }
}