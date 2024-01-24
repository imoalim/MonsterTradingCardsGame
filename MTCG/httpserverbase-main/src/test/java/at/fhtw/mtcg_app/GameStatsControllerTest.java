package at.fhtw.mtcg_app;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.controller.GameStatsController;
import at.fhtw.mtcg_app.service.GameStatsService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameStatsControllerTest {

    @Test
    void handleRequest_WhenGetMethodAndPathIsStats_ReturnsShowStatsResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.GET);
        when(requestMock.getPathname()).thenReturn("/stats");

        GameStatsService gameStatsServiceMock = mock(GameStatsService.class);
        when(gameStatsServiceMock.showStats(requestMock)).thenReturn(
                new Response(HttpStatus.OK, ContentType.JSON, "Show Stats Response")
        );

        GameStatsController gameStatsController = new GameStatsController();
        gameStatsController.setGameStatsServiceForTesting(gameStatsServiceMock);

        // Act
        Response response = gameStatsController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("Show Stats Response", response.getContent());
    }

    @Test
    void handleRequest_WhenGetMethodAndPathIsScoreboard_ReturnsShowScoreboardResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.GET);
        when(requestMock.getPathname()).thenReturn("/scoreboard");

        GameStatsService gameStatsServiceMock = mock(GameStatsService.class);
        when(gameStatsServiceMock.showScoreboard(requestMock)).thenReturn(
                new Response(HttpStatus.OK, ContentType.JSON, "Show Scoreboard Response")
        );

        GameStatsController gameStatsController = new GameStatsController();
        gameStatsController.setGameStatsServiceForTesting(gameStatsServiceMock);

        // Act
        Response response = gameStatsController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("Show Scoreboard Response", response.getContent());
    }

    @Test
    void handleRequest_WhenNotGetMethodNorPathStatsNorPathScoreboard_ReturnsNotImplementedResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.POST); // Assuming POST method for example

        GameStatsController gameStatsController = new GameStatsController();

        // Act
        Response response = gameStatsController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.NOT_IMPLEMENTED.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("[]", response.getContent());
    }
}

