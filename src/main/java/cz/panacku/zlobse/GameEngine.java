package cz.panacku.zlobse;

import java.util.List;
import java.util.Random;

public class GameEngine {
    private final List<Player> players;
    private int currentPlayerIndex = 0;
    private final Random random = new Random();
    private int lastDiceRoll = 0;
    private boolean hasRolled = false;

    public GameEngine(List<Player> players) {
        this.players = players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean hasRolled() {
        return hasRolled;
    }

    public int rollDice() {
        if (!hasRolled) {
            lastDiceRoll = random.nextInt(6) + 1;
            hasRolled = true;
        }
        return lastDiceRoll;
    }

    public int getLastDiceRoll() {
        return lastDiceRoll;
    }

    public void endTurn() {
        hasRolled = false;
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}