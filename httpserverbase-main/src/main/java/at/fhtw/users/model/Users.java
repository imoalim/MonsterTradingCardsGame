package at.fhtw.users.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Users {
    @JsonAlias({"id"})
    private Integer id;
    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;
    @JsonAlias({"Email"})
    private String email;
    @JsonAlias({"Firstname"})
    private String firstname;
    @JsonAlias({"Lastname"})
    private String lastname;


    public Users() {
    }

    public Users(Integer id, String username, String password, String email, String firstname, String lastname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
