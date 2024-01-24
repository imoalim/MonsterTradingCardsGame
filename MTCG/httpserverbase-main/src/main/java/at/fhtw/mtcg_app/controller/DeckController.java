package at.fhtw.mtcg_app.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcg_app.service.DeckService;

public class DeckController implements RestController {
    private DeckService deckService;

    public DeckController() {
        this.deckService = new DeckService();
    }
    public void setDeckServiceForTesting(DeckService deckServiceMock) {
        this.deckService = deckServiceMock;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            return this.deckService.showConfiguredDeck(request);
        } else if (request.getMethod() == Method.PUT) {
            return this.deckService.configureDeck(request);
        }
        return new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.JSON,
                "[]"
        );
    }


}

