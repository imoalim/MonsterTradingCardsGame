package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class Package {
    public Package(int id, String internalId) {
        this.id = id;
        this.internalId = internalId;
    }

    public Package() {
        // Default constructor is required for Jackson deserialization
    }

    @JsonAlias({"listOfCards"})
    private List<Card> cards;
    @JsonAlias({"Id"})
    private int id;
    @JsonAlias({"InternalId"})
    private String internalId;
    @JsonAlias({"Status"})
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}
