package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.Package;
import at.fhtw.mtcg_app.persistence.repository.PackagesRepo;
import at.fhtw.mtcg_app.persistence.repository.PackagesRepoImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;

public class PackageService extends AbstractService {
    private final PackagesRepo packagesRepo = new PackagesRepoImpl();

    public PackageService() {
    }

    public static void handleDuplicateKeyViolation(String duplicateKeyId) {
        // Return a custom Response object with status code 409 and a message
        // Log the error for debugging purposes
        System.err.println("Duplicate key violation for ID: " + duplicateKeyId);
        String responseContent = "{ \"message\": \"At least one card in the packages already exists\", \"duplicateKeyId\": \"" + duplicateKeyId + "\" }";
        new Response(HttpStatus.CONFLICT, ContentType.JSON, responseContent);
    }

    public Response createCards(Request request) {


        try {

            List<Package> newPackages = this.getObjectMapper().readValue(request.getBody(), new TypeReference<>() {
            });

            for (Package newPackage : newPackages) {
                packagesRepo.createPackage(request, newPackage);
            }
            if (request.getHeaderMap().getHeader("Authorization") == null ||
                    !request.getHeaderMap().getHeader("Authorization").equals("Bearer admin-mtcgToken")) {
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "Access token is missing or invalid");
            }

            if (newPackages.stream().anyMatch(Package::isAdmin)) {
                //String packageJson = this.convertToJson(newPackages.stream());
                //String responseContent = "{ \"\tPackage and cards successfully created\": " + packageJson + " }";
                return new Response(HttpStatus.CREATED, ContentType.JSON, "Package and cards successfully created");
            } else return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "Provided user is not \"admin\"");


        } catch (IOException e) {
            e.printStackTrace();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        }

    }

    //check if user is logged in
    //POST request


}