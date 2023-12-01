package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.sql.Timestamp;

public class User {

    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;

    private boolean userExists;


    public void setUserExists(boolean userExists) {
        this.userExists = userExists;
    }

    public boolean isUserExists() {
        return userExists;
    }

    public User() {
    }

    public User(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
