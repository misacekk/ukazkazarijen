package cz.panacku.zlobse;

public class BoardTile {
    private final int id;
    private final double x;
    private final double y;
    private final boolean isItemTile;

    public BoardTile(int id, double x, double y, boolean isItemTile) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isItemTile = isItemTile;
    }

    public int getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isItemTile() { return isItemTile; }
}