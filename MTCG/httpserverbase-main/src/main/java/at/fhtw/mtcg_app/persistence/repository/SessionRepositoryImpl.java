package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.User;
import at.fhtw.mtcg_app.persistence.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SessionRepositoryImpl extends BaseRepo implements SessionRepository {


    public SessionRepositoryImpl(UnitOfWork unitOfWork) {
        super(unitOfWork);
    }


    @Override
    public boolean islogged(User newUser) throws SQLException {

        List<User> userList = this.readAllUsersFromDB("public.user");

        boolean userExists = userList.stream()
                .anyMatch(user -> user.getUsername().equals(newUser.getUsername()));
        boolean passwordIsValid = userList.stream()
                .anyMatch(user -> user.getPassword().equals(newUser.getPassword()));

        if (userExists && passwordIsValid) {
            String userToken = newUser.getUsername() + "-mtcgToken";
            PreparedStatement statement = this.unitOfWork.prepareStatement(
                    "UPDATE public.user SET token = ? WHERE username = ?");
            statement.setString(1, userToken);
            statement.setString(2, newUser.getUsername());
            statement.executeUpdate();
            this.unitOfWork.commitTransaction();
            return true;
        }
        return false;
    }
}
