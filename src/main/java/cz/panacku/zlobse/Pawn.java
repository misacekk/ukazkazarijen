package cz.panacku.zlobse;

public class Pawn {
    private final int id;
    private final Player owner;
    private int currentTileIndex;
    private int totalStepsMoved = 0;
    private boolean inHouse = true;
    private boolean inFinish = false;

    public Pawn(int id, Player owner) {
        this.id = id;
        this.owner = owner;
        this.currentTileIndex = owner.getHeroClass().getStartIndex();
    }

    public int getId() { return id; }
    public Player getOwner() { return owner; }
    public int getCurrentTileIndex() { return currentTileIndex; }

    public void stepToTile(int index) {
        this.currentTileIndex = (index % BoardPath.getPathLength() + BoardPath.getPathLength()) % BoardPath.getPathLength();
    }

    public int getTotalStepsMoved() { return totalStepsMoved; }
    public void setTotalStepsMoved(int totalStepsMoved) { this.totalStepsMoved = totalStepsMoved; }
    public void addSteps(int steps) { this.totalStepsMoved += steps; }

    public boolean isInHouse() { return inHouse; }
    public void setInHouse(boolean inHouse) {
        this.inHouse = inHouse;
        if (inHouse) {
            resetToSpawn();
        }
    }

    public void resetToSpawn() {
        this.inHouse = true;
        this.inFinish = false;
        this.currentTileIndex = owner.getHeroClass().getStartIndex();
        this.totalStepsMoved = 0;
    }

    public boolean isInFinish() { return inFinish; }
    public void setInFinish(boolean inFinish) { this.inFinish = inFinish; }

    public boolean isAtEntranceTile() {
        return !inHouse && !inFinish && totalStepsMoved >= BoardPath.getPathLength();
    }

    public int getStepsToEntrance() {
        return Math.max(0, BoardPath.getPathLength() - totalStepsMoved);
    }
}