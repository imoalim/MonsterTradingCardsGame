package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Deck {
    public Deck(String user_id, String card_id) {
        this.user_id = user_id;
        this.card_id = card_id;
    }

    public Deck() {

    }

    @JsonAlias({"User_id"})
    private String user_id;
    @JsonAlias({"Card_id"})
    private String card_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
}
