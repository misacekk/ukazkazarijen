package cz.panacku.zlobse;

import java.util.Random;

public enum Item {
    TELEPORT("Teleport", "Ihned prohodí pozici tvé figurky s nejbližší figurkou soupeře na herním plánu.", "teleport.png"),
    STASTNA_SEDMA("Šťastná sedma", "V tomto kole nehážíš kostkou, ale posouváš se o garantovaných 7 políček dopředu.", "stastna_sedma.png"),
    CMAJZ("Čmajz", "Umožní vybrat jednoho protihráče a ukrást mu náhodný předmět přímo do tvého hotbaru.", "cmajz.png");

    private final String name;
    private final String description;
    private final String imageFileName;

    Item(String name, String description, String imageFileName) {
        this.name = name;
        this.description = description;
        this.imageFileName = imageFileName;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageFileName() { return imageFileName; }

    public static Item getRandomItem() {
        Item[] items = values();
        return items[new Random().nextInt(items.length)];
    }
}