package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final HeroClass heroClass;
    private boolean inHouse;
    private int currentTileIndex;
    private boolean hasRolledThisTurn;
    private final List<Item> inventory = new ArrayList<>();

    public Player(HeroClass heroClass) {
        this.heroClass = heroClass;
        this.inHouse = true;
        this.currentTileIndex = -1;
        this.hasRolledThisTurn = false;
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

    public List<Item> getInventory() {
        return inventory;
    }

    public boolean addItem(Item item) {
        if (inventory.size() < 3) {
            inventory.add(item);
            return true;
        }
        return false;
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