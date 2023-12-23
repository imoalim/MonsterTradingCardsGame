package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.SessionRepository;
import at.fhtw.mtcg_app.persistence.repository.SessionRepositoryImpl;

import java.io.IOException;
import java.sql.SQLException;

public class SessionService extends AbstractService {

    private final SessionRepository sessionRepository;

    public SessionService() {
        sessionRepository = new SessionRepositoryImpl(new UnitOfWork());
    }

    public Response checkLogin(Request request) {
        try {
            User newUser = this.getObjectMapper().readValue(request.getBody(), User.class);

            if (sessionRepository.islogged(newUser)) {
                return new Response(HttpStatus.OK, ContentType.JSON, " User login successful");
            } else {
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "Invalid username/password provided");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
