package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.Package;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.PackagesRepo;
import at.fhtw.mtcg_app.persistence.repository.PackagesRepoImpl;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PackageService extends AbstractService {
    private final PackagesRepo packagesRepo;

    public PackageService() {
        packagesRepo = new PackagesRepoImpl(new UnitOfWork());
    }

    public Response createCards(Request request) {
        boolean response = false;

        try {
            List<Card> cards = this.getObjectMapper().readValue(request.getBody(), new TypeReference<>() {
            });
            if (!AuthHandler.isUserAdmin(request.getHeaderMap().getHeader("Authorization"))) {
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "Provided user is not \"admin\"");
            }
            Package newPackage = new Package();
            newPackage.setInternalId(UUID.randomUUID().toString());
            newPackage.setCards(cards);
            packagesRepo.createPackage(newPackage);
            for (Card card : newPackage.getCards()) {
                packagesRepo.createCard(card); // Create each card
                packagesRepo.associateCardToPackage(newPackage.getInternalId(), card.getCardId());
                response = true;
            }
            if (!response)
                return new Response(HttpStatus.CONFLICT, ContentType.JSON, "Failed to create packages and cards");
            return new Response(HttpStatus.CREATED, ContentType.JSON, "Package and cards successfully created");
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Response acquireCards(Request request) {
        try {
            if (packagesRepo.acquireCards(request)) {
                return new Response(HttpStatus.OK, ContentType.JSON, "Cards Acquired");
            }
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "Authentication failed" -> new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, e.getMessage());
                case "User not found" -> new Response(HttpStatus.NOT_FOUND, ContentType.JSON, e.getMessage());
                case "Not enough coins", "No available packages found" ->
                        new Response(HttpStatus.FORBIDDEN, ContentType.JSON, e.getMessage());
                default -> new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
            };
        }
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "Unexpected error");
    }


    //check if user is logged in
    //POST request


}