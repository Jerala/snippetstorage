package me.studying.snippethub.service;

import me.studying.snippethub.utils.DBUtils;
import me.studying.snippethub.utils.WebUtils;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class LikeServiceImpl {

    public String isLiked(Long userId, String snippetName, String currUserName) {
        try {
            Connection connection = DBUtils.getConnection();
            Long snippetId = getSnippetId(connection, userId, snippetName);
            Long currUserId = WebUtils.getUserID(currUserName);
            PreparedStatement statement = connection.prepareStatement(
                    "select * from likes where snippet_id = ? and user_id = ?");
            statement.setLong(1, snippetId);
            statement.setLong(2, currUserId);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) return "yes";
        } catch(SQLException e) {
            System.out.println(e);
        }
        return "no";
    }

    private Long getSnippetId(Connection connection, Long userId, String snippetName)
    throws SQLException {
        CallableStatement statement = connection.prepareCall(
                "{? = call getsnippetid(?, ?)}");
        statement.registerOutParameter(1, Types.BIGINT);
        statement.setLong(2, userId);
        statement.setString(3, snippetName);
        statement.execute();
        return statement.getLong(1);
    }

    public String changeState(Long userId, String snippetName, String currUserName) {
        try {

            Connection connection = DBUtils.getConnection();
            PreparedStatement statement = null;
            Long snippetId = getSnippetId(connection, userId, snippetName);
            Long currUserId = WebUtils.getUserID(currUserName);

            if (isLiked(userId, snippetName, currUserName).equals("yes")) {
                statement = connection.prepareStatement("DELETE FROM LIKES WHERE " +
                        "snippet_id = ? and user_id = ?");
                changeLikesCount(connection, snippetId, false);
            }
            else {
                statement = connection.prepareStatement(
                        "INSERT INTO LIKES(snippet_id, user_id)" +
                                " values(?, ?)");
                changeLikesCount(connection, snippetId, true);
            }
            statement.setLong(1, snippetId);
            statement.setLong(2, currUserId);
            statement.executeUpdate();
            statement.close();
            connection.close();
            return "success";
        } catch(SQLException e) {
            System.out.println(e);
            return "failed";
        }
    }

    private void changeLikesCount(Connection connection,
                                  Long snippetId,
                                  boolean isIncrement) throws SQLException {
        PreparedStatement statement = null;
        if(isIncrement) {
            statement = connection.prepareStatement(
                    "UPDATE public.SNIPPETS SET like_Count = like_Count + 1 WHERE snippet_id = ?");
        }
        else {
            statement = connection.prepareStatement(
                    "UPDATE public.SNIPPETS SET like_Count = like_Count - 1 WHERE snippet_id = ?");
        }
        statement.setLong(1, snippetId);
        statement.executeUpdate();
    }
}
