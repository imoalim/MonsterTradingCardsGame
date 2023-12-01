package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.utils.DBUtils;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findbyId(String id) {
        return null;
    }

    @Override
    public Collection<User> findAllUsers() {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        try (Connection connection = DBUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();

            User user = new User();
            while (rs.next()) {
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            rs.close();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Fehler bei der Datenbankverbindung: " + e.getMessage(), e);
        }
    }

    @Override
    public User createUser(User newUser) {
        try (Connection connection = DBUtils.getConnection()) {
            List<User> userList = DBUtils.readAllUsersFromDB(connection, "users");

            // Überprüfen, ob der Benutzername bereits existiert
            boolean userExists = userList.stream()
                    .anyMatch(user -> user.getUsername().equals(newUser.getUsername()));

            // Oder direkt den übergebenen Benutzer zurückgeben
            if (userExists) {
                newUser.setUserExists(true);
            } else {
                // Nur wenn der Benutzer nicht existiert, füge ihn zur Datenbank hinzu
                PreparedStatement statement = connection.prepareStatement("INSERT INTO public.users (username, password) VALUES (?, ?)");
                statement.setString(1, newUser.getUsername());
                statement.setString(2, newUser.getPassword());
                statement.executeUpdate();

                // Hier könntest du optional den gerade erstellten Benutzer aus der Datenbank abrufen und zurückgeben
                // Das hängt von deinen Anforderungen ab.

            }
            return newUser; // Rückgabe des vorhandenen Benutzers ohne ihn in die DB zu schreiben
        } catch (SQLException e) {
            throw new RuntimeException("Fehler bei der Datenbankverbindung: " + e.getMessage(), e);
        }
    }
}
