package cz.panacku.zlobse;

public enum HeroClass {
    NEKROMANT("Nekromant", "N", 55, 55, 0),
    KOUZELNIK("Kouzelník", "K", 1145, 55, 5),
    BANDITA("Bandita", "B", 1145, 645, 10),
    RYTIR("Rytíř", "R", 55, 645, 15);

    private final String name;
    private final String homeCode;
    private final double homeX;
    private final double homeY;
    private final int startIndex;

    HeroClass(String name, String homeCode, double homeX, double homeY, int startIndex) {
        this.name = name;
        this.homeCode = homeCode;
        this.homeX = homeX;
        this.homeY = homeY;
        this.startIndex = startIndex;
    }

    public String getName() {
        return name;
    }

    public String getHomeCode() {
        return homeCode;
    }

    public double getHomeX() {
        return homeX;
    }

    public double getHomeY() {
        return homeY;
    }

    public int getStartIndex() {
        return startIndex;
    }
}