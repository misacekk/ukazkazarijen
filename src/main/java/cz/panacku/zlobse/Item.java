package cz.panacku.zlobse;

public enum Item {
    TELEPORT("Teleport"),
    STASTNA_SEDMA("Šťastná sedma"),
    CMAJZ("Čmajz");

    private final String name;

    Item(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}