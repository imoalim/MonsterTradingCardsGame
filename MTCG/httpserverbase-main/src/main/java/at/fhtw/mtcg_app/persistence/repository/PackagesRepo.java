package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.Package;


public interface PackagesRepo {

    Package createPackage(Request request, Package newPackage);
}
