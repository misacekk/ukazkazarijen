package cz.panacku.zlobse;

public class Player {
    private final HeroClass heroClass;
    private boolean inHouse;
    private int currentTileIndex;
    private boolean hasRolledThisTurn;

    public Player(HeroClass heroClass) {
        this.heroClass = heroClass;
        this.inHouse = true;               // DŮLEŽITÉ: Začíná uvnitř domečku N
        this.currentTileIndex = -1;
        this.hasRolledThisTurn = false;    // Ochrana proti více hodům v jednom tahu
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public boolean isInHouse() {
        return inHouse;
    }

    public int getCurrentTileIndex() {
        return currentTileIndex;
    }

    public boolean hasRolledThisTurn() {
        return hasRolledThisTurn;
    }

    public void setHasRolledThisTurn(boolean status) {
        this.hasRolledThisTurn = status;
    }

    public void stepToTile(int newIndex) {
        this.inHouse = false;
        this.currentTileIndex = newIndex % BoardPath.getPathLength();
    }

    public void resetToHouse() {
        this.inHouse = true;
        this.currentTileIndex = -1;
        this.hasRolledThisTurn = false;
    }
}