package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.Deck;

import java.util.List;

public interface DeckRepo {
    List<Deck> getConfiguredDeck(Request request) throws Exception;

    boolean configureDeck(Request request, List<String> ids) throws Exception;
}
