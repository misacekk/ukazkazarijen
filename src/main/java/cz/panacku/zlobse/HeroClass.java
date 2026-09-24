package cz.panacku.zlobse;

public enum HeroClass {
    NEKROMANT("Nekromant", 33, 127.0, 87.0, "nekromant.png"),
    KOUZELNIK("Kouzelník", 8, 1273.0, 82.0, "mag.png"),
    BANDITA("Bandita", 16, 1267.0, 743.0, "bandita.png"),
    RYTIR("Rytíř", 25, 137.0, 758.0, "rytir.png");

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