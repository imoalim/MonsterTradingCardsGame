package at.fhtw.mtcg_app.service;


import at.fhtw.mtcg_app.model.UserData;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.UserRepository;
import at.fhtw.mtcg_app.persistence.repository.UserRepositoryImpl;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.User;


public class UsersService extends AbstractService {

    private final UserRepository userRepository;

    public UsersService() {
        userRepository = new UserRepositoryImpl(new UnitOfWork());
    }
    // GET /users/:id
    public Response getUser(String username, Request request) {
        UserData userData;
        try {
            userData = userRepository.findByUsername(username, request);
            String json = this.convertToJson(userData);
            return new Response(HttpStatus.OK, ContentType.JSON, json);
        } catch (Exception e) {
            if (e.getMessage().equals("Access token is missing or invalid"))
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, e.getMessage());
            if(e.getMessage().equals("Username doesn't match request header"))
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, e.getMessage());
            if(e.getMessage().equals("User doesn't exist"))
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, e.getMessage());
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Unexpected error");
    }

    // POST /users
    public Response addUser(Request request) {
        try {
            User newUser = this.getObjectMapper().readValue(request.getBody(), User.class);
            if (newUser.getUsername() == null || newUser.getPassword() == null) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Ungültige Benutzerdaten");
            }
            if (userRepository.createUser(newUser)) {
                String userJson = this.convertToJson(newUser);
                String responseContent = "{ \"user\": " + userJson + " }";
                return new Response(HttpStatus.CREATED, ContentType.JSON, responseContent);
            }
        } catch (Exception e) {
            if (e.getMessage().equals("User already exists"))
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, e.getMessage());
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Unexpected error");
    }

    public Response updateUser(Request request) {
        try {
            UserData newUserData = this.getObjectMapper().readValue(request.getBody(), UserData.class);
            if (!AuthHandler.checkAuthFromHeader(request))
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "\t\n" +
                        "Access token is missing or invalid");
            if (userRepository.updateUser(AuthHandler.getUsernameFromToken(), newUserData))
                return new Response(HttpStatus.OK, ContentType.JSON, "User successfully updated.");
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "User not found" -> new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "User not found");
                default -> new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
            };
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Unexpected error");
    }
}
