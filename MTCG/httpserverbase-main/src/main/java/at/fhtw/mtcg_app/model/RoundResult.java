package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"roundNumber", "player1Damage", "player2Damage", "winner", "fightType"})
public class RoundResult {
    private final int roundNumber;
    private final float player1Damage;
    private final float player2Damage;
    private final String winner;
    private final String fightType;


    @JsonGetter("roundNumber")
    public int getRoundNumber() {
        return roundNumber;
    }

    @JsonGetter("player1Damage")
    public float getPlayer1Damage() {
        return player1Damage;
    }

    @JsonGetter("player2Damage")
    public float getPlayer2Damage() {
        return player2Damage;
    }

    @JsonGetter("winner")
    public String getWinner() {
        return winner;
    }

    @JsonGetter("fightType")
    public String getFightType() {
        return fightType;
    }

    public RoundResult(int roundNumber, float player1Damage, float player2Damage, String winner, String fightType) {
        this.roundNumber = roundNumber;
        this.player1Damage = player1Damage;
        this.player2Damage = player2Damage;
        this.winner = winner;
        this.fightType = fightType;
    }
}
