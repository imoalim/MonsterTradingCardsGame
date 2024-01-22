package at.fhtw.mtcg_app.model;

public class DamageResult {
    private final float player1Damage;
    private final float player2Damage;

    public DamageResult(float player1Damage, float player2Damage) {
        this.player1Damage = player1Damage;
        this.player2Damage = player2Damage;
    }

    public float getPlayer1Damage() {
        return player1Damage;
    }

    public float getPlayer2Damage() {
        return player2Damage;
    }
}
