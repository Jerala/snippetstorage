package me.studying.snippethub.service;

import me.studying.snippethub.entity.Queries;
import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.utils.DBUtils;
import me.studying.snippethub.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SnippetsServiceImpl {

    @Autowired
    private EntityManager entityManager;

    public List<Snippets> findSnippetByTag(String tags) {


        List<Snippets> snippets = new ArrayList<>();

        List<String> cmb = getCombinationsOfKeyWords(tags);
        String query = generateQuery(cmb);

        try {
            Connection connection = DBUtils.getConnection();

            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Snippets snippet = new Snippets();
                snippet.setSnippetId(rs.getLong("SNIPPET_ID"));
                snippet.setPLID(rs.getLong("PL_ID"));
                snippet.setSnippet_name(rs.getString("SNIPPET_NAME"));
                snippet.setUser_id(rs.getLong("USER_ID"));
                snippet.setUploadDate(rs.getDate("UPLOAD_DATE"));
                snippet.setLikesCount(rs.getLong("LIKE_COUNT"));
                snippet.setApproved(rs.getInt("APPROVED"));
                snippet.setTags(rs.getString("TAGS"));

                snippets.add(snippet);
            }
            stmt.close();
            connection.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        return snippets;
    }

    private List<String> getCombinationsOfKeyWords(String tagString) {
        List<String> tags = new ArrayList<>(Arrays.asList(tagString.split(" ")));
        // ограничение в 5 тегов
        if(tags.size() > 5)
            tags = tags.subList(0, 5);
        List<String> combinationsOfTags = new ArrayList<>();
        for(int i = tags.size(); i >= 1; i--)
            combinationsOfTags.addAll(getCombinationsOfKeyWords(i, tags));
        return combinationsOfTags;
    }

    private List<String> getCombinationsOfKeyWords(int countOfTagsInOneCombination,
                                                   List<String> combinations) {

        List<String> newCombs = new ArrayList<>();
        if(countOfTagsInOneCombination == 1) {
            for(String i: combinations)
                newCombs.add(i);
            return newCombs;
        }
        int prevSize = 0;
        for(int i = 0; i < combinations.size(); i++) {
            String currElem = combinations.get(i);
            newCombs.addAll(getCombinationsOfKeyWords(countOfTagsInOneCombination - 1, combinations.subList(1 + i, combinations.size())));
            for(int j = prevSize; j < newCombs.size(); j++)
                newCombs.set(j, newCombs.get(j) + " " + currElem);
            prevSize = newCombs.size();
        }
        return newCombs;
    }

    private String generateQuery(List<String> combinations) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT foo.snippet_id, foo.pl_id, foo.snippet_name, " +
                "foo.user_id,foo.upload_date, foo.like_count, foo.approved, foo.tags, sum(rs) as a FROM (");
        for(int i = 0; i < combinations.size(); i++) {
            String[] words = combinations.get(i).split(" ");
            query.append("SELECT snippet_id, pl_id, snippet_name, user_id," +
                    "upload_date, like_count, approved, tags," + Integer.toString(words.length)
                    + " as rs FROM public.SNIPPETS WHERE TAGS LIKE ");

            for(int j = 0; j < words.length; j++) {
                if(j + 1 == words.length)
                    query.append("\'%" + words[j] + "%\' ");
                else
                    query.append("\'%" + words[j] + "%\' AND TAGS LIKE ");
            }
            if(i + 1 != combinations.size())
                query.append("\nUNION\n");
        }
        query.append(") as foo\n" +
                "group by foo.snippet_id, foo.pl_id, foo.snippet_name, " +
                "foo.user_id,foo.upload_date, foo.like_count, foo.approved, foo.tags\n" +
                "order by a desc");
        return query.toString();
    }

    public void registerSearchQuery(String tags) {
        // create query
        List<String> tagsArr = new ArrayList<>(Arrays.asList(tags.split(" ")));
        if(tagsArr.size() > 5)
            tagsArr = tagsArr.subList(0,5);
        tagsArr.sort(String::compareToIgnoreCase);
        StringBuilder query = new StringBuilder();

        for(int i = 0; i < tagsArr.size() - 1; i++)
            query.append(tagsArr.get(i) + " ");
        query.append(tagsArr.get(tagsArr.size() - 1));

        // perform query
        try {
            Connection connection = DBUtils.getConnection();
            CallableStatement stmt =
                    connection.prepareCall(
                            "{? = call getcountforquery(?)}");
            stmt.registerOutParameter(1, Types.BIGINT);
            stmt.setString(2, query.toString());
            stmt.execute();
            Long counter = stmt.getLong(1);
            stmt.close();

            // create new row in table
            if(counter == 0) {
                PreparedStatement statement = connection
                        .prepareStatement(
                                "INSERT INTO public.QUERIES (query, " +
                                        "counter, searched)" +
                                " VALUES (?, ?, ?)");
                statement.setString(1, query.toString());
                statement.setLong(2, 1L);
                statement.setDate(3, new java.sql.Date((new java.util.Date()).getTime()));
                statement.executeUpdate();
                statement.close();
            }
            else {
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE public.QUERIES SET counter = counter + 1 WHERE query = ?");
                statement.setString(1, query.toString());
                statement.executeUpdate();
                statement.close();
            }
            connection.close();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Long getSnippetId(Long userId, String snippetName) {
        try {
            Connection connection = DBUtils.getConnection();
            CallableStatement stmt = connection.prepareCall(
                    "{? = call getsnippetid(?, ?)}");
            stmt.registerOutParameter(1, Types.BIGINT);
            stmt.setLong(2, userId);
            stmt.setString(3, snippetName);
            stmt.execute();
            return stmt.getLong(1);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0L;
    }

}
