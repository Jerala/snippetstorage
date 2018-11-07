package me.studying.snippethub.dao;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;

import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.formbean.UploadForm;
import me.studying.snippethub.utils.DBUtils;
import me.studying.snippethub.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SnippetsDAO extends JdbcDaoSupport {

    @Autowired
    private EntityManager entityManager;

    private static final Map<Long, Snippets> Snippets_MAP = new HashMap<>();

    static {
        initDATA();
    }

    private static void initDATA() {

        try
        {
            Connection con = DBUtils.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM public.Snippets");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Snippets snippet = new Snippets();
                snippet.setSnippetId(rs.getLong("Snippet_ID"));
                snippet.setPLID(rs.getLong("PL_ID"));
                snippet.setSnippet_name(rs.getString("Snippet_Name"));
                snippet.setUser_id(rs.getLong("USER_ID"));
                snippet.setUploadDate(rs.getDate("UPLOAD_DATE"));
                snippet.setLikesCount(rs.getLong("Like_Count"));
                int approved = rs.getInt("Approved");
                boolean approvedBool = approved == 0 ? false : true;
                snippet.setApproved(approvedBool);
                Snippets_MAP.put(snippet.getSnippetId(), snippet);
            }

            con.close();
            stmt.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    @Autowired
    public SnippetsDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public Snippets findSnippet(String snippetName) {
        try {
            String sql = "Select e from " + Snippets.class.getName() + " e " //
                    + " Where e.snippet_name = :snippetName ";

            Query query = entityManager.createQuery(sql, Snippets.class);
            query.setParameter("snippetName", snippetName);
            System.out.println(query);
            return (Snippets) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getMaxSnippetId() {
        long max = 0;
        for (Long id : Snippets_MAP.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max;
    }



    public Snippets findSnippetByName(String snippetName) {
        Collection<Snippets> snippets = Snippets_MAP.values();
        for (Snippets u : snippets) {
            if (u.getSnippet_name().equals(snippetName)) {
                return u;
            }
        }
        return null;
    }

    public List<Snippets> getSnippets() {
        List<Snippets> list = new ArrayList<>();

        list.addAll(Snippets_MAP.values());
        return list;
    }

    public Snippets findSnippetByNameAndUserID(String snippet_name, long userID) {
        Collection<Snippets> snippets = Snippets_MAP.values();
        for (Snippets s : snippets) {
            if(s.getSnippet_name().equals(snippet_name)
            && s.getUser_id().equals(userID))
                return s;
        }
        return null;
    }

    public Snippets createSnippet(UploadForm form) {
        Long snippetId = this.getMaxSnippetId() + 1;
        Long userId = WebUtils.getUserID(null);
        String snippet_name = form.getSnippet_name().replaceAll(" ", "_");

        String plName = getPLName(form.getPLID());

        String tagsStr = "";
        if(form.getTags() != null && !form.getTags().equals(""))
            tagsStr = "#" + plName.toLowerCase() + formatTags(form.getTags());
        else
            tagsStr = "";

        createSnippetFile(userId, form.getCode_text(), plName, snippet_name);

        try
        {
            Connection con = DBUtils.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO public.Snippets " +
                    "(SNIPPET_ID, PL_ID, SNIPPET_NAME, USER_ID, UPLOAD_DATE, LIKE_COUNT," +
                    "APPROVED, TAGS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setLong(1, snippetId);
            stmt.setLong(2, form.getPLID());
            stmt.setString(3, snippet_name);
            stmt.setLong(4, userId);
            stmt.setDate(5, new java.sql.Date((new Date()).getTime()));
            stmt.setLong(6, 0);
            stmt.setInt(7, 0);
            stmt.setString(8, tagsStr);

            stmt.executeUpdate();

            con.close();
            stmt.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        Snippets snippet = new Snippets(snippetId, form.getPLID(), snippet_name,
                userId, new Date(), 0L, false, tagsStr);

        Snippets_MAP.put(snippet.getSnippetId(), snippet);
        return snippet;
    }


    private String formatTags(String tags) {
        String[] tag = tags.split(" ");
        StringBuilder sb = new StringBuilder();
        for(String t : tag) {
            if(t.charAt(0) == '#')
                sb.append(t.toLowerCase());
            else {
                sb.append("#" + t.toLowerCase());
            }
        }
        return sb.toString();
    }

    private void createSnippetFile(long userId, String text,
                                   String PLName, String snippetName) {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                new FileOutputStream(
                        "snippets/" + PLName + "/" + Long.toString(userId) + "-" +
                                snippetName + ".txt"), "utf-8"))) {
            writer.write(text);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private String getPLName(long PLID) {
//        try {
//            Connection con = DBUtils.getConnection();
//            CallableStatement stmt = con.prepareCall("{? = call GETPLNAMEBYID(?)}");
//            stmt.registerOutParameter(1, Types.VARCHAR);
//            stmt.setObject(2, PLID);
//            stmt.execute();
//
//            return stmt.getString(1);
//        }
//        catch(SQLException e) {
//            e.printStackTrace();
//        }
        return PLangsDAO.getPLangs_MAP().get(PLID).getPLName();
        // default lang
       // return "Java";
    }

}