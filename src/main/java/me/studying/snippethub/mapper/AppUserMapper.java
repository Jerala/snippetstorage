package me.studying.snippethub.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.studying.snippethub.utils.EncrytedPasswordUtils;
import me.studying.snippethub.model.AppUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static me.studying.snippethub.utils.EncrytedPasswordUtils.encrytePassword;

public class AppUserMapper implements RowMapper<AppUser> {

    public static final String BASE_SQL //
            = "Select u.UserId, u.UserName, u.Password, u.EMAIL From USERS u ";

    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        Long userId = rs.getLong("USERID");
        String userName = rs.getString("USERNAME");
        String encrytedPassword = rs.getString("Password");
        String email = rs.getString("EMAIL");

        return new AppUser(userId, userName, email, encrytedPassword);
    }

}
