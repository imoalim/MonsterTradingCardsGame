package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.Package;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.service.AuthHandler;
import at.fhtw.mtcg_app.service.ExceptionHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PackagesRepoImpl extends BaseRepo implements PackagesRepo {

    private int userId;
    private int coins;

    public PackagesRepoImpl(UnitOfWork unitOfWork) {
        super(unitOfWork);
    }


    @Override
    public void createPackage(Package newPackage) throws SQLException {
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(
                "INSERT INTO package (internal_id) VALUES (?)")) {
            statement.setString(1, newPackage.getInternalId());
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
        }
    }

    @Override
    public void createCard(Card card) throws SQLException {
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(
                "INSERT INTO card (card_id, name, damage) VALUES (?, ?, ?)")) {
            statement.setString(1, card.getCardId());
            statement.setString(2, card.getName());
            statement.setDouble(3, card.getDamage());
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
        }
    }

    @Override
    public void associateCardToPackage(String internalId, String cardId) throws SQLException {
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(
                "INSERT INTO package_card (package_id, card_id) VALUES ((SELECT package_id FROM package WHERE internal_id = ?), ?)")
        ) {
            statement.setString(1, internalId);
            statement.setString(2, cardId);
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
        }
    }


    @Override
    public boolean acquireCards(Request request) throws ExceptionHandler, SQLException {

        checkUserAuthentication(request);

        int packageCost = 5; // Cost per package
        int maxPackages = coins / packageCost; // Maximum packages the user can acquire

        if (maxPackages <= 0) {
            throw new ExceptionHandler("Not enough coins");
        }
        acquirePackagesForUser();
        deductUserCoins(packageCost);
        return true;
    }

    private boolean usernameFromTokenExists() throws SQLException {

        List<User> userList = this.readSpecificUserFromDB("public.user", AuthHandler.getUsernameFromToken());


        if (!userList.isEmpty()) {
            userId = Integer.parseInt(userList.get(0).getId());
            coins = userList.get(0).getCoins();
            return true;
        } else {

            return false;
        }
    }

    private void checkUserAuthentication(Request request) throws SQLException, ExceptionHandler {
        if (!AuthHandler.checkAuthFromHeader(request)) {
            throw new ExceptionHandler("Authentication failed");
        }
        if (!usernameFromTokenExists()) {
            throw new ExceptionHandler("User not found");
        }
    }

    private void deductUserCoins(int totalCost) throws SQLException {
        PreparedStatement updateUsers = this.unitOfWork.prepareStatement(
                "UPDATE public.user SET coins = coins - ? WHERE user_id = ?");
        updateUsers.setInt(1, totalCost);
        updateUsers.setInt(2, userId);
        updateUsers.executeUpdate();
        this.unitOfWork.commitTransaction();
    }

    private void acquirePackagesForUser() throws SQLException {

        try (PreparedStatement getOneRandomPackage = this.unitOfWork.prepareStatement(
                "SELECT package_id FROM package WHERE status = 'available' ORDER BY RANDOM() LIMIT 1"); ResultSet rs = getOneRandomPackage.executeQuery()) {

            if (rs.next()) {
                int packageId = rs.getInt("package_id");
                recordTransaction(userId, packageId);
                updatePackageStatus(packageId, "assigned");
                this.unitOfWork.commitTransaction();
            } else {
                throw new SQLException("No available packages found");
            }
        } catch (SQLException ex) {
            this.unitOfWork.rollbackTransaction();
            throw ex;
        }
    }


    private void recordTransaction(int userId, int packageId) throws SQLException {
        // Record the transaction
        PreparedStatement insertTransaction = this.unitOfWork.prepareStatement(
                "INSERT INTO transaction (user_id, package_id, status) VALUES (?, ?, ?)");
        insertTransaction.setInt(1, userId);
        insertTransaction.setInt(2, packageId);
        insertTransaction.setString(3, "success");
        insertTransaction.executeUpdate();
    }

    private void updatePackageStatus(int packageId, String newStatus) throws SQLException {
        // Update the package status
        PreparedStatement updatePackageStatus = this.unitOfWork.prepareStatement(
                "UPDATE package SET status = ? WHERE package_id = ?");
        updatePackageStatus.setString(1, newStatus);
        updatePackageStatus.setInt(2, packageId);
        updatePackageStatus.executeUpdate();
    }


}
