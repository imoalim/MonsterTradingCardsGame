package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.Player;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.service.AuthHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BattleRepoImpl extends BaseRepo implements BattleRepo {
    public BattleRepoImpl(UnitOfWork unitOfWork) {
        super(unitOfWork);
    }
    Integer userId = null;

    @Override
    public void splitCards(Request request, List<Card> monsterCards, List<Card> spellCards) throws SQLException {
        AuthHandler.checkAuthFromHeader(request);
        userId = this.getUserIdByUsername("public.user", AuthHandler.getUsernameFromToken());

        String sql = "SELECT c.card_id, c.name, c.damage " +
                "FROM transaction t " +
                "JOIN package_card pc ON t.package_id = pc.package_id " +
                "JOIN card c ON pc.card_id = c.card_id " +
                "WHERE t.user_id = ?";

        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String cardId = resultSet.getString("card_id");
                    String name = resultSet.getString("name");
                    double damage = resultSet.getDouble("damage");

                    Card card = new Card(cardId, name, damage);

                    // Classify the card into monster or spell based on the name
                    if (isSpell(name)) {
                        spellCards.add(card);
                    } else {
                        monsterCards.add(card);
                    }
                }
            }
        }
    }


    @Override
    public String getPlayerName(Player player) {
        String playerName = null;

        // Check if userId is available
        if (player.getPlayerId() != 0) {
            String sql = "SELECT name FROM user_data WHERE user_id = ?";

            try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
                statement.setInt(1, player.getPlayerId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        playerName = resultSet.getString("name");
                    }
                }
            } catch (SQLException e) {
                // Handle SQL exception
                e.printStackTrace(); // Handle more gracefully in production
            }
        }

        return playerName;
    }

    @Override
    public void playerWon(Player player) {

        String sqlUpdate = "UPDATE user_stats SET wins = wins + 1, elo = elo + 3 WHERE user_id = ?";

        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sqlUpdate)) {
            statement.setInt(1, player.getPlayerId());
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playerLoss(Player player) {
        String sqlUpdate = "UPDATE user_stats SET losses = losses + 1, elo = elo - 5 WHERE user_id = ?";

        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sqlUpdate)) {
            statement.setInt(1, player.getPlayerId());
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    private boolean isSpell(String cardName) {
        return cardName.contains("Spell");
    }


}
