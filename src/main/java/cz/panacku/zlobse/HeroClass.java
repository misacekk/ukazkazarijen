package cz.panacku.zlobse;

/**
 * Výčtový typ hrdinů dostupných ve hře.
 * Obsahuje uživatelské jméno a název souboru s obrázkem figurek.
 */
public enum HeroClass {
    NEKROMANT("Nekromant", "nekromant.png"),
    CARODEJNICE("Čarodějnice", "witch.png"),
    BANDITA("Bandita", "bandita.png"),
    RYTIR("Rytíř", "rytir.png"),
    ELFKA("Elfka", "elf.png"),
    KRALOVNA("Královna", "queen.png");

    private final String name;
    private final String imageFileName;

    HeroClass(String name, String imageFileName) {
        this.name = name;
        this.imageFileName = imageFileName;
    }

    public String getName() {
        return name;
    }

    public String getImageFileName() {
        return imageFileName;
    }
}