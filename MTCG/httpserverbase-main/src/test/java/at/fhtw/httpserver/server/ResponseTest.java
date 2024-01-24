package at.fhtw.httpserver.server;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {
    @Test
    void get_EmptyContent_ReturnsValidResponseString() {
        // Arrange
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ContentType contentType = ContentType.HTML;
        Response response = new Response(httpStatus, contentType, "");

        // Act
        String responseString = response.get();

        // Assert
        assertNotNull(responseString);
        assertTrue(responseString.contains("HTTP/1.1 " + httpStatus.code + " " + httpStatus.message));
        assertTrue(responseString.contains("Content-Type: " + contentType.type));
        assertTrue(responseString.endsWith("\r\n\r\n"));
    }

    @Test
    void get_NullContentType_ReturnsValidResponseString() {
        // Arrange
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ContentType contentType = null;
        String content = "Internal Server Error";
        Response response = new Response(httpStatus, contentType, content);

        // Act
        String responseString = response.get();

        // Assert
        assertNotNull(responseString);
        assertTrue(responseString.contains("HTTP/1.1 " + httpStatus.code + " " + httpStatus.message));
        assertTrue(responseString.endsWith("\r\n\r\n" + content));
    }

    @Test
    void get_NullContent_ReturnsValidResponseString() {
        // Arrange
        HttpStatus httpStatus = HttpStatus.OK;
        ContentType contentType = ContentType.JSON;
        String content = null;
        Response response = new Response(httpStatus, contentType, content);

        // Act
        String responseString = response.get();

        // Assert
        assertNotNull(responseString);
        assertTrue(responseString.contains("HTTP/1.1 " + httpStatus.code + " " + httpStatus.message));
        assertTrue(responseString.contains("Content-Type: " + contentType.type));
        assertTrue(responseString.endsWith("\r\n\r\n"));
    }

}

