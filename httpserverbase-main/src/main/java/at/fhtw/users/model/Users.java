package at.fhtw.users.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.UUID;

public class Users {

    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;


    public Users() {
    }

    public Users(String username, String password) {

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
