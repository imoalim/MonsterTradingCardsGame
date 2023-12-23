package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SessionRepositoryImpl implements SessionRepository {
    private final UnitOfWork unitOfWork;

    public SessionRepositoryImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }


    @Override
    public boolean islogged(User newUser) throws SQLException {

        List<User> userList = this.unitOfWork.readAllUsersFromDB("users");

        boolean userExists = userList.stream()
                .anyMatch(user -> user.getUsername().equals(newUser.getUsername()));
        boolean passwordIsValid = userList.stream()
                .anyMatch(user -> user.getPassword().equals(newUser.getPassword()));

        if (userExists && passwordIsValid) {
            String userToken = newUser.getUsername() + "-mtcgToken";
            PreparedStatement statement = this.unitOfWork.prepareStatement(
                    "UPDATE public.users SET token = ? WHERE username = ?");
            statement.setString(1, userToken);
            statement.setString(2, newUser.getUsername());
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
            return true;
        }
        return false;
    }
}
