package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.UnitOfWork;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final UnitOfWork unitOfWork;

    public UserRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
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
        PreparedStatement statement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE username = ?");
        statement.setString(1, username);

        ResultSet rs = statement.executeQuery();

        User user = new User();
        while (rs.next()) {
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
        }
        rs.close();
        return user;
    }


    @Override
    public User createUser(User newUser) throws SQLException {
        try {
            List<User> userList = this.unitOfWork.readAllUsersFromDB("users");

            boolean userExists = userList.stream()
                    .anyMatch(user -> user.getUsername().equals(newUser.getUsername()));

            if (userExists) {
                newUser.setUserExists(true);
            } else {
                try (PreparedStatement statement = this.unitOfWork.prepareStatement("INSERT INTO public.users (username, password) VALUES (?, ?)")) {
                    statement.setString(1, newUser.getUsername());
                    statement.setString(2, newUser.getPassword());
                    statement.executeUpdate();
                    this.unitOfWork.commitTransaction();
                }
            }
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            throw e;
        }
        return newUser;
    }

}

