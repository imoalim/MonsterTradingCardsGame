package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.Player;
import at.fhtw.mtcg_app.model.RoundResult;

import java.sql.SQLException;
import java.util.List;

public interface BattleRepo {

    void splitCards(Request request, List<Card> monsterCards, List<Card> spellCards) throws SQLException;

    String getPlayerName(Player player);


    void playerWon(Player player1);

    void playerLoss(Player player1);

    Integer getUserId();
}
