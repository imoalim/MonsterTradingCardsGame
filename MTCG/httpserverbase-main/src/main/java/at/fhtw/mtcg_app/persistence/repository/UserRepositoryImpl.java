package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.model.UserData;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
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
    public User findByUsername(String username) throws SQLException {
        PreparedStatement statement = this.unitOfWork.prepareStatement("SELECT * FROM public.user WHERE username = ?");
        statement.setString(1, username);

        ResultSet rs = statement.executeQuery();

        User user = new User();
        while (rs.next()) {
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setId(rs.getString("user_id"));
        }
        rs.close();
        return user;
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
        String userId = findByUsername(username).getId();
        if (insertIntoUserData(userId, newUserData)) {
            userUpdated(userId);
            return true;
        }
        return false;
    }

    private void userUpdated(String userId) throws SQLException {
        String sql = "UPDATE public.user SET user_data_updated = ? WHERE user_id = ?";
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setBoolean(1, true);
            statement.setInt(2, Integer.parseInt(userId));
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            throw e;
        }
    }

    private boolean insertIntoUserData(String userId, UserData newUserData) throws SQLException {
        String sql = "INSERT INTO public.user_data (user_id,name, bio, image) VALUES (?,?,?,?)";
        try (PreparedStatement statement = this.unitOfWork.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(userId));
            statement.setString(2, newUserData.getName());
            statement.setString(3, newUserData.getBio());
            statement.setString(4, newUserData.getImage());
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
            return true;
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            throw e;
        }
    }
}

