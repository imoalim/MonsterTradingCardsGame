package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.Card;

import java.util.List;

public interface CardsRepo {
    List<Card> getUserCards(String userId);
}
