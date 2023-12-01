package at.fhtw.mtcg_app.service;

import at.fhtw.mtcg_app.persistence.repository.SessionRepository;
import at.fhtw.mtcg_app.persistence.repository.SessionRepositoryImpl;
import at.fhtw.utils.DBUtils;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.User;

import java.io.IOException;

public class SessionService extends AbstractUsers {

    private SessionRepository sessionRepository = new SessionRepositoryImpl();

    public SessionService() {
    }

    final DBUtils dbUtils = new DBUtils();

    //check if user is logged in
    //POST request
   /* public Response checkLogin(Request request) {
        try {
            User newUser = this.getObjectMapper().readValue(request.getBody(), User.class);


        } catch (IOException e) {
            e.printStackTrace();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        }
    }
    */
}
