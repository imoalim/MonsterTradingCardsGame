package at.fhtw.mtcg_app;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.Deck;
import at.fhtw.mtcg_app.persistence.repository.DeckRepo;
import at.fhtw.mtcg_app.service.DeckService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeckServiceTest {

    @Test
    void showConfiguredDeck_WhenDeckIsEmpty_ReturnsNotFoundResponse() throws Exception {
        // Arrange
        DeckRepo deckRepoMock = mock(DeckRepo.class);
        when(deckRepoMock.getConfiguredDeck(any(Request.class))).thenReturn(Collections.emptyList());

        DeckService deckService = new DeckService();
        deckService.setDeckRepoForTesting(deckRepoMock);

        Request requestMock = mock(Request.class);

        // Act
        Response response = deckService.showConfiguredDeck(requestMock);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("The request was fine, but the deck doesn't have any cards", response.getContent());
    }

    @Test
    void showConfiguredDeck_WhenDeckIsNotEmpty_ReturnsOkResponseWithDeckJson() throws Exception {
        // Arrange
        List<Deck> decks = List.of(new Deck(/* deck details here */));
        DeckRepo deckRepoMock = mock(DeckRepo.class);
        when(deckRepoMock.getConfiguredDeck(any(Request.class))).thenReturn(decks);

        DeckService deckService = new DeckService();
        deckService.setDeckRepoForTesting(deckRepoMock);

        Request requestMock = mock(Request.class);

        // Act
        Response response = deckService.showConfiguredDeck(requestMock);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
    }

}
