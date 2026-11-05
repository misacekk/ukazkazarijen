package cz.panacku.zlobse;

/**
 * Reprezentuje jednotlivou figurku hráče na hrací desce.
 * Sleduje její pozici, stav v domečku a odehrané kroky.
 */
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
        this.currentTileIndex = owner.getStartTileIndex();
    }

    // Gettery základních vlastností
    public int getId() { return id; }
    public Player getOwner() { return owner; }
    public int getCurrentTileIndex() { return currentTileIndex; }

    /**
     * Posune figurku na zadaný index políčka na desce s ošetřením celkového okruhu.
     */
    public void stepToTile(int index) {
        int length = BoardPath.getPathLength();
        this.currentTileIndex = (index % length + length) % length;
    }

    // Správa celkového počtu ujetých kroků
    public int getTotalStepsMoved() { return totalStepsMoved; }
    public void setTotalStepsMoved(int totalStepsMoved) { this.totalStepsMoved = totalStepsMoved; }
    public void addSteps(int steps) { this.totalStepsMoved += steps; }

    // Správa stavu v domovském spawnu
    public boolean isInHouse() { return inHouse; }
    public void setInHouse(boolean inHouse) {
        this.inHouse = inHouse;
        if (inHouse) {
            resetToSpawn();
        }
    }

    /**
     * Resetuje stav figurky zpět do počátečního spawnu (při vyhození).
     */
    public void resetToSpawn() {
        this.inHouse = true;
        this.inFinish = false;
        this.currentTileIndex = owner.getStartTileIndex();
        this.totalStepsMoved = 0;
    }

    // Správa cílového domečku
    public boolean isInFinish() { return inFinish; }
    public void setInFinish(boolean inFinish) { this.inFinish = inFinish; }

    /**
     * Kontroluje, zda je figurka přesně před vstupem do cílového domečku.
     */
    public boolean isAtEntranceTile() {
        return !inHouse && !inFinish && totalStepsMoved >= BoardPath.getPathLength();
    }

    /**
     * Vrací zbývající počet kroků do vstupu do domečku.
     */
    public int getStepsToEntrance() {
        return Math.max(0, BoardPath.getPathLength() - totalStepsMoved);
    }
}