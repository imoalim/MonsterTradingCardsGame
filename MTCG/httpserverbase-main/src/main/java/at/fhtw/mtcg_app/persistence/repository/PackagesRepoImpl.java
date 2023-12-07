package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.Package;
import at.fhtw.mtcg_app.persistence.DBUtils;
import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.service.PackageService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PackagesRepoImpl implements PackagesRepo {


    private static final String ADMIN_TOKEN_PREFIX = "Bearer admin-mtcgToken";

    boolean isUserAdmin(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.equals(ADMIN_TOKEN_PREFIX);
    }

    @Override
    public Package createPackage(Request request, Package newPackage) {
        try (Connection connection = DBUtils.getConnection()) {
            if (isUserAdmin(request.getHeaderMap().getHeader("Authorization"))) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO public.packages (id, name, damage) VALUES (?, ?, ?)");
                statement.setString(1, newPackage.getId());
                statement.setString(2, newPackage.getName());
                statement.setDouble(3, newPackage.getDamage());
                statement.executeUpdate();
                newPackage.setAdmin(true);
            }
            return newPackage;
        } catch (SQLException e) {
            // Check if it's a duplicate key violation
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                // Handle the duplicate key violation and return the appropriate response
                PackageService.handleDuplicateKeyViolation(newPackage.getId());
            }

            // Rethrow other SQL exceptions
            throw new RuntimeException("Error creating package: " + e.getMessage(), e);
        }
    }
}