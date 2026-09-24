package cz.panacku.zlobse;

public class Player {
    private final int id;
    private final HeroClass heroClass;

    public Player(int id, HeroClass heroClass) {
        this.id = id;
        this.heroClass = heroClass;
    }

    public int getId() { return id; }
    public HeroClass getHeroClass() { return heroClass; }
}