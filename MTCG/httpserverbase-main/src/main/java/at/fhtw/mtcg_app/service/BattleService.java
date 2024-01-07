package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.UserStats;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.BattleRepo;
import at.fhtw.mtcg_app.persistence.repository.BattleRepoImpl;

import java.util.List;

public class BattleService extends AbstractService{
    private final BattleRepo battleRepo;

    public BattleService() {
        this.battleRepo = new BattleRepoImpl(new UnitOfWork());
    }

    public Response showStats(Request request) {
        try {
            UserStats userStats;
            userStats = battleRepo.getUserStats(request);
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
            userStats = battleRepo.getScoreboard(request);
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
