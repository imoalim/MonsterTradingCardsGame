package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.persistence.UnitOfWork;


import java.util.List;

public class CardsRepoImpl extends BaseRepo implements CardsRepo {
    public CardsRepoImpl(UnitOfWork unitOfWork) {
        super(unitOfWork);
    }


    @Override
    public List<Card> getUserCards(String username) {
        return super.getUserCards(username);
    }



}
