package at.fhtw.mtcg_app.service;


import at.fhtw.utils.DB_utils;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

public class UsersService extends AbstractUsers {
    public UsersService() {

    }
    final DB_utils db = new DB_utils();

    // GET /users
    public Response getUsers() {
        try (Connection connection = db.connectToDatabase()) {
            List<Users> userList = db.readAllUsersFromDB(connection, "users");
            String json = convertUserListToJson(userList);
            return new Response(HttpStatus.OK, ContentType.JSON, json);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler bei der Datenbankverbindung: " + e.getMessage(), e);
        }
    }


    // GET /users/:id
    public Response getUser(String username) {
        try (Connection connection = db.connectToDatabase()) {
            List<Users> userList = db.readSpecificUserFromDB(connection, "users", username);
            String json = convertUserListToJson(userList);
            if(json == null){
                System.out.println("There is no user" + username );
            }
            return new Response(HttpStatus.OK, ContentType.JSON, json);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler bei der Datenbankverbindung: " + e.getMessage(), e);
        }
    }

    private String convertUserListToJson(List<Users> userList) {
        try {
            return this.getObjectMapper().writeValueAsString(userList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    // POST /users
    public Response addUser(Request request) {
        try {
            Users newUser = this.getObjectMapper().readValue(request.getBody(), Users.class);

            // Überprüfe, ob die Benutzerdaten gültig sind
            if (newUser.getUsername() == null || newUser.getPassword() == null) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Ungültige Benutzerdaten");
            }
            // Daten in die Datenbank einfügen
            String insertSQL = "INSERT INTO public.users (username, password) VALUES ( ?, ?)";

            try (Connection connection = db.connectToDatabase();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, newUser.getUsername());
                preparedStatement.setString(2, newUser.getPassword());


                List<Users> userList = db.readAllUsersFromDB(connection, "users");
                // Überprüfen, ob der Benutzername bereits existiert
                boolean userExists = userList.stream()
                        .anyMatch(user -> user.getUsername().equals(newUser.getUsername()));

                if (userExists) {
                    return new Response(HttpStatus.CONFLICT, ContentType.JSON, "Benutzer mit demselben Benutzernamen existiert bereits");
                } else {
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Erfolgreich eingefügt
                        String userJson = this.getObjectMapper().writeValueAsString(newUser);
                        String responseContent = "{ \"user\": " + userJson + " }";
                        return new Response(HttpStatus.CREATED, ContentType.JSON, responseContent);
                    } else {
                        // Fehler beim Einfügen
                        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Fehler beim Einfügen des Benutzers");
                    }
                }
            } catch (SQLException e) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Fehler bei der Datenbankverbindung oder beim SQL-Befehl");
            }
        } catch (IOException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        }
    }


    // PUT /users/:id
    public Response updateUser(String username, Request request) {
        try {
            Connection connection = db.connectToDatabase();

            // Überprüfen, ob der Benutzername nicht null ist und nicht leer
            if (username == null || username.isEmpty()) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Ungültiger Benutzername");
            }

            // Überprüfen, ob der Benutzer mit dem angegebenen Benutzernamen existiert
            List<Users> userList = db.readSpecificUserFromDB(connection, "users", username);
            if (userList.isEmpty()) {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "Benutzer nicht gefunden");
            }

            // Extrahieren des aktualisierten Passworts aus dem Anfrageobjekt
            Users updatedUser = this.getObjectMapper().readValue(request.getBody(), Users.class);
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


}
