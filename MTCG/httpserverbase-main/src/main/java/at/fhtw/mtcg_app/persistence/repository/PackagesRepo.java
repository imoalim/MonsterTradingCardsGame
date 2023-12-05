package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.Package;

import java.sql.Connection;

public interface PackagesRepo {
    Package createPackage(Request request, Package newPackage);
}
