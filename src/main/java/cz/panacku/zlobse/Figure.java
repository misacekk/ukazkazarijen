package cz.panacku.zlobse;

public class Figure {
    private final int id;
    private final String ownerColor;
    private int currentTileIndex;
    private boolean isInHome;
    private boolean isInFinish;

    public Figure(int id, String ownerColor) {
        this.id = id;
        this.ownerColor = ownerColor;
        this.currentTileIndex = -1;
        this.isInHome = true;
        this.isInFinish = false;
    }

    public int getId() { return id; }
    public String getOwnerColor() { return ownerColor; }
    public int getCurrentTileIndex() { return currentTileIndex; }
    public void setCurrentTileIndex(int index) { this.currentTileIndex = index; }
    public boolean isInHome() { return isInHome; }
    public void setInHome(boolean inHome) { this.isInHome = inHome; }
    public boolean isInFinish() { return isInFinish; }
    public void setInFinish(boolean inFinish) { this.isInFinish = inFinish; }
}