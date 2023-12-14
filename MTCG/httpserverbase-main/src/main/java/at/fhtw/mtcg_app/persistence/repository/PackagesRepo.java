package at.fhtw.mtcg_app.persistence.repository;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.model.Card;
import at.fhtw.mtcg_app.model.Package;
import at.fhtw.mtcg_app.service.ExceptionHandler;

import java.sql.SQLException;


public interface PackagesRepo {

    //Package createPackage(Request request, Package newPackage);

    boolean acquireCards(Request request) throws ExceptionHandler;

    void createPackage(Package newPackage) throws SQLException;

    void createCard(Card card) throws SQLException;

    void associateCardToPackage(String id, String cardId) throws SQLException;
}
