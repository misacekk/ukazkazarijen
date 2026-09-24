package cz.panacku.zlobse;

public enum HeroClass {
    NEKROMANT("Nekromant"),
    KOUZELNIK("Kouzelník"),
    BANDITA("Bandita"),
    RYTIR("Rytíř");

    private final String name;

    HeroClass(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}