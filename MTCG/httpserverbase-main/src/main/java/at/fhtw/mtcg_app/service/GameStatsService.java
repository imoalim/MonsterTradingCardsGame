package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.*;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.GameStatRepo;
import at.fhtw.mtcg_app.persistence.repository.GameStatRepoImpl;

import java.util.List;

public class GameStatsService extends AbstractService {
    private final GameStatRepo gameStat;

    public GameStatsService() {
        this.gameStat = new GameStatRepoImpl(new UnitOfWork());
    }

    public Response showStats(Request request) {
        try {
            UserStats userStats;
            userStats = gameStat.getUserStats(request);
            if (userStats != null) {
                String userStatsToJson = this.convertToJson(userStats);
                String responseContent = "{ \"Stats\": " + userStatsToJson + " }";
                return new Response(HttpStatus.OK, ContentType.JSON, responseContent);
            }
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "Authentication failed" -> new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, e.getMessage());
                case "User not found" -> new Response(HttpStatus.NOT_FOUND, ContentType.JSON, e.getMessage());
                default -> new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
            };
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Unexpected error");
    }

    public Response showScoreboard(Request request) {
        try {
            List<UserStats> userStats;
            userStats = gameStat.getScoreboard(request);
            if (userStats != null) {
                String userStatsToJson = this.convertToJson(userStats);
                String responseContent = "{ \"Scoreboard\": " + userStatsToJson + " }";
                return new Response(HttpStatus.OK, ContentType.JSON, responseContent);
            }
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "Authentication failed" -> new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, e.getMessage());
                case "User not found" -> new Response(HttpStatus.NOT_FOUND, ContentType.JSON, e.getMessage());
                default -> new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
            };
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Unexpected error");
    }
}
