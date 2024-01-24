package at.fhtw.mtcg_app;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.*;
import at.fhtw.mtcg_app.service.BattleService;
import org.junit.jupiter.api.Test;

import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleServiceTest {

    @Test
    void handleBattleRequest_ErrorRequests_SuccessfulResponse() {
        // Arrange
        BattleService battleService = new BattleService();
        Request player1Request = new Request();
        Request player2Request = new Request();

        // Act
        Response response = battleService.handleBattleRequest(player1Request, player2Request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, response.getStatus());
        assertEquals(ContentType.JSON.type, response.getContentType());
    }

    @Test
    void performBattle_ValidPlayers_BattleLogGenerated() {
        // Arrange
        BattleService battleService = new BattleService();

        // Create mock players
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        // Mock the behavior of the players
        when(player1.getPlayerId()).thenReturn(1);
        when(player1.getPlayerName()).thenReturn("Player 1");
        when(player1.getMonsterCards()).thenReturn(Collections.singletonList(new Card("MonsterCard1", "dragon", 5)));
        when(player1.getSpellCards()).thenReturn(Collections.singletonList(new Card("SpellCard1", "RandomSpell", 10)));

        when(player2.getPlayerId()).thenReturn(2);
        when(player2.getPlayerName()).thenReturn("Player 2");
        when(player2.getMonsterCards()).thenReturn(Collections.singletonList(new Card("MonsterCard2", "Witch", 4)));
        when(player2.getSpellCards()).thenReturn(Collections.singletonList(new Card("SpellCard2", "WaterSpell", 8)));

        // Act
        BattleLog battleLog = battleService.performBattle(player1, player2);

        // Assert
        assertNotNull(battleLog);
        assertFalse(battleLog.getRoundResults().isEmpty());
    }
}
