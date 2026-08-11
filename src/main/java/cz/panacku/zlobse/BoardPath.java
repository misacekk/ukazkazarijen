package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class BoardPath {
    private static final List<TilePosition> PATH = new ArrayList<>();

    static {
        // Horní řada (zleva doprava)
        PATH.add(new TilePosition(205, 154)); // Index 0 (Start pro Nekromanta)
        PATH.add(new TilePosition(315, 154)); // Index 1
        PATH.add(new TilePosition(427, 155)); // Index 2
        PATH.add(new TilePosition(538, 155)); // Index 3
        PATH.add(new TilePosition(650, 155)); // Index 4
        PATH.add(new TilePosition(761, 154)); // Index 5 (Start pro Kouzelníka)
        PATH.add(new TilePosition(871, 154)); // Index 6
        PATH.add(new TilePosition(985, 154)); // Index 7

        // Pravá strana (shora dolů)
        PATH.add(new TilePosition(1064, 235)); // Index 8
        PATH.add(new TilePosition(1064, 344)); // Index 9
        PATH.add(new TilePosition(1064, 453)); // Index 10 (Start pro Banditu)

        // Spodní řada (zprava doleva)
        PATH.add(new TilePosition(983, 535)); // Index 11
        PATH.add(new TilePosition(874, 536)); // Index 12
        PATH.add(new TilePosition(761, 535)); // Index 13
        PATH.add(new TilePosition(649, 535)); // Index 14
        PATH.add(new TilePosition(541, 535)); // Index 15 (Start pro Rytíře)
        PATH.add(new TilePosition(427, 535)); // Index 16
        PATH.add(new TilePosition(315, 535)); // Index 17
        PATH.add(new TilePosition(203, 533)); // Index 18

        // Levá strana (zdola nahoru)
        PATH.add(new TilePosition(124, 454)); // Index 19
        PATH.add(new TilePosition(125, 345)); // Index 20
        PATH.add(new TilePosition(124, 236)); // Index 21
    }

    public static TilePosition getTile(int index) {
        int safeIndex = (index % PATH.size() + PATH.size()) % PATH.size();
        return PATH.get(safeIndex);
    }

    public static int getPathLength() {
        return PATH.size();
    }
}