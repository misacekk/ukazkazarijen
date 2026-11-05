package cz.panacku.zlobse;

/**
 * Výčtový typ speciálních předmětů, které mohou hráči získat na desce a použít z inventáře.
 */
public enum Item {
    TELEPORT("Teleport", "teleport.png", "Prohodí pozici s nejbližším soupeřem."),
    STASTNA_SEDMA("Šťastná sedma", "stastna_sedma.png", "Posune figurku o 7 polí dopředu."),
    CMAJZ("Čmajz", "cmajz.png", "Ukradne náhodný předmět protihráči.");

    private final String name;
    private final String imageFileName;
    private final String description;

    Item(String name, String imageFileName, String description) {
        this.name = name;
        this.imageFileName = imageFileName;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String getDescription() {
        return description;
    }
}