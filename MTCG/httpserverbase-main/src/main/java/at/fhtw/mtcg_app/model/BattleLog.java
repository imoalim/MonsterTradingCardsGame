package at.fhtw.mtcg_app.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.ArrayList;
import java.util.List;

public class BattleLog {

    @JsonAlias({"roundResults", "ListOfRoundResults"})
    private final List<RoundResult> roundResults;

    public BattleLog() {
        this.roundResults = new ArrayList<>();
    }

    public void addRoundResult(RoundResult result) {
        roundResults.add(result);
    }

}
