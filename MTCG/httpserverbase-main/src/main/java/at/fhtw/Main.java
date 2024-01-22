package at.fhtw;

import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;
import at.fhtw.mtcg_app.controller.*;
import at.fhtw.sampleapp.controller.EchoController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/users", new UsersController());
        router.addService("/sessions", new SessionController());
        router.addService("/packages", new PackageController());
        router.addService("/transactions", new PackageController());
        router.addService("/cards", new CardsController());
        router.addService("/deck", new DeckController());
        router.addService("/stats", new GameStatsController());
        router.addService("/scoreboard", new GameStatsController());
        router.addService("/battles", new BattleController());
        router.addService("/echo", new EchoController());

        return router;
    }
}
