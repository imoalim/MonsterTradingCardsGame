package at.fhtw.mtcg_app.persistence.repository;
import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.User;
import java.util.Collection;

public interface UserRepository {
    User findbyId(String id);
    Collection<User> findAllUsers();
    User findByUsername(String username);
    User createUser(User  newuser);
}
