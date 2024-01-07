package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.model.UserData;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.service.AuthHandler;
import at.fhtw.mtcg_app.service.ExceptionHandler;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class UserRepositoryImpl extends BaseRepo implements UserRepository {
    public UserRepositoryImpl(UnitOfWork unitOfWork) {
        super(unitOfWork);
    }

    boolean checkIfUserExists(String userName) throws SQLException {
        List<User> userList = this.readSpecificUserFromDB("public.user", userName);

        return userList.size() != 0;
    }

    @Override
    public User findbyId(String id) {
        return null;
    }

    @Override
    public Collection<User> findAllUsers() {
        return null;
    }

    @Override
    public UserData findByUsername(String username, Request request) throws Exception {
        if(!this.checkIfUserExists(username)) throw new Exception("User doesn't exist");
        if(!AuthHandler.checkAuthFromHeader(request)) throw new Exception("Access token is missing or invalid");
        String tokenUsername = AuthHandler.getUsernameFromToken();
        if (tokenUsername == null || !tokenUsername.equals(username)) {
            throw new Exception("Username doesn't match request header");
        }
        Integer userId = this.getUserIdByUsername("public.user",username);
        PreparedStatement statement = this.unitOfWork.prepareStatement("SELECT * FROM public.user_data WHERE user_id = ?");
        statement.setInt(1, userId);

        ResultSet rs = statement.executeQuery();

        UserData userData = new UserData();
        while (rs.next()) {
            userData.setName(rs.getString("name"));
            userData.setBio(rs.getString("bio"));
            userData.setImage(rs.getString("image"));
        }
        rs.close();
        return userData;
    }


    @Override
    public boolean createUser(User newUser) throws SQLException, ExceptionHandler {
        try {

            if (checkIfUserExists(newUser.getUsername())) {
                throw new ExceptionHandler("User already exists");
            } else {
                try (PreparedStatement statement = this.unitOfWork.prepareStatement("INSERT INTO public.user (username, password) VALUES (?, ?)")) {
                    statement.setString(1, newUser.getUsername());
                    statement.setString(2, newUser.getPassword());
                    statement.executeUpdate();
                    this.unitOfWork.commitTransaction();
                    return true;
                }
            }
        } catch (Exception e) {
            this.unitOfWork.rollbackTransaction();
            throw e;
        }
    }

    @Override
    public boolean updateUser(String username, UserData newUserData) throws Exception {
        if (!checkIfUserExists(username)) throw new Exception("User not found");
        Integer userId = this.getUserIdByUsername("public.user",username);
        if (upsertUserData(userId, newUserData)) {
            userUpdated(userId);

            return true;
        }
        return false;
    }
    private boolean upsertUserData(Integer userId, UserData newUserData) throws SQLException {
        if (userDataExists(userId)) {
            return updateUserData(userId, newUserData);
        } else {
            return insertUserData(userId, newUserData);
        }
    }


    private boolean userDataExists(Integer userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM public.user_data WHERE user_id = ?";
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    private void userUpdated(Integer userId) throws SQLException {
        String sql = "UPDATE public.user SET user_data_updated = ? WHERE user_id = ?";
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setBoolean(1, true);
            statement.setInt(2, userId);
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            throw e;
        }
    }
    private boolean updateUserData(Integer userId, UserData newUserData) throws SQLException {

        String sqlUpdateUserStats = "UPDATE public.user_stats SET name = ? WHERE user_id = ?";
        try (PreparedStatement statementUpdateUserStats = this.unitOfWork.prepareStatement(sqlUpdateUserStats)) {
            statementUpdateUserStats.setString(1, newUserData.getName());
            statementUpdateUserStats.setInt(2, userId);
            statementUpdateUserStats.executeUpdate();
        }
        String sql = "UPDATE public.user_data SET name = ?, bio = ?, image = ? WHERE user_id = ?";
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setString(1, newUserData.getName());
            statement.setString(2, newUserData.getBio());
            statement.setString(3, newUserData.getImage());
            statement.setInt(4, userId);
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
            return true;
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            throw e;
        }
    }

    private boolean insertUserData(Integer userId, UserData newUserData) throws SQLException {
        String sqlInsertUserData = "INSERT INTO public.user_data (user_id, name, bio, image) VALUES (?, ?, ?, ?)";
        String sqlInsertUserStats = "INSERT INTO public.user_stats (name,user_id) VALUES (?,?)";

        try (PreparedStatement statementInsertUserData = this.unitOfWork.prepareStatement(sqlInsertUserData)) {
            statementInsertUserData.setInt(1, userId);
            statementInsertUserData.setString(2, newUserData.getName());
            statementInsertUserData.setString(3, newUserData.getBio());
            statementInsertUserData.setString(4, newUserData.getImage());
            statementInsertUserData.executeUpdate();

            try (PreparedStatement statementInsertUserStats = this.unitOfWork.prepareStatement(sqlInsertUserStats)) {
                statementInsertUserStats.setString(1, newUserData.getName());
                statementInsertUserStats.setInt(2, userId);

                statementInsertUserStats.executeUpdate();
            }

            this.unitOfWork.commitTransaction();
            return true;
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            throw e;
        }
    }

}

