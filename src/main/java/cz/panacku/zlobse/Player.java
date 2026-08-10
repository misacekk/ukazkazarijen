package cz.panacku.zlobse;

public class Player {
    private final String name;
    private final String characterType;
    private final String color;
    private final String[] inventory = new String[3];
    private int abilityUsesLeft = 2;

    public Player(String name, String characterType, String color) {
        this.name = name;
        this.characterType = characterType;
        this.color = color;
    }

    public String getName() { return name; }
    public String getCharacterType() { return characterType; }
    public String getColor() { return color; }
    public String[] getInventory() { return inventory; }
    public int getAbilityUsesLeft() { return abilityUsesLeft; }

    public boolean useAbility() {
        if (abilityUsesLeft > 0) {
            abilityUsesLeft--;
            return true;
        }
        return false;
    }

    public boolean addItem(String item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 3 && inventory[slotIndex] != null) {
            inventory[slotIndex] = null;
            return true;
        }
        return false;
    }
}