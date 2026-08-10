package cz.panacku.zlobse;

import java.util.List;
import java.util.Random;

public class GameEngine {
    private final List<Player> players;
    private int currentPlayerIndex = 0;
    private final Random random = new Random();
    private int lastDiceRoll = 0;
    private boolean hasRolled = false;

    public static final int BOARD_SIZE = 40;

    public GameEngine(List<Player> players) {
        this.players = players;
    }

    public int rollDice() {
        if (!hasRolled) {
            lastDiceRoll = random.nextInt(6) + 1;
            hasRolled = true;
        }
        return lastDiceRoll;
    }

    public boolean moveFigure(Figure figure, int steps) {
        if (!hasRolled) return false;

        if (figure.isInHome()) {
            if (steps == 6) {
                figure.setInHome(false);
                figure.setCurrentTileIndex(getStartTileForPlayer(getCurrentPlayer()));
                checkVyhozeni(figure);
                endTurn();
                return true;
            }
            return false;
        }

        int newIndex = (figure.getCurrentTileIndex() + steps) % BOARD_SIZE;
        figure.setCurrentTileIndex(newIndex);
        checkVyhozeni(figure);

        if (random.nextInt(10) > 6) {
            String[] items = {"TELEPORT", "ZLODEJ", "SKOK"};
            getCurrentPlayer().addItem(items[random.nextInt(items.length)]);
        }

        endTurn();
        return true;
    }

    private void checkVyhozeni(Figure movedFigure) {
        for (Player p : players) {
            if (p == getCurrentPlayer()) continue;
            for (Figure f : p.getFigures()) {
                if (!f.isInHome() && !f.isInFinish() && f.getCurrentTileIndex() == movedFigure.getCurrentTileIndex()) {
                    f.setInHome(true);
                    f.setCurrentTileIndex(-1);
                    System.out.println("Figurka hráče " + p.getName() + " byla vyhozena!");
                }
            }
        }
    }

    public void useAbility() {
        Player p = getCurrentPlayer();
        if (p.isAbilityUsedThisTurn()) return;

        switch (p.getCharacterType()) {
            case "Rytíř":
                System.out.println("Rytíř pouští schopnost: Štít aktivován!");
                break;
            case "Kouzelník":
                System.out.println("Kouzelník zdvojnásobuje hod!");
                break;
            case "Bandita":
                System.out.println("Bandita krade tah!");
                break;
            case "Nekromant":
                System.out.println("Nekromant oživuje figurku!");
                break;
        }
        p.setAbilityUsedThisTurn(true);
    }

    private int getStartTileForPlayer(Player p) {
        int idx = players.indexOf(p);
        return idx * 10;
    }

    public void endTurn() {
        hasRolled = false;
        getCurrentPlayer().setAbilityUsedThisTurn(false);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getLastDiceRoll() { return lastDiceRoll; }
    public boolean hasRolled() { return hasRolled; }
}