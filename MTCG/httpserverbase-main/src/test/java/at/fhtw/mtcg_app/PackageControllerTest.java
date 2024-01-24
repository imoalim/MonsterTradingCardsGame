package at.fhtw.mtcg_app;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.controller.PackageController;
import at.fhtw.mtcg_app.service.PackageService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackageControllerTest {

    @Test
    void handleRequest_WhenPostMethodWithPathParts_ReturnsAcquireCardsResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.POST);
        when(requestMock.getPathParts()).thenReturn(List.of("path", "parts"));

        PackageService packageServiceMock = mock(PackageService.class);
        when(packageServiceMock.acquireCards(requestMock)).thenReturn(
                new Response(HttpStatus.OK, ContentType.JSON, "Acquire Cards Response")
        );

        PackageController packageController = new PackageController();
        packageController.setPackageServiceForTesting(packageServiceMock);

        // Act
        Response response = packageController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("Acquire Cards Response", response.getContent());
    }

    @Test
    void handleRequest_WhenPostMethodWithoutPathParts_ReturnsCreateCardsResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.POST);
        when(requestMock.getPathParts()).thenReturn(List.of());

        PackageService packageServiceMock = mock(PackageService.class);
        when(packageServiceMock.createCards(requestMock)).thenReturn(
                new Response(HttpStatus.CREATED, ContentType.JSON, "Create Cards Response")
        );

        PackageController packageController = new PackageController();
        packageController.setPackageServiceForTesting(packageServiceMock);

        // Act
        Response response = packageController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.CREATED.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("Create Cards Response", response.getContent());
    }

    @Test
    void handleRequest_WhenNotPostMethod_ReturnsNotImplementedResponse() {
        // Arrange
        Request requestMock = mock(Request.class);
        when(requestMock.getMethod()).thenReturn(Method.GET);

        PackageController packageController = new PackageController();

        // Act
        Response response = packageController.handleRequest(requestMock);

        // Assert
        assertEquals(HttpStatus.NOT_IMPLEMENTED.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
        assertEquals("[]", response.getContent());
    }
}
