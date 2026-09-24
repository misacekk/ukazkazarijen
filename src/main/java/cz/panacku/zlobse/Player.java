package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int id;
    private final HeroClass heroClass;
    private int currentTileIndex;
    private int totalStepsMoved = 0;
    private boolean inHouse = true;
    private boolean finished = false;
    private boolean hasRolledThisTurn = false;
    private boolean abilityUsed = false;

    private int score = 0;
    public static final int TARGET_SCORE = 4;

    private boolean doubleRollActive = false;
    private boolean skipped = false;

    private final List<Item> inventory = new ArrayList<>();

    public Player(int id, HeroClass heroClass) {
        this.id = id;
        this.heroClass = heroClass;
        this.currentTileIndex = heroClass.getStartIndex();
    }

    public int getId() { return id; }
    public HeroClass getHeroClass() { return heroClass; }
    public int getCurrentTileIndex() { return currentTileIndex; }

    public void stepToTile(int index) {
        this.currentTileIndex = (index % BoardPath.getPathLength() + BoardPath.getPathLength()) % BoardPath.getPathLength();
    }

    public int getTotalStepsMoved() { return totalStepsMoved; }
    public void addSteps(int steps) { this.totalStepsMoved += steps; }

    public boolean isInHouse() { return inHouse; }

    public void resetToSpawn() {
        this.inHouse = true;
        this.currentTileIndex = heroClass.getStartIndex();
        this.totalStepsMoved = 0;
    }

    public void setInHouse(boolean inHouse) {
        this.inHouse = inHouse;
        if (inHouse) {
            resetToSpawn();
        }
    }

    public boolean isAtEntranceTile() {
        return !inHouse && totalStepsMoved >= BoardPath.getPathLength();
    }

    public int getStepsToEntrance() {
        return Math.max(0, BoardPath.getPathLength() - totalStepsMoved);
    }

    public int getScore() { return score; }
    public void incrementScore() { this.score++; }

    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }

    public boolean hasRolledThisTurn() { return hasRolledThisTurn; }
    public void setHasRolledThisTurn(boolean hasRolledThisTurn) { this.hasRolledThisTurn = hasRolledThisTurn; }

    public boolean hasUsedAbility() { return abilityUsed; }
    public void setAbilityUsed(boolean abilityUsed) { this.abilityUsed = abilityUsed; }

    public boolean isDoubleRollActive() { return doubleRollActive; }
    public void setDoubleRollActive(boolean doubleRollActive) { this.doubleRollActive = doubleRollActive; }

    public boolean isSkipped() { return skipped; }
    public void setSkipped(boolean skipped) { this.skipped = skipped; }

    public List<Item> getInventory() { return inventory; }

    public void addItem(Item item) {
        if (inventory.size() < 3) {
            inventory.add(item);
        }
    }

    public void resetTurn() {
        this.hasRolledThisTurn = false;
    }
}