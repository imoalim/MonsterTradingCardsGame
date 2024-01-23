package at.fhtw.mtcg_app.service;

import at.fhtw.mtcg_app.model.BattleState;

public enum BattleStateEnum {
    WAITING_FOR_PLAYER1(new BattleState("WAITING_FOR_PLAYER1", "Waiting for Player 1")),
    WAITING_FOR_PLAYER2(new BattleState("WAITING_FOR_PLAYER2", "Waiting for Player 2")),
    BATTLE_IN_PROGRESS(new BattleState("BATTLE_IN_PROGRESS", "Battle in progress")),
    BATTLE_COMPLETE(new BattleState("BATTLE_COMPLETE", "Battle complete"));

    private final BattleState state;

    BattleStateEnum(BattleState state) {
        this.state = state;
    }

    public BattleState getState() {
        return state;
    }

    public void setState(String newStateName){
        // Set the new state values
        this.state.setStateName(newStateName);
    }

}
