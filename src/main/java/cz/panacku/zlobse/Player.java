package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje hráče ve hře. Spravuje jeho hrdinu, seznam 4 figurek
 * a stav aktuálního tahu.
 */
public class Player {
    private final int id;
    private final HeroClass heroClass;
    private final List<Pawn> pawns = new ArrayList<>();

    private boolean hasRolledThisTurn = false;
    private boolean skipped = false;

    public static final int TARGET_SCORE = 4;

    public Player(int id, HeroClass heroClass) {
        this.id = id;
        this.heroClass = heroClass;
        // Vytvoření 4 základních figurek
        for (int i = 0; i < 4; i++) {
            this.pawns.add(new Pawn(i, this));
        }
    }

    public int getId() { return id; }
    public int getSlotIndex() { return id - 1; }
    public HeroClass getHeroClass() { return heroClass; }
    public List<Pawn> getPawns() { return pawns; }

    /**
     * Vrací index startovního políčka hráče.
     */
    public int getStartTileIndex() {
        return BoardPath.getStartTileIndex(getSlotIndex());
    }

    /**
     * Vrací počet figurek v cílovém domečku.
     */
    public int getScore() {
        int count = 0;
        for (Pawn p : pawns) {
            if (p.isInFinish()) count++;
        }
        return count;
    }

    public boolean isFinished() { return getScore() >= TARGET_SCORE; }
    public boolean hasRolledThisTurn() { return hasRolledThisTurn; }
    public void setHasRolledThisTurn(boolean hasRolledThisTurn) { this.hasRolledThisTurn = hasRolledThisTurn; }
    public boolean isSkipped() { return skipped; }
    public void setSkipped(boolean skipped) { this.skipped = skipped; }

    public void resetTurn() { this.hasRolledThisTurn = false; }

    /**
     * Vrací první figurku, která je ještě v domovském spawnu (nebyla nasazena).
     */
    public Pawn getPawnInHouse() {
        for (Pawn p : pawns) {
            if (p.isInHouse()) return p;
        }
        return null;
    }

    /**
     * Vrací seznam figurek, které se aktuálně pohybují po hrací desce.
     */
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