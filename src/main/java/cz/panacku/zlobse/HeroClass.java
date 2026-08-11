package cz.panacku.zlobse;

public enum HeroClass {
    RYTIR("Rytíř", 15, 60, 535, "rytir.png"),
    KOUZELNIK("Kouzelník", 5, 1140, 60, "mag.png"),
    BANDITA("Bandita", 10, 1140, 535, "bandita.png"),
    NEKROMANT("Nekromant", 0, 60, 60, "nekromant.png");

    private final String name;
    private final int startIndex;
    private final double homeX;
    private final double homeY;
    private final String imageFileName;

    HeroClass(String name, int startIndex, double homeX, double homeY, String imageFileName) {
        this.name = name;
        this.startIndex = startIndex;
        this.homeX = homeX;
        this.homeY = homeY;
        this.imageFileName = imageFileName;
    }

    public String getName() { return name; }
    public int getStartIndex() { return startIndex; }
    public double getHomeX() { return homeX; }
    public double getHomeY() { return homeY; }
    public String getImageFileName() { return imageFileName; }
}