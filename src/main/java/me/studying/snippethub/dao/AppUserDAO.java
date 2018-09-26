package me.studying.snippethub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;
import me.studying.snippethub.mapper.AppUserMapper;
import me.studying.snippethub.formbean.AppUserForm;
//import me.studying.snippethub.model.AppUser;
import me.studying.snippethub.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
@Transactional
public class AppUserDAO extends JdbcDaoSupport {

    // Config in WebSecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    private static final Map<Long, Users> USERS_MAP = new HashMap<>();

    static {
        initDATA();
    }

    private static void initDATA() {

        try
        {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "SYSTEM", "parlipa555");
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM USERS");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Users user = new Users();
                user.setUserId(rs.getLong("USER_ID"));
                user.setUserName(rs.getString("USER_NAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setEncrytedPassword(rs.getString("PASSWORD"));
                user.setEnabled(false);
                USERS_MAP.put(user.getUserId(), user);
            }

            con.close();
            stmt.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    @Autowired
    public AppUserDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

 /*   public AppUser findUserAccount(String userName) {
        // Select .. from App_User u Where u.User_Name = ?
        String sql = AppUserMapper.BASE_SQL + " where u.UserName = ? ";

        Object[] params = new Object[] { userName };
        AppUserMapper mapper = new AppUserMapper();
        try {
            AppUser userInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
            return userInfo;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }*/

    public Users findUserAccount(String userName) {
        try {
           // String sql = AppUserMapper.BASE_SQL + " where u.UserName = :userName ";
            String sql = "Select e from " + Users.class.getName() + " e " //
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, Users.class);
            query.setParameter("userName", userName);
            System.out.println(query);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getMaxUserId() {
        long max = 0;
        for (Long id : USERS_MAP.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max;
    }



    public Users findAppUserByUserName(String userName) {
        Collection<Users> appUsers = USERS_MAP.values();
        for (Users u : appUsers) {
            if (u.getUserName().equals(userName)) {
                return u;
            }
        }
        return null;
    }

    public Users findAppUserByEmail(String email) {
        Collection<Users> appUsers = USERS_MAP.values();
        for (Users u : appUsers) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public List<Users> getAppUsers() {
        List<Users> list = new ArrayList<>();

        list.addAll(USERS_MAP.values());
        return list;
    }

    public Users createAppUser(AppUserForm form) {
        Long userId = this.getMaxUserId() + 1;
        String encrytedPassword = this.passwordEncoder.encode(form.getPassword());
        try
        {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "SYSTEM", "parlipa555");
            PreparedStatement stmt = con.prepareStatement("INSERT INTO USERS " +
                            "(USER_ID, USER_NAME, PASSWORD, EMAIL, ROLEID, ENABLED) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setLong(1, userId);
            stmt.setString(2, form.getUserName());
            stmt.setString(3, encrytedPassword);
            stmt.setString(4, form.getEmail());
            stmt.setInt(5, 2);
            stmt.setString(6, "0");

            stmt.executeUpdate();

            con.close();
            stmt.close();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        Users user = new Users(userId, form.getUserName(), //
                form.getEmail(),  //
                encrytedPassword, false);

        USERS_MAP.put(userId, user);
        return user;
    }

}