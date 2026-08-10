package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final String characterType;
    private final String color;
    private final List<Figure> figures;
    private final String[] inventory = new String[3];
    private boolean abilityUsedThisTurn = false;

    public Player(String name, String characterType, String color) {
        this.name = name;
        this.characterType = characterType;
        this.color = color;
        this.figures = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            this.figures.add(new Figure(i, color));
        }
    }

    public String getName() { return name; }
    public String getCharacterType() { return characterType; }
    public String getColor() { return color; }
    public List<Figure> getFigures() { return figures; }
    public String[] getInventory() { return inventory; }
    public boolean isAbilityUsedThisTurn() { return abilityUsedThisTurn; }
    public void setAbilityUsedThisTurn(boolean used) { this.abilityUsedThisTurn = used; }

    public boolean addItem(String item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                return true;
            }
        }
        return false;
    }

    public boolean useItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 3 && inventory[slotIndex] != null) {
            inventory[slotIndex] = null;
            return true;
        }
        return false;
    }

    public boolean hasWon() {
        return figures.stream().allMatch(Figure::isInFinish);
    }
}