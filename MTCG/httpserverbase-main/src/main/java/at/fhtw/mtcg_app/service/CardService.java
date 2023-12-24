package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.CardsRepo;
import at.fhtw.mtcg_app.persistence.repository.CardsRepoImpl;

import java.util.List;

public class CardService extends AbstractService {
    private final CardsRepo cardsRepo;
    AuthHandler authHandler = new AuthHandler();

    public CardService() {
        cardsRepo = new CardsRepoImpl(new UnitOfWork());
    }

    public Response showUserCards(Request request) {
        try {
            if (!AuthHandler.checkAuthFromHeader(request)) {
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "\t\n" +
                        "Access token is missing or invalid");
            }
            String userName = AuthHandler.getUsernameFromToken();
            List<Card> newCard = cardsRepo.getUserCards(userName);
            if (newCard.size() != 0) {
                String cardsToJson = this.convertToJson(newCard);
                String responseContent = "{ \"Acquired cards\": " + cardsToJson + " }";
                return new Response(HttpStatus.OK, ContentType.JSON, responseContent);
            } else {
                return new Response(HttpStatus.CONFLICT, ContentType.JSON, "User has no cards");
            }
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
        }
    }
}
