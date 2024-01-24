package at.fhtw.mtcg_app;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.controller.DeckController;
import at.fhtw.mtcg_app.service.DeckService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeckControllerTest {

    @Test
    void handleRequest_WhenGetMethod_ReturnsShowConfiguredDeckResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.GET);

        DeckService deckServiceMock = mock(DeckService.class);
        when(deckServiceMock.showConfiguredDeck(requestMock)).thenReturn(
                new Response(HttpStatus.OK, ContentType.JSON, "Show Configured Deck Response")
        );

        DeckController deckController = new DeckController();
        deckController.setDeckServiceForTesting(deckServiceMock);

        // Act
        Response response = deckController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("Show Configured Deck Response", response.getContent());
    }

    @Test
    void handleRequest_WhenPutMethod_ReturnsConfigureDeckResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.PUT);

        DeckService deckServiceMock = mock(DeckService.class);
        when(deckServiceMock.configureDeck(requestMock)).thenReturn(
                new Response(HttpStatus.OK, ContentType.JSON, "Configure Deck Response")
        );

        DeckController deckController = new DeckController();
        deckController.setDeckServiceForTesting(deckServiceMock);

        // Act
        Response response = deckController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("Configure Deck Response", response.getContent());
    }

    @Test
    void handleRequest_WhenNotGetMethodNorPutMethod_ReturnsNotImplementedResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.POST);

        DeckController deckController = new DeckController();

        // Act
        Response response = deckController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.NOT_IMPLEMENTED.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("[]", response.getContent());
    }
}

