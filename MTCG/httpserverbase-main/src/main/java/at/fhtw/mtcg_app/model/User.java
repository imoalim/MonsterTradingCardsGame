package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;


public class User {
    public User() {
    }

    public User(String token, String id, String username, String password, int coins) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.password = password;
        this.coins = coins;
    }

    @JsonAlias({"Id"})
    private String id;
    @JsonAlias({"Token"})
    private String token;

    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;

    @JsonAlias({"Coins"})
    private int coins;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
