package at.fhtw.mtcg_app.persistence.repository;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.model.UserData;
import at.fhtw.mtcg_app.service.ExceptionHandler;

import java.sql.SQLException;
import java.util.Collection;

public interface UserRepository {
    User findbyId(String id);

    Collection<User> findAllUsers();

    User findByUsername(String username) throws SQLException;

    boolean createUser(User newuser) throws SQLException, ExceptionHandler;

    boolean updateUser(String username, UserData newUserData) throws Exception;
}
