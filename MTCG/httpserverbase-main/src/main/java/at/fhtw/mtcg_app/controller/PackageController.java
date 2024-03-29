package at.fhtw.mtcg_app.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcg_app.service.PackageService;

public class PackageController implements RestController {

    private PackageService packageService;

    public PackageController() {
        this.packageService = new PackageService();
    }
    public void setPackageServiceForTesting(PackageService packageServiceMock) {
        this.packageService = packageServiceMock;
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST &&
                request.getPathParts().size() > 1) {
            return this.packageService.acquireCards(request);
        } else if (request.getMethod() == Method.POST) {
            return this.packageService.createCards(request);
        }
        return new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.JSON,
                "[]"
        );
    }


}
