package at.fhtw.mtcg_app.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcg_app.service.BattleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleController implements RestController {

    private final BattleService battleService;
    private final List<Request> requestList;

    public BattleController() {
        this.battleService = new BattleService();
        this.requestList = Collections.synchronizedList(new ArrayList<>());
    }

    private Response processRequests() {
        synchronized (requestList) {
            if (requestList.size() == 2) {
                Request player1Request = requestList.get(0);
                Request player2Request = requestList.get(1);

                // Clear the list to prepare for new requests
                requestList.clear();

                // Process the requests in the BattleService
                return this.battleService.handleBattleRequest(player1Request, player2Request);
            }else return new Response(HttpStatus.OK, ContentType.JSON, "Waiting for Player 2...\n");
        }
    }


    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST && request.getPathname().equals("/battles")) {
            // Add the request to the synchronized list
            synchronized (requestList) {
                requestList.add(request);
            }
           return this.processRequests();
        }
        return new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.JSON,
                "[]"
        );
    }
}
