package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class BoardPath {
    private static final List<TilePosition> PATH = new ArrayList<>();

    static {
        PATH.add(new TilePosition(306.0, 64.0));
        PATH.add(new TilePosition(420.0, 64.0));
        PATH.add(new TilePosition(530.0, 64.0));
        PATH.add(new TilePosition(642.0, 64.0));
        PATH.add(new TilePosition(754.0, 65.0));
        PATH.add(new TilePosition(865.0, 65.0));
        PATH.add(new TilePosition(976.0, 65.0));
        PATH.add(new TilePosition(1087.0, 64.0));
        PATH.add(new TilePosition(1088.0, 176.0));
        PATH.add(new TilePosition(1199.0, 177.0));
        PATH.add(new TilePosition(1312.0, 176.0));
        PATH.add(new TilePosition(1312.0, 289.0));
        PATH.add(new TilePosition(1313.0, 399.0));
        PATH.add(new TilePosition(1314.0, 511.0));
        PATH.add(new TilePosition(1312.0, 623.0));
        PATH.add(new TilePosition(1201.0, 623.0));
        PATH.add(new TilePosition(1088.0, 623.0));
        PATH.add(new TilePosition(1090.0, 734.0));
        PATH.add(new TilePosition(978.0, 736.0));
        PATH.add(new TilePosition(867.0, 736.0));
        PATH.add(new TilePosition(755.0, 735.0));
        PATH.add(new TilePosition(644.0, 733.0));
        PATH.add(new TilePosition(531.0, 735.0));
        PATH.add(new TilePosition(421.0, 735.0));
        PATH.add(new TilePosition(310.0, 735.0));
        PATH.add(new TilePosition(308.0, 625.0));
        PATH.add(new TilePosition(198.0, 623.0));
        PATH.add(new TilePosition(85.0, 624.0));
        PATH.add(new TilePosition(87.0, 513.0));
        PATH.add(new TilePosition(87.0, 400.0));
        PATH.add(new TilePosition(88.0, 288.0));
        PATH.add(new TilePosition(85.0, 178.0));
        PATH.add(new TilePosition(196.0, 179.0));
        PATH.add(new TilePosition(312.0, 177.0));
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