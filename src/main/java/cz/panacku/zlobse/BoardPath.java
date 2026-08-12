package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class BoardPath {
    private static final List<TilePosition> PATH = new ArrayList<>();

    static {
        // --- 1. HORNÍ ŘADA (Indexy 0 - 7) ---
        PATH.add(new TilePosition(306.0, 64.0));  // 0: Start Nekromant (Hřbitov)
        PATH.add(new TilePosition(420.0, 64.0));  // 1
        PATH.add(new TilePosition(530.0, 64.0));  // 2
        PATH.add(new TilePosition(642.0, 64.0));  // 3
        PATH.add(new TilePosition(754.0, 65.0));  // 4: TRUHLA (Horní)
        PATH.add(new TilePosition(865.0, 65.0));  // 5
        PATH.add(new TilePosition(976.0, 65.0));  // 6
        PATH.add(new TilePosition(1087.0, 64.0)); // 7

        // --- 2. HORNÍ PRAVÁ VĚTEV A BOK (Indexy 8 - 13) ---
        PATH.add(new TilePosition(1088.0, 176.0)); // 8
        PATH.add(new TilePosition(1199.0, 177.0)); // 9
        PATH.add(new TilePosition(1312.0, 176.0)); // 10: Start Kouzelník (Les)
        PATH.add(new TilePosition(1312.0, 289.0)); // 11
        PATH.add(new TilePosition(1313.0, 399.0)); // 12
        PATH.add(new TilePosition(1314.0, 511.0)); // 13: TRUHLA (Pravá)

        // --- 3. SPODNÍ PRAVÁ VĚTEV A ŘADA (Indexy 14 - 24) ---
        PATH.add(new TilePosition(1312.0, 623.0)); // 14: Start Bandita (Pevnost)
        PATH.add(new TilePosition(1201.0, 623.0)); // 15
        PATH.add(new TilePosition(1088.0, 623.0)); // 16
        PATH.add(new TilePosition(1090.0, 734.0)); // 17
        PATH.add(new TilePosition(978.0, 736.0));  // 18
        PATH.add(new TilePosition(867.0, 736.0));  // 19
        PATH.add(new TilePosition(755.0, 735.0));  // 20:
        PATH.add(new TilePosition(644.0, 733.0));  // 21 TRUHLA (Spodní)
        PATH.add(new TilePosition(531.0, 735.0));  // 22
        PATH.add(new TilePosition(421.0, 735.0));  // 23
        PATH.add(new TilePosition(310.0, 735.0));  // 24

        // --- 4. SPODNÍ LEVÁ VĚTEV A BOK (Indexy 25 - 33) ---
        PATH.add(new TilePosition(308.0, 625.0));  // 25
        PATH.add(new TilePosition(198.0, 623.0));  // 26
        PATH.add(new TilePosition(85.0, 624.0));   // 27: Start Rytíř (Hrad)
        PATH.add(new TilePosition(87.0, 513.0));   // 28
        PATH.add(new TilePosition(87.0, 400.0));   // 29: TRUHLA (Levá)
        PATH.add(new TilePosition(88.0, 288.0));   // 30
        PATH.add(new TilePosition(85.0, 178.0));   // 31
        PATH.add(new TilePosition(196.0, 179.0));  // 32
        PATH.add(new TilePosition(312.0, 177.0));  // 33
    }

    public static TilePosition getTile(int index) {
        int safeIndex = (index % PATH.size() + PATH.size()) % PATH.size();
        return PATH.get(safeIndex);
    }

    public static boolean isItemTile(int index) {
        int safeIndex = (index % PATH.size() + PATH.size()) % PATH.size();
        return safeIndex == 4 || safeIndex == 13 || safeIndex == 21 || safeIndex == 30;
    }

    public static int getPathLength() {
        return PATH.size();
    }
}