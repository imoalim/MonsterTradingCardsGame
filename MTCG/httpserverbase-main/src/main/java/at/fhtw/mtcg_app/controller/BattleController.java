package at.fhtw.mtcg_app.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcg_app.service.BattleService;

public class BattleController implements RestController {
    private final BattleService battleService;

    public BattleController() {
        this.battleService = new BattleService();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET && request.getPathname().equals("/stats")) {
            return this.battleService.showStats(request);
        }
        if (request.getMethod() == Method.GET && request.getPathname().equals("/scoreboard")) {
            return this.battleService.showScoreboard(request);
        }
        if (request.getMethod() == Method.POST) {
            //return this.battleService.startBattle(request);
        }
        return new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.JSON,
                "[]"
        );
    }
}
