package at.fhtw.mtcg_app.service;


import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.UserRepository;
import at.fhtw.mtcg_app.persistence.repository.UserRepositoryImpl;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.User;

import java.io.IOException;
import java.sql.SQLException;

public class UsersService extends AbstractService {

    private final UserRepository userRepository;

    public UsersService() {
        userRepository = new UserRepositoryImpl(new UnitOfWork());
    }
    // GET /users/:id
    public Response getUser(String username) {
        User user;
        try {
            user = userRepository.findByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            System.out.println("There is no user" + username);
        }
        String json = this.convertToJson(user);
        return new Response(HttpStatus.OK, ContentType.JSON, json);
    }

    // POST /users
    public Response addUser(Request request) {
        try {
            User newUser = this.getObjectMapper().readValue(request.getBody(), User.class);
            newUser  =  userRepository.createUser(newUser);
            // Überprüfe, ob die Benutzerdaten gültig sind
            if (newUser.getUsername() == null || newUser.getPassword() == null) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Ungültige Benutzerdaten");
            }
                if (newUser.isUserExists()) {
                    return new Response(HttpStatus.CONFLICT, ContentType.JSON, "Benutzer mit demselben Benutzernamen existiert bereits");
                } else {
                    //int rowsAffected = preparedStatement.executeUpdate();
                    //if (rowsAffected > 0) {
                    // Erfolgreich eingefügt
                    String userJson = this.convertToJson(newUser);
                    String responseContent = "{ \"user\": " + userJson + " }";
                    return new Response(HttpStatus.CREATED, ContentType.JSON, responseContent);
                } /*else {
                        // Fehler beim Einfügen
                        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Fehler beim Einfügen des Benutzers");
                    }
                }*/
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
/*

    // PUT /users/:id
    public Response updateUser(String username, Request request) {
        try {
            Connection connection = db.connectToDatabase();

            // Überprüfen, ob der Benutzername nicht null ist und nicht leer
            if (username == null || username.isEmpty()) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Ungültiger Benutzername");
            }

            // Überprüfen, ob der Benutzer mit dem angegebenen Benutzernamen existiert
            List<User> userList = db.readSpecificUserFromDB(connection, "users", username);
            if (userList.isEmpty()) {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "Benutzer nicht gefunden");
            }

            // Extrahieren des aktualisierten Passworts aus dem Anfrageobjekt
            User updatedUser = this.getObjectMapper().readValue(request.getBody(), User.class);
            String updatedPassword = updatedUser.getPassword();

            // Daten in die Datenbank aktualisieren
            String updateSQL = "UPDATE users SET password = ? WHERE username = ?";
            try (Connection conn = db.connectToDatabase();
                 PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                stmt.setString(1, updatedPassword);
                stmt.setString(2, username);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Erfolgreich aktualisiert
                    return new Response(HttpStatus.OK, ContentType.JSON, "Benutzerdaten aktualisiert");
                } else {
                    // Das Update war nicht erfolgreich
                    return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Fehler beim Aktualisieren der Benutzerdaten");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE /users/:id
    public Response deleteUser(String username) {
        if (username == null || username.isEmpty()) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Ungültiger Benutzername");
        }

        String deleteSQL = "DELETE FROM users WHERE username = ?";
        try (Connection conn = db.connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                return new Response(HttpStatus.NO_CONTENT); // Erfolgreich gelöscht, 204 No Content
            } else {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "Benutzer nicht gefunden");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

*/
}
