package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.UserStats;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.service.AuthHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GameStatRepoImpl extends BaseRepo implements GameStatRepo{
    public GameStatRepoImpl(UnitOfWork unitOfWork) {
        super(unitOfWork);
    }

    @Override
    public UserStats getUserStats(Request request) throws Exception {
        AuthHandler.checkAuthFromHeader(request);
        String usernameFromToken = AuthHandler.getUsernameFromToken();
        int userId = getUserIdByUsername("public.user",usernameFromToken);

        String userName = null;
        try (PreparedStatement getName = this.unitOfWork.prepareStatement(
                "SELECT name FROM public.user_data WHERE user_id = ?")) {
            getName.setInt(1, userId);
            try (ResultSet rs = getName.executeQuery()) {
                if (rs.next()) {
                    userName = rs.getString("name");
                }
            }
        }

        try (PreparedStatement statement = this.unitOfWork.prepareStatement(
                "SELECT * FROM public.user_stats WHERE name = ?")) {
            statement.setString(1, userName);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int elo = rs.getInt("elo");
                    int wins = rs.getInt("wins");
                    int losses = rs.getInt("losses");
                    return new UserStats(name, elo, wins, losses);
                }
            }
        }
        throw new Exception("User not found");
    }

    @Override
    public List<UserStats> getScoreboard(Request request) throws Exception {
        AuthHandler.checkAuthFromHeader(request);
        List<UserStats> scoreboard = new ArrayList<>();

        try (PreparedStatement statement = this.unitOfWork.prepareStatement(
                "SELECT * FROM public.user_stats ORDER BY elo DESC")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int elo = rs.getInt("elo");
                    int wins = rs.getInt("wins");
                    int losses = rs.getInt("losses");
                    scoreboard.add(new UserStats(name, elo, wins, losses));
                }
            }
        }
        return scoreboard;
    }

}
