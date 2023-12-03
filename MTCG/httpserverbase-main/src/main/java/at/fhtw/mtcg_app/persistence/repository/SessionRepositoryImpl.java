package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SessionRepositoryImpl implements SessionRepository {

    @Override
    public boolean islogged(User newUser) {
        try (Connection connection = DBUtils.getConnection()) {
            List<User> userList = DBUtils.readAllUsersFromDB(connection, "users");

            boolean userExists = userList.stream()
                    .anyMatch(user -> user.getUsername().equals(newUser.getUsername()));

            if (userExists) {
                String userToken = newUser.getUsername() + "-mtcgToken";
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE public.users SET token = ? WHERE username = ?");
                statement.setString(1, userToken);
                statement.setString(2, newUser.getUsername());
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler bei der Datenbankverbindung: " + e.getMessage(), e);
        }
        return false;
    }
}
