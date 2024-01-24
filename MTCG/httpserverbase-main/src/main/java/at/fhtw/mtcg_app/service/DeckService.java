package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.Deck;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.DeckRepo;
import at.fhtw.mtcg_app.persistence.repository.DeckRepoImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class DeckService extends AbstractService {
    private DeckRepo deckRepo;

    public DeckService() {
        deckRepo = new DeckRepoImpl(new UnitOfWork());
    }
    public void setDeckRepoForTesting(DeckRepo deckRepoMock) {
        this.deckRepo = deckRepoMock;
    }

    public Response showConfiguredDeck(Request request) {
        try {
            List<Deck> decks = deckRepo.getConfiguredDeck(request);
            if (decks.isEmpty()) {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "The request was fine, but the deck doesn't have any cards");
            } else {
                String deckToJson = this.convertToJson(decks);
                String responseContent = "{ \"The deck has cards, the response contains these\n" +
                        "\n\": " + deckToJson + " }";
                return new Response(HttpStatus.OK, ContentType.JSON, responseContent);
            }
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "Authentication failed" -> new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, e.getMessage());
                default -> new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
            };
        }
    }

    public Response configureDeck(Request request) {
        try {
            List<String> ids = convertFromJson(request.getBody(), new TypeReference<>() {
            });
            if (!deckRepo.configureDeck(request, ids))
                return new Response(HttpStatus.CONFLICT, ContentType.JSON, "Configure failed");
            else {
                return new Response(HttpStatus.OK, ContentType.JSON, "The deck has been successfully configured");
            }
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "Authentication failed" -> new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, e.getMessage());
                case "User didn't acquire cards" ->
                        new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "At least one of the provided cards does not belong to the user or is not available.");
                case "Didn't include the required amount of cards" ->
                        new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, e.getMessage());
                default -> new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
            };
        }
    }


}
