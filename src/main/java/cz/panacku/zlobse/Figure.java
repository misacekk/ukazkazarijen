package cz.panacku.zlobse;

public class Figure {
    private int id;
    private String ownerColor;

    public Figure(int id, String ownerColor) {
        this.id = id;
        this.ownerColor = ownerColor;
    }

    public int getId() { return id; }
    public String getOwnerColor() { return ownerColor; }
}