package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final HeroClass heroClass;
    private int currentTileIndex = 0;
    private boolean inHouse = true; // Zde je nastaveno, že hráč začíná v domečku
    private boolean hasRolledThisTurn = false;
    private final List<Item> inventory = new ArrayList<>();

    public Player(HeroClass heroClass) {
        this.heroClass = heroClass;
    }

    public HeroClass getHeroClass() { return heroClass; }

    public int getCurrentTileIndex() { return currentTileIndex; }

    public void stepToTile(int index) {
        this.currentTileIndex = index % BoardPath.getPathLength();
    }

    public boolean isInHouse() { return inHouse; }

    public void setInHouse(boolean inHouse) { this.inHouse = inHouse; }

    public boolean hasRolledThisTurn() { return hasRolledThisTurn; }

    public void setHasRolledThisTurn(boolean hasRolledThisTurn) {
        this.hasRolledThisTurn = hasRolledThisTurn;
    }

    public List<Item> getInventory() { return inventory; }

    public void addItem(Item item) {
        if (inventory.size() < 3) {
            inventory.add(item);
        }
    }
}