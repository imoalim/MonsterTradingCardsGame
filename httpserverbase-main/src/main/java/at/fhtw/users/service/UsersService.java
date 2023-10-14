package at.fhtw.users.service;


import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;

public class UsersService extends at.fhtw.users.service.UserAbstractService {
    public UsersService() {

    }
    // GET /users
    public Response getUsers() {
        return new Response(HttpStatus.NOT_IMPLEMENTED);
    }
    // GET /users/:id
    public Response getUser(String id) {
        return new Response(HttpStatus.NOT_IMPLEMENTED);
    }
    // POST /users
    public Response addUser(Request request) {
        return new Response(HttpStatus.NOT_IMPLEMENTED);
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
