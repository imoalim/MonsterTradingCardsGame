package at.fhtw.users.service;


import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.users.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class UsersService extends AbstractUsers {
    public UsersService() {

    }
    // GET /users
    public Response getUsers() {
        System.out.println("getUser.");
        Users users = new Users(1,"admin", "admin");
        String Json = null;
        try{
            Json  = this.getObjectMapper().writeValueAsString(users);
            //System.out.println(Json);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return new Response(HttpStatus.OK, ContentType.JSON,  Json);
    }

    // GET /users/:id
    public Response getUser(String id) {
        return new Response(HttpStatus.NOT_IMPLEMENTED);
    }
    // POST /users
    public Response addUser(Request request) {
        try {
            // Benutzer aus den Daten im Request-Objekt erstellen.
            // Benutzerdaten aus dem Request-Body parsen.
            /*System.out.println("BODY" + request.getBody());
            System.out.println("CONTENT_TYPE" + request.getContentType());
            System.out.println("PARAMTER" + request.getParams());
            System.out.println("PATHNAME" + request.getPathname());
            System.out.println("METHOD"  + request.getMethod());
            System.out.println("UrlContent" + request.getUrlContent());
            System.out.println("SERVICE_ROUTE" + request.getServiceRoute());
            System.out.println("PATH_PARTS" + request.getPathParts());
            System.out.println("HEADER_MAP" + request.getHeaderMap());*/
            Users newUser = this.getObjectMapper().readValue(request.getBody(), Users.class);

            // neuen Benutzer erstellt. Du könntest ihn speichern oder verarbeiten.

            // Zum Beispiel:
            // userDatabase.save(newUser);
            System.out.println(newUser);
            // JSON-Antwort mit den Benutzerdetails
            String userJson = this.getObjectMapper().writeValueAsString(newUser);
            String responseContent = "{ \"user\": " + userJson + " }";

            return new Response(HttpStatus.CREATED, ContentType.JSON, responseContent);
            // return new Response(HttpStatus.CREATED, ContentType.JSON, userJson);


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
