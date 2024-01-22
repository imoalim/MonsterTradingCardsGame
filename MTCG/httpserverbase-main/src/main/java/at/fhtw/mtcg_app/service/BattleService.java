package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg_app.model.*;
import at.fhtw.mtcg_app.persistence.UnitOfWork;
import at.fhtw.mtcg_app.persistence.repository.BattleRepo;
import at.fhtw.mtcg_app.persistence.repository.BattleRepoImpl;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BattleService extends AbstractService{
    private final BattleRepo battleRepo;
    Player player1 = new Player(0,"one", new ArrayList<>(), new ArrayList<>());
    Player player2 = new Player(0,"two", new ArrayList<>(), new ArrayList<>());

    public BattleService() {
        this.battleRepo = new BattleRepoImpl(new UnitOfWork());
    }

    public Response handleBattleRequest(Request request) {
        try {
            // Step 1: Check the current battle state
            BattleStateEnum battleState = BattleStateEnum.WAITING_FOR_PLAYER1;
            List<Card> monsterCards = new ArrayList<>();
            List<Card> spellCards = new ArrayList<>();

            // Step 2: Process the request based on the current state
            switch (battleState.getState().getStateName()) {
                case "WAITING_FOR_PLAYER1":

                    // Process Player 1 request
                    player1 = setupPlayerData(request, monsterCards, spellCards, player1);
                    // Move to the next state
                    battleState.setState("WAITING_FOR_PLAYER2");
                    return new Response(HttpStatus.OK, ContentType.JSON, "Waiting for Player 2...");

                case "WAITING_FOR_PLAYER2":
                    // Process Player 2 request

                    player2 = setupPlayerData(request, monsterCards, spellCards, player2);

                    // Move to the next state
                    battleState.setState("BATTLE_IN_PROGRESS");
                case "BATTLE_IN_PROGRESS":
                    // Continue with battle logic
                    BattleLog battleLog = performBattle(player1,player2);

                    // Check for special cases and apply rules

                    // Update player decks based on the result
                    // battleRepo.updatePlayerDecks(result); // Assuming result is available

                    // Update battle state
                    battleState.setState("BATTLE_COMPLETE");

                    // Step 4: Battle Log
                    String battleLogJson = this.convertToJson(battleLog);

                    // Return the battle log as the response
                    return new Response(HttpStatus.OK, ContentType.JSON, battleLogJson);

                case "BATTLE_COMPLETE":
                    // Handle post-battle actions or cleanup
                    return new Response(HttpStatus.OK, ContentType.JSON, "Battle already complete.");

                default:
                    return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Invalid state");
            }
        } catch (Exception e) {
            // Handle exceptions
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, e.getMessage());
        }
    }

    private Player setupPlayerData(Request request, List<Card> monsterCards, List<Card> spellCards, Player player) throws SQLException {
        battleRepo.splitCards(request, monsterCards, spellCards);
        Integer userId = battleRepo.getUserId();
        player.setPlayerId(userId);
        String name = battleRepo.getPlayerName(player);
        player.setPlayerName(name);
        player.setMonsterCards(monsterCards);
        player.setSpellCards(spellCards);
        return player;
    }
    private BattleLog performBattle(Player player1, Player player2) {
        BattleLog battleLog = new BattleLog();

        for (int round = 1; round <= 100; round++) {
            // Randomly select one monster card and one spell card from each player
            List<Card> player1AllCards = new ArrayList<>();
            player1AllCards.addAll(player1.getMonsterCards());
            player1AllCards.addAll(player1.getSpellCards());

            List<Card> player2AllCards = new ArrayList<>();
            player2AllCards.addAll(player2.getMonsterCards());
            player2AllCards.addAll(player2.getSpellCards());

            Card player1Card = getRandomCard(player1AllCards);
            Card player2Card = getRandomCard(player2AllCards);

            setElementAndSpecialTypes(player1Card, player2Card);

            // Initialize damages
            DamageResult damageResult = calculateDamage(player1Card, player2Card);

            // Determine the winner and update decks
            RoundResult result = determineRoundWinner(damageResult, player1Card, player2Card, round);

            // Step 5: Update Player Stats
            updatePlayerStats(player1, player2,player1Card,player2Card, result, player1AllCards,player2AllCards);

            // Update battle log
            battleLog.addRoundResult(result);

        }

        return battleLog;
    }

    private void updatePlayerStats(Player player1, Player player2, Card player1Card, Card player2Card, RoundResult result, List<Card> player1AllCards, List<Card> player2AllCards) {
        if(result.getWinner().equalsIgnoreCase("PLAYER 1")){
            //takes the player2 card out of his deck and wins it for himself
            player2AllCards.remove(player2Card);
            player1AllCards.add(player2Card);
            //ELO: +3 points for win, -5 for loss, starting value: 100
            battleRepo.playerWon(player1);
            battleRepo.playerLoss(player2);

        }else if(result.getWinner().equalsIgnoreCase("PLAYER 2")){
            player1AllCards.remove(player1Card);
            player2AllCards.add(player1Card);
            battleRepo.playerWon(player2);
            battleRepo.playerLoss(player1);
        }
    }

    public Card getRandomCard(List<Card> allCards) {
        if (allCards == null || allCards.isEmpty()) {
            return null;  // Return null if the list is empty
        }

        Random random = new Random();

        int randomIndex = random.nextInt(allCards.size());
        return allCards.get(randomIndex);
    }


    private DamageResult calculateDamage(Card player1Card, Card player2Card) {
        if (player1Card == null || player2Card == null) {
            return new DamageResult(0, 0);
        }

        float player1Damage;
        float player2Damage;

        // spell fight
        if (player1Card.getSpecialType() == Card.SpecialType.NORMAL && player2Card.getSpecialType() == Card.SpecialType.NORMAL) {
            player1Damage = calculateSpellDamage(player1Card, player2Card);
            player2Damage = calculateSpellDamage(player2Card, player1Card);
        } else if (player1Card.getSpecialType() != Card.SpecialType.NORMAL && player2Card.getSpecialType() != Card.SpecialType.NORMAL) {
            // monster fight
            player1Damage = (int) player1Card.getDamage();
            player2Damage = (int) player2Card.getDamage();
        } else {
            // mixed fight
            // Check if the first card (spellCard) is a monster and the second card (monsterCard) is a spell
            if (!player1Card.getName().contains("Spell")) {
                // Swap the cards to ensure correct logic for the mixed fight
                Card temp = player1Card;
                player1Card = player2Card;
                player2Card = temp;
            }

            // Set damage for the monster card
            player2Damage = (int) player2Card.getDamage();

            // Calculate damage for the spell card
            player1Damage = calculateMixedFightDamage(player1Card, player2Card);
        }

        return new DamageResult(player1Damage, player2Damage);
    }

    private int calculateMixedFightDamage(Card spellCard, Card monsterCard) {
        return switch (spellCard.getElementType()) {
            case WATER ->
                    (monsterCard.getSpecialType() == Card.SpecialType.KNIGHTS) ? 0 : (int) monsterCard.getDamage() / 2;
            case FIRE ->
                    (monsterCard.getSpecialType() == Card.SpecialType.DRAGONS) ? 0 : (int) monsterCard.getDamage() / 2;
            default -> (monsterCard.getSpecialType() == Card.SpecialType.ORKS) ? 0 : (int) monsterCard.getDamage() / 2;
        };
    }


    private int calculateSpellDamage(Card spellCard, Card opposingCard) {
        return switch (spellCard.getElementType()) {
            case WATER ->
                    (opposingCard.getElementType() == Card.ElementType.FIRE) ? (int) spellCard.getDamage() * 2 : (int) spellCard.getDamage() / 2;
            case FIRE ->
                    (opposingCard.getElementType() == Card.ElementType.NORMAL) ? (int) spellCard.getDamage() * 2 : (int) spellCard.getDamage() / 2;
            default ->
                    (opposingCard.getElementType() == Card.ElementType.WATER) ? (int) spellCard.getDamage() * 2 : (int) spellCard.getDamage() / 2;
        };
    }
    private void setElementAndSpecialTypes(Card player1Card, Card player2Card) {

        setElementType(player1Card);
        setSpecialType(player1Card);

        setElementType(player2Card);
        setSpecialType(player2Card);

    }

    private void setElementType(Card card) {
        card.setElementType(getElementType(card.getName()));
    }

    private void setSpecialType(Card card) {
        card.setSpecialType(getSpecialType(card.getName()));
    }

    private Card.ElementType getElementType(String cardName) {
        if (cardName.contains("Water")) {
            return Card.ElementType.WATER;
        } else if (cardName.contains("Fire")) {
            return Card.ElementType.FIRE;
        } else {
            return Card.ElementType.NORMAL;
        }
    }

    private Card.SpecialType getSpecialType(String cardName) {
        if (cardName.contains("Goblin")) {
            return Card.SpecialType.GOBLINS;
        } else if (cardName.contains("Dragon")) {
            return Card.SpecialType.DRAGONS;
        } else if (cardName.contains("Wizard")) {
            return Card.SpecialType.WIZARD;
        } else if (cardName.contains("Ork")) {
            return Card.SpecialType.ORKS;
        } else if (cardName.contains("Knight")) {
            return Card.SpecialType.KNIGHTS;
        } else if (cardName.contains("Kraken")) {
            return Card.SpecialType.KRAKEN;
        } else if (cardName.contains("Elf")) {
            return Card.SpecialType.ELVES;
        } else if (cardName.contains("Troll")) {
            return Card.SpecialType.TROLL;
        } else {
            return Card.SpecialType.NORMAL;
        }
    }

    private RoundResult determineRoundWinner(DamageResult damageResult, Card player1Card, Card player2Card, int roundNumber) {
        float player1Damage = damageResult.getPlayer1Damage();
        float player2Damage = damageResult.getPlayer2Damage();
        String winner;

        if (player1Damage == player2Damage) {
            winner = "Draw";
        } else if (player1Damage < player2Damage) {
            winner = "Player 2";
        } else {
            winner = "Player 1";
        }
        String combinedType = player1Card.getName() + " vs " + player2Card.getName();

        return new RoundResult(roundNumber, player1Damage, player2Damage, winner, combinedType);
    }

}
