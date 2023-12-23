package at.fhtw.mtcg_app.persistence.repository;
import at.fhtw.mtcg_app.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserRepository {
    User findbyId(String id);

    Collection<User> findAllUsers();

    User findByUsername(String username) throws SQLException;

    User createUser(User newuser) throws SQLException;
}
