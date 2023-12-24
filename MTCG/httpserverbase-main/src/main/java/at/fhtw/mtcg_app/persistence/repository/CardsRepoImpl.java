package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.persistence.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardsRepoImpl implements CardsRepo {
    private final UnitOfWork unitOfWork;

    public CardsRepoImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }


    @Override
    public List<Card> getUserCards(String username) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT c.* FROM cards c " +
                "JOIN packagecards pc ON c.card_id = pc.card_id " +
                "JOIN transactions t ON pc.package_id = t.package_id " +
                "JOIN users u ON t.user_id = u.user_id " + // Join with users table
                "WHERE u.username = ?"; // Filter on username

        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setString(1, username); // Set the username parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Card card = new Card(
                            resultSet.getString("card_id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("damage")
                    );
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user cards", e);
        }
        return cards;
    }

}
