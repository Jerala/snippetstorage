package me.studying.snippethub.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

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

    public static Long getUserID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String principal = auth.getPrincipal().toString();
        String substr = principal.substring(principal.indexOf("Username: "));
        int firstIdx = 10, secondIdx = substr.indexOf(";");
        String userName = substr.substring(firstIdx, secondIdx);

        try
        {
            System.out.println(userName);
            Connection con = DBUtils.getConnection();
            CallableStatement stmt = con.prepareCall("{call GETUSERID(?,?)}");
            stmt.setObject(1, userName);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();

            return (long)stmt.getInt(2);
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        return 0L;
    }
}