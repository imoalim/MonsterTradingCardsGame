package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"roundNumber", "player1Damage", "player2Damage", "winner", "fightType"})
public record RoundResult(
        @JsonAlias({"roundNumber", "RoundNum"}) @JsonGetter("roundNumber") int roundNumber,
        @JsonAlias({"player1Damage", "Player1Dmg"}) @JsonGetter("player1Damage") float player1Damage,
        @JsonAlias({"player2Damage", "Player2Dmg"}) @JsonGetter("player2Damage") float player2Damage,
        @JsonAlias({"winner", "WinningPlayer"}) @JsonGetter("winner") String winner,
        @JsonAlias({"fightType", "TypeOfFight"}) @JsonGetter("fightType") String fightType) {

    public RoundResult {
    }
}
