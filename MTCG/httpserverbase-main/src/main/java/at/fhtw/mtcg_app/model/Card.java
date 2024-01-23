package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Card {

    public Card(String cardId, String name, double damage) {
        this.cardId = cardId;
        this.name = name;
        this.damage = damage;
    }

    public enum ElementType {
        WATER, FIRE, NORMAL
    }

    public enum SpecialType {
        GOBLINS, DRAGONS, WIZARD, ORKS, KNIGHTS, KRAKEN, ELVES, TROLL, NORMAL
    }

    private SpecialType specialType;
    private ElementType elementType;

    @JsonAlias({"Id"})
    private String cardId;
    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private double damage;

    public Card(String cardId, String name, double damage, ElementType elementType, SpecialType specialType) {
        this.cardId = cardId;
        this.name = name;
        this.damage = damage;
        this.elementType = elementType;
        this.specialType = specialType;
    }

    public Card() {
    }

    public String getCardId() {
        return cardId;
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

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public SpecialType getSpecialType() {
        return specialType;
    }

    public void setSpecialType(SpecialType specialType) {
        this.specialType = specialType;
    }
}
