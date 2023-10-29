package at.fhtw.users.service;


import at.fhtw.db.DbFunctions;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.users.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

public class UsersService extends AbstractUsers {
    public UsersService() {

    }
    final DbFunctions db = new DbFunctions();

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
            if (newUser.getId() <= 0 || newUser.getUsername() == null || newUser.getPassword() == null) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Ungültige Benutzerdaten");
            }
            // Daten in die Datenbank einfügen
            String insertSQL = "INSERT INTO public.users (id, username, password) VALUES (?, ?, ?)";
            try (Connection connection = db.connectToDatabase();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setInt(1, newUser.getId());
                preparedStatement.setString(2, newUser.getUsername());
                preparedStatement.setString(3, newUser.getPassword());
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
            } catch (SQLException e) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Fehler bei der Datenbankverbindung oder beim SQL-Befehl");
            }
        } catch (IOException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        }
    }


    // PUT /users/:id
    public Response updateUser(String id, Request request) {
        return new Response(HttpStatus.NOT_IMPLEMENTED);
    }
    // DELETE /users/:id
    public Response deleteUser(String id) {
        return new Response(HttpStatus.NOT_IMPLEMENTED);
    }
    // GET /users
    // gleich wie "public Response getUsers()" nur mittels Repository


}
