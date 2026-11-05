package cz.panacku.zlobse;

/**
 * Přepravka pro 2D souřadnice (X, Y) na hrací desce.
 */
public class TilePosition {
    private final double x;
    private final double y;

    public TilePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}