package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.Package;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.DBUtils;
import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.service.AuthHandler;
import at.fhtw.mtcg_app.service.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PackagesRepoImpl implements PackagesRepo {

    private int userId;
    private int coins;

    @Override
    public void createPackage(Package newPackage) throws SQLException {
        try (Connection connection = DBUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO packages (internal_id) VALUES (?)");
            statement.setString(1, newPackage.getInternalId());
            // Set other fields if necessary
            statement.executeUpdate();
        }
    }

    @Override
    public void createCard(Card card) throws SQLException {
        try (Connection connection = DBUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO cards (card_id, name, damage) VALUES (?, ?, ?)");
            statement.setString(1, card.getCardId());
            statement.setString(2, card.getName());
            statement.setDouble(3, card.getDamage());
            statement.executeUpdate();
        }
    }

    @Override
    public void associateCardToPackage(String internalId, String cardId) throws SQLException {
        try (Connection connection = DBUtils.getConnection()) {
            // Prepare a statement with a subquery to fetch the package_id using the internalId
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO PackageCards (package_id, card_id) VALUES ((SELECT package_id FROM packages WHERE internal_id = ?), ?)");

            statement.setString(1, internalId);  // Set the internalId to find the package
            statement.setString(2, cardId);      // Set the cardId

            statement.executeUpdate();
        }
    }

    @Override
    public boolean acquireCards(Request request) throws ExceptionHandler {
        try (Connection connection = DBUtils.getConnection()) {
            checkUserAuthentication(request, connection);

            int packageCost = 5; // Cost per package
            int maxPackages = coins / packageCost; // Maximum packages the user can acquire

            if (maxPackages <= 0) {
                throw new ExceptionHandler("Not enough coins");
            }
            deductUserCoins(connection, packageCost);
            acquirePackagesForUser(connection);
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage(), ex);
        }
    }

    private boolean usernameFromTokenExists(Connection connection) throws SQLException {

        List<User> userList = DBUtils.readSpecificUserFromDB(connection, "users", AuthHandler.getUsernameFromToken());


        if (!userList.isEmpty()) {
            userId = Integer.parseInt(userList.get(0).getId());
            coins = userList.get(0).getCoins();
            System.out.println("User ID: " + userId);

            return true;
        } else {

            return false;
        }
    }

    private void checkUserAuthentication(Request request, Connection connection) throws SQLException, ExceptionHandler {
        if (!AuthHandler.checkAuthFromHeader(request)) {
            throw new ExceptionHandler("Authentication failed");
        }
        if (!usernameFromTokenExists(connection)) {
            throw new ExceptionHandler("User not found");
        }
    }

    private void deductUserCoins(Connection connection, int totalCost) throws SQLException {
        PreparedStatement updateUsers = connection.prepareStatement(
                "UPDATE public.users SET coins = coins - ? WHERE user_id = ?");
        updateUsers.setInt(1, totalCost);
        updateUsers.setInt(2, userId);
        updateUsers.executeUpdate();
    }

    private void acquirePackagesForUser(Connection connection) throws SQLException {
        // Select a random available package
        PreparedStatement getOneRandomPackage = connection.prepareStatement(
                "SELECT package_id FROM packages WHERE status = 'available' ORDER BY RANDOM() LIMIT 1");

        ResultSet rs = getOneRandomPackage.executeQuery();
        if (rs.next()) {
            int packageId = rs.getInt("package_id");
            recordTransaction(connection, userId, packageId);
            // Update the status to 'assigned' or other status to indicate it's no longer available
            updatePackageStatus(connection, packageId, "assigned");
        } else {
            // No available packages
            throw new SQLException("No available packages found");
        }
    }

    private void recordTransaction(Connection connection, int userId, int packageId) throws SQLException {
        // Start transaction
        connection.setAutoCommit(false);

        // Record the transaction
        PreparedStatement insertTransaction = connection.prepareStatement(
                "INSERT INTO Transactions (user_id, package_id, status) VALUES (?, ?, ?)");
        insertTransaction.setInt(1, userId);
        insertTransaction.setInt(2, packageId);
        insertTransaction.setString(3, "success");
        insertTransaction.executeUpdate();

        connection.commit(); // Commit transaction
    }

    private void updatePackageStatus(Connection connection, int packageId, String newStatus) throws SQLException {
        // Update the package status
        PreparedStatement updatePackageStatus = connection.prepareStatement(
                "UPDATE packages SET status = ? WHERE package_id = ?");
        updatePackageStatus.setString(1, newStatus);
        updatePackageStatus.setInt(2, packageId);
        updatePackageStatus.executeUpdate();
    }


}
