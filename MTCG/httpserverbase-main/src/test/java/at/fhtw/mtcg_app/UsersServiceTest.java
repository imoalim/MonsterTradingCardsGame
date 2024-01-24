package at.fhtw.mtcg_app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.model.UserData;
import at.fhtw.mtcg_app.persistence.repository.UserRepository;
import at.fhtw.mtcg_app.service.ExceptionHandler;
import at.fhtw.mtcg_app.service.UsersService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class UsersServiceTest {

    @Test
    void getUser_ValidUsername_ReturnsOkResponse() throws Exception {
        // Arrange
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UsersService usersService = new UsersService();
        usersService.setUserRepositoryForTesting(userRepositoryMock);

        Request request = mock(Request.class);
        String username = "testUser";
        UserData userData = new UserData();
        when(userRepositoryMock.findByUsername(eq(username), any(Request.class))).thenReturn(userData);

        // Act
        Response response = usersService.getUser(username, request);

        // Assert
        assertEquals(HttpStatus.OK.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
    }

    @Test
    void addUser_ValidUser_ReturnsCreatedResponse() throws ExceptionHandler, SQLException {
        // Arrange
        UserRepository userRepositoryMock = mock(UserRepository.class);
        UsersService usersService = new UsersService();
        usersService.setUserRepositoryForTesting(userRepositoryMock);

        Request request = mock(Request.class);
        String requestBody = "{ \"username\": \"testUser\", \"password\": \"testPassword\" }";
        when(request.getBody()).thenReturn(requestBody);


        when(userRepositoryMock.createUser(any(User.class))).thenReturn(true);

        // Act
        Response response = usersService.addUser(request);

        // Assert
        assertEquals(HttpStatus.CREATED.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
    }
}
