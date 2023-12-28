package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.Deck;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.service.AuthHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeckRepoImpl extends BaseRepo implements DeckRepo {
    public DeckRepoImpl(UnitOfWork unitOfWork) {
        super(unitOfWork);
    }

    private int userID;

    private void checkAuth(Request request) throws Exception {
        try {
            AuthHandler.checkAuthFromHeader(request);
            setUserID(AuthHandler.getUsernameFromToken());
        } catch (Exception e) {
            throw new Exception("\t\n" +
                    "Authentication failed", e);
        }
    }

    private void setUserID(String userName) throws SQLException {
        userID = this.getUserIdByUsername("public.user", userName);
    }

    @Override
    public List<Deck> getConfiguredDeck(Request request) throws Exception {
        checkAuth(request);
        List<Deck> decks = new ArrayList<>();

        try (PreparedStatement statement = this.unitOfWork.prepareStatement(
                "SELECT * FROM public.deck WHERE user_id = ?")) {
            statement.setInt(1, userID);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Deck deck = new Deck();
                    deck.setUser_id(rs.getString("user_id"));
                    deck.setCard_id(rs.getString("card_id"));
                    decks.add(deck);
                }
            }
        }
        return decks;
    }


    @Override
    public boolean configureDeck(Request request, List<String> cardIds) throws Exception {
        checkAuth(request);
        String username = AuthHandler.getUsernameFromToken();
        List<Card> userAcquiredCards = this.getUserCards(username);
        if (!checkIfUserAcquiredCards(userAcquiredCards, cardIds)) {
            throw new Exception("User didn't acquire cards");
        }
        if (cardIds.size() != 4) {
            throw new Exception("Didn't include the required amount of cards");
        }
        String insertSql = "INSERT INTO public.deck (user_id, card_id) VALUES (?, ?)";
        for (String cardId : cardIds) {
            try (PreparedStatement statement = this.unitOfWork.prepareStatement(insertSql)) {
                statement.setInt(1, userID);
                statement.setString(2, cardId);
                statement.executeUpdate();
                this.unitOfWork.commitTransaction();
            }
        }
        return true;
    }


    private boolean checkIfUserAcquiredCards(List<Card> userAcquiredCards, List<String> cardIds) {
        // Extract card IDs from userAcquiredCards
        Set<String> acquiredCardIds = userAcquiredCards.stream()
                .map(Card::getCardId)
                .collect(Collectors.toSet());


        for (String cardId : cardIds) {
            if (!acquiredCardIds.contains(cardId)) {
                System.out.println("Card ID not found: " + cardId);
                System.out.println("User's Acquired Card IDs: " + acquiredCardIds);
                System.out.println("Requested Card IDs: " + cardIds);
                return false;
            }
        }

        return true;
    }
}
