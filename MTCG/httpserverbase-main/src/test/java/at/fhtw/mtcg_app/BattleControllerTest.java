package at.fhtw.mtcg_app;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.controller.BattleController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BattleControllerTest {

    @Test
    void handleRequest_PostRequestWithValidPath_CallsProcessRequests() {
        // Arrange
        Request mockRequest = mock(Request.class);
        when(mockRequest.getMethod()).thenReturn(Method.POST);
        when(mockRequest.getPathname()).thenReturn("/battles");

        BattleController battleController = spy(new BattleController());
        doReturn(new Response(HttpStatus.OK, ContentType.JSON, "Response")).when(battleController).processRequests();

        // Act
        Response response = battleController.handleRequest(mockRequest);

        // Assert
        verify(battleController, times(1)).processRequests();
        assertEquals(HttpStatus.OK.code, response.getStatus());
    }


    @Test
    void handleRequest_GetRequest_ReturnsNotImplementedResponse() {
        // Arrange
        Request mockRequest = mock(Request.class);
        when(mockRequest.getMethod()).thenReturn(Method.GET);

        BattleController battleController = new BattleController();

        // Act
        Response response = battleController.handleRequest(mockRequest);

        // Assert
        assertEquals(HttpStatus.NOT_IMPLEMENTED.code, response.getStatus());
    }

}
