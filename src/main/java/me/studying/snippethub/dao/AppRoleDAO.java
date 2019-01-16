package me.studying.snippethub.dao;

import java.sql.*;
import java.util.List;

import javax.sql.DataSource;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import me.studying.snippethub.entity.UserRole;
import me.studying.snippethub.utils.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AppRoleDAO extends JdbcDaoSupport {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public AppRoleDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<String> getRoleNames(Long userId) {
        String sql = "Select ur.appRole.roleName from " + UserRole.class.getName() + " ur " //
                + " where ur.appUser.userId = :userId ";
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    private static Long getMaxID() {
        try
        {
            Connection con = DBUtils.getConnection();
            CallableStatement stmt = con.prepareCall("{? = call GETMAXROLEUSERID()}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.execute();
            con.close();
            return (long)stmt.getInt(1);
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        return 0L;
    }

    public static void createUserRole(Long userId) {
        try
        {
            Connection con = DBUtils.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO public.USER_ROLE " +
                    "(ID, USER_ID, ROLE_ID) VALUES (?, ?, ?)");
            stmt.setLong(1, getMaxID() + 1);
            stmt.setLong(2, userId);
            stmt.setLong(3, 2);

            stmt.executeUpdate();

            con.close();
            stmt.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

}