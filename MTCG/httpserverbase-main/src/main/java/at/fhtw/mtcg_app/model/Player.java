package at.fhtw.mtcg_app.model;

import java.util.List;

public class Player {
    private String playerName;
    private int playerId;
    private List<Card> monsterCards;
    private List<Card> spellCards;


    // Constructor for Player with a player ID
    public Player(int userID, String playerName, List<Card> monsterCards, List<Card> spellCards) {
        this.playerId = userID;
        this.playerName = playerName;
        this.monsterCards = monsterCards;
        this.spellCards = spellCards; // Fetch name from the database using player ID
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    public String getPlayerName() {
        return playerName;
    }


    public List<Card> getMonsterCards() {
        return monsterCards;
    }

    public void setMonsterCards(List<Card> monsterCards) {
        this.monsterCards = monsterCards;
    }

    public List<Card> getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(List<Card> spellCards) {
        this.spellCards = spellCards;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


}
