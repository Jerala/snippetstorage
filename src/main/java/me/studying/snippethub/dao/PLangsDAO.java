package me.studying.snippethub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.studying.snippethub.entity.PLangs;
import me.studying.snippethub.entity.Users;
import me.studying.snippethub.utils.DBUtils;
import org.springframework.stereotype.Repository;

@Repository
public class PLangsDAO {

    private static final Map<Long, PLangs> PLangs_MAP = new HashMap<>();

    static {
        initDATA();
    }

    public static Map<Long, PLangs> getPLangs_MAP() {
        return PLangs_MAP;
    }

    private static void initDATA() {
        try
        {
            Connection con = DBUtils.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM public.PLangs");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                PLangs lang = new PLangs();
                lang.setPLID(rs.getLong("PL_ID"));
                lang.setPLName(rs.getString("PL_name"));
                PLangs_MAP.put(lang.getPLID(), lang);
            }

            con.close();
            stmt.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    public PLangs findPLByID(Long ID) {
        return PLangs_MAP.get(ID);
    }

    public List<PLangs> getPLangs() {
        List<PLangs> list = new ArrayList<>();
        list.addAll(PLangs_MAP.values());
        return list;
    }

}