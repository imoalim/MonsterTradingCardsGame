package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.UnitOfWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepo {
    protected UnitOfWork unitOfWork;

    public BaseRepo(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    protected Integer getUserIdByUsername(String tableName, String userName) throws SQLException {
        String query = "SELECT user_id FROM " + tableName + " WHERE username = ?";
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(query)) {
            preparedStatement.setString(1, userName);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                } else {
                    return null; // Or handle the case where the user is not found
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching user ID from DB: " + e.getMessage(), e);
        }
    }


    protected List<User> readUsersFromDB(String tableName, String whereClause, List<Object> parameters) throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s", tableName, whereClause);

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(query)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getString("token"),
                            rs.getString("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("coins")
                    );
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading user from DB: " + e.getMessage(), e);
        }
        return userList;
    }

    protected List<User> readAllUsersFromDB(String tableName) throws SQLException {
        String whereClause = "1=1"; // This condition is always true, so it selects all rows
        List<Object> parameters = new ArrayList<>(); // No parameters needed for this query

        return readUsersFromDB(tableName, whereClause, parameters);
    }

    protected List<User> readSpecificUserFromDB(String tableName, String userName) throws SQLException {
        String whereClause = "username = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(userName);

        return readUsersFromDB(tableName, whereClause, parameters);
    }

    protected List<Card> getUserCards(String username) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT c.* FROM public.card c " +
                "JOIN public.package_card pc ON c.card_id = pc.card_id " +
                "JOIN public.transaction t ON pc.package_id = t.package_id " +
                "JOIN public.user u ON t.user_id = u.user_id " + // Join with users table
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
