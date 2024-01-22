package at.fhtw.mtcg_app.model;
import java.util.ArrayList;
import java.util.List;

public class BattleLog {
    private final List<RoundResult> roundResults;

    public BattleLog() {
        this.roundResults = new ArrayList<>();
    }

    public void addRoundResult(RoundResult result) {
        roundResults.add(result);
    }
}
