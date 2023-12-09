package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.Package;
import at.fhtw.mtcg_app.persistence.repository.PackagesRepo;
import at.fhtw.mtcg_app.persistence.repository.PackagesRepoImpl;
import at.fhtw.sampleapp.service.AbstractService;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;

public class PackageService extends AbstractService {
    private final PackagesRepo packagesRepo = new PackagesRepoImpl();

    public PackageService() {
    }

    public Response createCards(Request request) {

        try {

            List<Package> newPackages = this.getObjectMapper().readValue(request.getBody(), new TypeReference<>() {
            });

            for (Package newPackage : newPackages) {
                packagesRepo.createPackage(request, newPackage);
            }
            return new Response(HttpStatus.ACCEPTED, ContentType.JSON, "test");

        } catch (IOException e) {
            e.printStackTrace();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Fehler beim Parsen des Benutzers");
        }

    }

    //check if user is logged in
    //POST request


}