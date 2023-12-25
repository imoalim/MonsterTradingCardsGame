package at.fhtw.mtcg_app.controller;


import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcg_app.service.UsersService;


public class UsersController implements RestController {
    private final UsersService usersService;

    public UsersController() {
        this.usersService = new UsersService();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET &&
                request.getPathParts().size() > 1) {
            return this.usersService.getUser(request.getPathParts().get(1));
        } else if (request.getMethod() == Method.POST) {
            return this.usersService.addUser(request);
        } else if (request.getMethod() == Method.PUT) {
            return this.usersService.updateUser(request);
        }

        return new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.JSON,
                "[]"
        );
    }
}
