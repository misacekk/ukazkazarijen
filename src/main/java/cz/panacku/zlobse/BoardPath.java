package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class BoardPath {

    private static final List<TilePosition> PATH = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES = new ArrayList<>();

    private static final int[] PLAYER_START_INDICES = {0, 9, 18, 27};

    static {
        // --- HLAVNÍ DRÁHA (36 POLÍČEK) ---
        PATH.add(new TilePosition(328.0, 34.0));  // 0 - Start P1
        PATH.add(new TilePosition(412.0, 35.0));
        PATH.add(new TilePosition(495.0, 34.0));
        PATH.add(new TilePosition(577.0, 33.0));
        PATH.add(new TilePosition(659.0, 34.0));
        PATH.add(new TilePosition(742.0, 34.0)); // 5 - Truhla
        PATH.add(new TilePosition(824.0, 34.0));
        PATH.add(new TilePosition(905.0, 34.0));
        PATH.add(new TilePosition(987.0, 33.0));
        PATH.add(new TilePosition(1072.0, 33.0)); // 9 - Start P2
        PATH.add(new TilePosition(1071.0, 118.0));
        PATH.add(new TilePosition(1071.0, 195.0));
        PATH.add(new TilePosition(1070.0, 277.0));
        PATH.add(new TilePosition(1071.0, 359.0));
        PATH.add(new TilePosition(1072.0, 440.0)); // 14 - Truhla
        PATH.add(new TilePosition(1070.0, 525.0));
        PATH.add(new TilePosition(1071.0, 607.0));
        PATH.add(new TilePosition(1070.0, 687.0));
        PATH.add(new TilePosition(1070.0, 764.0)); // 18 - Start P3
        PATH.add(new TilePosition(989.0, 764.0));
        PATH.add(new TilePosition(906.0, 764.0));
        PATH.add(new TilePosition(824.0, 764.0));
        PATH.add(new TilePosition(742.0, 764.0));
        PATH.add(new TilePosition(658.0, 764.0));  // 23 - Truhla
        PATH.add(new TilePosition(576.0, 764.0));
        PATH.add(new TilePosition(493.0, 765.0));
        PATH.add(new TilePosition(412.0, 764.0));
        PATH.add(new TilePosition(328.0, 764.0));  // 27 - Start P4
        PATH.add(new TilePosition(329.0, 689.0));
        PATH.add(new TilePosition(327.0, 606.0));
        PATH.add(new TilePosition(328.0, 522.0));
        PATH.add(new TilePosition(328.0, 441.0));
        PATH.add(new TilePosition(328.0, 358.0));  // 32 - Truhla
        PATH.add(new TilePosition(328.0, 277.0));
        PATH.add(new TilePosition(328.0, 195.0));
        PATH.add(new TilePosition(328.0, 118.0));

        // SPAWNY
        List<TilePosition> spawnP1 = List.of(new TilePosition(174.0, 49.0), new TilePosition(243.0, 51.0), new TilePosition(177.0, 114.0), new TilePosition(244.0, 117.0));
        List<TilePosition> spawnP2 = List.of(new TilePosition(1155.0, 46.0), new TilePosition(1223.0, 47.0), new TilePosition(1156.0, 116.0), new TilePosition(1229.0, 117.0));
        List<TilePosition> spawnP3 = List.of(new TilePosition(1154.0, 687.0), new TilePosition(1226.0, 689.0), new TilePosition(1162.0, 758.0), new TilePosition(1226.0, 757.0));
        List<TilePosition> spawnP4 = List.of(new TilePosition(171.0, 691.0), new TilePosition(239.0, 688.0), new TilePosition(175.0, 755.0), new TilePosition(234.0, 756.0));
        SPAWNS.addAll(List.of(spawnP1, spawnP2, spawnP3, spawnP4));

        // CÍLE
        List<TilePosition> finishP1 = List.of(new TilePosition(423.0, 132.0), new TilePosition(502.0, 133.0), new TilePosition(426.0, 201.0), new TilePosition(498.0, 199.0));
        List<TilePosition> finishP2 = List.of(new TilePosition(901.0, 125.0), new TilePosition(966.0, 131.0), new TilePosition(897.0, 196.0), new TilePosition(970.0, 197.0));
        List<TilePosition> finishP3 = List.of(new TilePosition(896.0, 597.0), new TilePosition(970.0, 600.0), new TilePosition(900.0, 668.0), new TilePosition(970.0, 674.0));
        List<TilePosition> finishP4 = List.of(new TilePosition(424.0, 600.0), new TilePosition(493.0, 600.0), new TilePosition(425.0, 671.0), new TilePosition(493.0, 671.0));
        FINISHES.addAll(List.of(finishP1, finishP2, finishP3, finishP4));
    }

    public static TilePosition getTile(int index) {
        int safeIndex = (index % PATH.size() + PATH.size()) % PATH.size();
        return PATH.get(safeIndex);
    }

    public static int getStartTileIndex(int playerSlot) {
        return PLAYER_START_INDICES[playerSlot % PLAYER_START_INDICES.length];
    }

    public static TilePosition getSpawnPosition(int playerSlot, int pawnId) {
        return SPAWNS.get(playerSlot % 4).get(pawnId % 4);
    }

    public static TilePosition getFinishPosition(int playerSlot, int pawnId) {
        return FINISHES.get(playerSlot % 4).get(pawnId % 4);
    }

    public static boolean isItemTile(int index) {
        int safeIndex = (index % PATH.size() + PATH.size()) % PATH.size();
        return safeIndex == 5 || safeIndex == 14 || safeIndex == 23 || safeIndex == 32;
    }

    public static int getPathLength() {
        return PATH.size();
    }
}