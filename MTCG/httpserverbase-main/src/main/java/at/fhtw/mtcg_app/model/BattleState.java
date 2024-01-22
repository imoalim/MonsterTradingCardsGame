package at.fhtw.mtcg_app.model;

public class BattleState {
    private String stateName; // You might use a more descriptive attribute
    private String description; // Additional metadata, if needed

    public BattleState(String stateName, String description) {
        this.stateName = stateName;
        this.description = description;
    }

    // Getters and setters for attributes

    @Override
    public String toString() {
        return stateName;
    }


    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

}
