package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int id;
    private final HeroClass heroClass;
    private final List<Pawn> pawns = new ArrayList<>();

    private boolean hasRolledThisTurn = false;
    private boolean abilityUsed = false;
    private boolean doubleRollActive = false;
    private boolean skipped = false;

    public static final int TARGET_SCORE = 4;
    private final List<Item> inventory = new ArrayList<>();

    public Player(int id, HeroClass heroClass) {
        this.id = id;
        this.heroClass = heroClass;
        for (int i = 0; i < 4; i++) {
            this.pawns.add(new Pawn(i, this));
        }
    }

    public int getId() { return id; }
    public HeroClass getHeroClass() { return heroClass; }
    public List<Pawn> getPawns() { return pawns; }

    public int getScore() {
        int count = 0;
        for (Pawn p : pawns) {
            if (p.isInFinish()) count++;
        }
        return count;
    }

    public boolean isFinished() {
        return getScore() >= TARGET_SCORE;
    }

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

    public Pawn getPawnInHouse() {
        for (Pawn p : pawns) {
            if (p.isInHouse()) return p;
        }
        return null;
    }

    public List<Pawn> getPawnsOnBoard() {
        List<Pawn> list = new ArrayList<>();
        for (Pawn p : pawns) {
            if (!p.isInHouse() && !p.isInFinish()) {
                list.add(p);
            }
        }
        return list;
    }
}