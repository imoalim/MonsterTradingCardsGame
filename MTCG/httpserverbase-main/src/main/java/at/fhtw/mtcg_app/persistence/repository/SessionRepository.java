package at.fhtw.mtcg_app.persistence.repository;


import at.fhtw.mtcg_app.model.User;

import java.sql.SQLException;


public interface SessionRepository {
    boolean islogged(User user) throws SQLException;
}
