package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Package {
    public Package(String id, String userId, String name, double damage, boolean isAdmin) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.damage = damage;
        this.isAdmin = isAdmin;
    }

    public Package() {
        // Default constructor is required for Jackson deserialization
    }

    @JsonAlias({"Id"})
    private String id;
    @JsonAlias({"UserId"})
    private String userId;
    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private double damage;

    @JsonAlias({"isAdmin"})
    private boolean isAdmin;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }


    public String getUserId() {
        return userId;
    }
}
