package at.fhtw.users.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Users {
    @JsonAlias({"id"})
    private Integer id;
    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;



    public Users() {
    }

    public Users(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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