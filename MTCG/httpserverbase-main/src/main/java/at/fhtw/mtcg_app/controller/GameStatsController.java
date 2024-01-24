package at.fhtw.mtcg_app.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcg_app.service.GameStatsService;

public class GameStatsController implements RestController {

    private  GameStatsService gameStatsService;


    public GameStatsController() {
        this.gameStatsService = new GameStatsService();
    }
    public void setGameStatsServiceForTesting(GameStatsService gameStatsServiceMock) {
        this.gameStatsService = gameStatsServiceMock;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET && request.getPathname().equals("/stats")) {
            return this.gameStatsService.showStats(request);
        }
        if (request.getMethod() == Method.GET && request.getPathname().equals("/scoreboard")) {
            return this.gameStatsService.showScoreboard(request);
        }
        return new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.JSON,
                "[]"
        );
    }


}
