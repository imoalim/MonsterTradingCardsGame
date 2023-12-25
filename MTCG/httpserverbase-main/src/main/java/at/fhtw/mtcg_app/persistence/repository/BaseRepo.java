package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.UnitOfWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepo {
    protected UnitOfWork unitOfWork;

    public BaseRepo(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    protected List<User> readUsersFromDB(String tableName, String whereClause, List<Object> parameters) throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s", tableName, whereClause);

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(query)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getString("token"),
                            rs.getString("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("coins")
                    );
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading user from DB: " + e.getMessage(), e);
        }
        return userList;
    }

    protected List<User> readAllUsersFromDB(String tableName) throws SQLException {
        String whereClause = "1=1"; // This condition is always true, so it selects all rows
        List<Object> parameters = new ArrayList<>(); // No parameters needed for this query

        return readUsersFromDB(tableName, whereClause, parameters);
    }

    protected List<User> readSpecificUserFromDB(String tableName, String userName) throws SQLException {
        String whereClause = "username = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(userName);

        return readUsersFromDB(tableName, whereClause, parameters);
    }

}
