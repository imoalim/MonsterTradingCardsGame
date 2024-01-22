package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.UserStats;

import java.sql.SQLException;
import java.util.List;

public interface GameStatRepo {
    UserStats getUserStats(Request request) throws Exception;

    List<UserStats> getScoreboard(Request request) throws Exception;

}
