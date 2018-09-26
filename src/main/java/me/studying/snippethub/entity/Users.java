package me.studying.snippethub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Users", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "Users_UK", columnNames = "User_Name") })
public class Users {

    public Users() {}
    public Users(Long userId, String userName, //
                   String email, String encrytedPassword, boolean enabled) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.enabled = enabled;
        this.email = email;
        this.encrytedPassword = encrytedPassword;
    }

    @Id
    @GeneratedValue
    @Column(name = "User_Id", nullable = false)
    private Long userId;

    @Column(name = "User_Name", length = 36, nullable = false)
    private String userName;

    @Column(name = "Password", length = 128, nullable = false)
    private String encrytedPassword;

    @Column(name = "Enabled", length = 2, nullable = false)
    private boolean enabled;

    @Column(name = "EMAIL", length = 128, nullable = false)
    private String email;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncrytedPassword() {
        return encrytedPassword;
    }

    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}