package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Card {
    public Card(String cardId, String name, double damage) {
        this.cardId = cardId;
        this.name = name;
        this.damage = damage;
    }

    public Card() {

    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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

    @JsonAlias({"Id"})
    private String cardId; // Card ID
    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private double damage;
}
