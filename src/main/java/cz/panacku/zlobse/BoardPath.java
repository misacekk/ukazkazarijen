package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class BoardPath {

    private static int activePlayerCount = 4; // Výchozí režim

    // --- SOUŘADNICE PRO 4 HRÁČE (1v1v1v1 / 1v1) ---
    private static final List<TilePosition> PATH_4P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_4P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_4P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_4P = {0, 9, 18, 27};

    // --- SOUŘADNICE PRO 3 HRÁČE (1v1v1) ---
    private static final List<TilePosition> PATH_3P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_3P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_3P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_3P = {0, 8, 16};

    static {
        init4PlayerPath();
        init3PlayerPath();
    }

    private static void init4PlayerPath() {
        // HLAVNÍ DRÁHA (36 POLÍČEK)
        PATH_4P.add(new TilePosition(328.0, 34.0));  // 0 - Start P1
        PATH_4P.add(new TilePosition(412.0, 35.0));
        PATH_4P.add(new TilePosition(495.0, 34.0));
        PATH_4P.add(new TilePosition(577.0, 33.0));
        PATH_4P.add(new TilePosition(659.0, 34.0));
        PATH_4P.add(new TilePosition(742.0, 34.0)); // 5 - Truhla
        PATH_4P.add(new TilePosition(824.0, 34.0));
        PATH_4P.add(new TilePosition(905.0, 34.0));
        PATH_4P.add(new TilePosition(987.0, 33.0));
        PATH_4P.add(new TilePosition(1072.0, 33.0)); // 9 - Start P2
        PATH_4P.add(new TilePosition(1071.0, 118.0));
        PATH_4P.add(new TilePosition(1071.0, 195.0));
        PATH_4P.add(new TilePosition(1070.0, 277.0));
        PATH_4P.add(new TilePosition(1071.0, 359.0));
        PATH_4P.add(new TilePosition(1072.0, 440.0)); // 14 - Truhla
        PATH_4P.add(new TilePosition(1070.0, 525.0));
        PATH_4P.add(new TilePosition(1071.0, 607.0));
        PATH_4P.add(new TilePosition(1070.0, 687.0));
        PATH_4P.add(new TilePosition(1070.0, 764.0)); // 18 - Start P3
        PATH_4P.add(new TilePosition(989.0, 764.0));
        PATH_4P.add(new TilePosition(906.0, 764.0));
        PATH_4P.add(new TilePosition(824.0, 764.0));
        PATH_4P.add(new TilePosition(742.0, 764.0));
        PATH_4P.add(new TilePosition(658.0, 764.0));  // 23 - Truhla
        PATH_4P.add(new TilePosition(576.0, 764.0));
        PATH_4P.add(new TilePosition(493.0, 765.0));
        PATH_4P.add(new TilePosition(412.0, 764.0));
        PATH_4P.add(new TilePosition(328.0, 764.0));  // 27 - Start P4
        PATH_4P.add(new TilePosition(329.0, 689.0));
        PATH_4P.add(new TilePosition(327.0, 606.0));
        PATH_4P.add(new TilePosition(328.0, 522.0));
        PATH_4P.add(new TilePosition(328.0, 441.0));
        PATH_4P.add(new TilePosition(328.0, 358.0));  // 32 - Truhla
        PATH_4P.add(new TilePosition(328.0, 277.0));
        PATH_4P.add(new TilePosition(328.0, 195.0));
        PATH_4P.add(new TilePosition(328.0, 118.0));

        // SPAWNY
        List<TilePosition> spawnP1 = List.of(new TilePosition(174.0, 49.0), new TilePosition(243.0, 51.0), new TilePosition(177.0, 114.0), new TilePosition(244.0, 117.0));
        List<TilePosition> spawnP2 = List.of(new TilePosition(1155.0, 46.0), new TilePosition(1223.0, 47.0), new TilePosition(1156.0, 116.0), new TilePosition(1229.0, 117.0));
        List<TilePosition> spawnP3 = List.of(new TilePosition(1154.0, 687.0), new TilePosition(1226.0, 689.0), new TilePosition(1162.0, 758.0), new TilePosition(1226.0, 757.0));
        List<TilePosition> spawnP4 = List.of(new TilePosition(171.0, 691.0), new TilePosition(239.0, 688.0), new TilePosition(175.0, 755.0), new TilePosition(234.0, 756.0));
        SPAWNS_4P.addAll(List.of(spawnP1, spawnP2, spawnP3, spawnP4));

        // CÍLE
        List<TilePosition> finishP1 = List.of(new TilePosition(423.0, 132.0), new TilePosition(502.0, 133.0), new TilePosition(426.0, 201.0), new TilePosition(498.0, 199.0));
        List<TilePosition> finishP2 = List.of(new TilePosition(901.0, 125.0), new TilePosition(966.0, 131.0), new TilePosition(897.0, 196.0), new TilePosition(970.0, 197.0));
        List<TilePosition> finishP3 = List.of(new TilePosition(896.0, 597.0), new TilePosition(970.0, 600.0), new TilePosition(900.0, 668.0), new TilePosition(970.0, 674.0));
        List<TilePosition> finishP4 = List.of(new TilePosition(424.0, 600.0), new TilePosition(493.0, 600.0), new TilePosition(425.0, 671.0), new TilePosition(493.0, 671.0));
        FINISHES_4P.addAll(List.of(finishP1, finishP2, finishP3, finishP4));
    }

    private static void init3PlayerPath() {
        // HLAVNÍ DRÁHA (24 POLÍČEK - Kliky 1–24)
        PATH_3P.add(new TilePosition(700.0, 172.0)); // 0 - Start P1
        PATH_3P.add(new TilePosition(762.0, 215.0));
        PATH_3P.add(new TilePosition(828.0, 253.0));
        PATH_3P.add(new TilePosition(896.0, 292.0));
        PATH_3P.add(new TilePosition(963.0, 330.0));
        PATH_3P.add(new TilePosition(964.0, 397.0));
        PATH_3P.add(new TilePosition(963.0, 475.0));
        PATH_3P.add(new TilePosition(963.0, 551.0));
        PATH_3P.add(new TilePosition(963.0, 628.0)); // 8 - Start P2
        PATH_3P.add(new TilePosition(904.0, 667.0));
        PATH_3P.add(new TilePosition(843.0, 712.0));
        PATH_3P.add(new TilePosition(774.0, 750.0));
        PATH_3P.add(new TilePosition(700.0, 765.0));
        PATH_3P.add(new TilePosition(632.0, 749.0));
        PATH_3P.add(new TilePosition(565.0, 706.0));
        PATH_3P.add(new TilePosition(496.0, 665.0));
        PATH_3P.add(new TilePosition(436.0, 627.0)); // 16 - Start P3
        PATH_3P.add(new TilePosition(436.0, 551.0));
        PATH_3P.add(new TilePosition(436.0, 475.0));
        PATH_3P.add(new TilePosition(436.0, 399.0));
        PATH_3P.add(new TilePosition(437.0, 330.0));
        PATH_3P.add(new TilePosition(505.0, 292.0));
        PATH_3P.add(new TilePosition(572.0, 253.0));
        PATH_3P.add(new TilePosition(637.0, 216.0));

        // SPAWNY (Kliky 25–36)
        List<TilePosition> spawnP1 = List.of(
                new TilePosition(664.0, 40.0),
                new TilePosition(733.0, 40.0),
                new TilePosition(665.0, 105.0),
                new TilePosition(729.0, 103.0)
        );
        List<TilePosition> spawnP2 = List.of(
                new TilePosition(1032.0, 643.0),
                new TilePosition(1097.0, 671.0),
                new TilePosition(1004.0, 691.0),
                new TilePosition(1060.0, 727.0)
        );
        List<TilePosition> spawnP3 = List.of(
                new TilePosition(304.0, 668.0),
                new TilePosition(364.0, 637.0),
                new TilePosition(339.0, 729.0),
                new TilePosition(396.0, 698.0)
        );
        SPAWNS_3P.addAll(List.of(spawnP1, spawnP2, spawnP3));

        // CÍLE (Kliky 37–47)
        // Cíl P1 (Kliky 37–40)
        List<TilePosition> finishP1 = List.of(
                new TilePosition(699.0, 297.0),
                new TilePosition(650.0, 372.0),
                new TilePosition(700.0, 373.0),
                new TilePosition(747.0, 374.0)
        );
        // Cíl P2 (Kliky 45–47 + doplněná symetrická středová pozice 814, 536)
        List<TilePosition> finishP2 = List.of(
                new TilePosition(815.0, 484.0),
                new TilePosition(814.0, 536.0),
                new TilePosition(769.0, 577.0),
                new TilePosition(858.0, 577.0)
        );
        // Cíl P3 (Kliky 41–44)
        List<TilePosition> finishP3 = List.of(
                new TilePosition(523.0, 580.0),
                new TilePosition(571.0, 493.0),
                new TilePosition(589.0, 536.0),
                new TilePosition(621.0, 579.0)
        );
        FINISHES_3P.addAll(List.of(finishP1, finishP2, finishP3));
    }

    public static void setPlayerCount(int count) {
        activePlayerCount = count;
    }

    private static boolean is3PlayerReady() {
        return activePlayerCount == 3 && !PATH_3P.isEmpty() && !SPAWNS_3P.isEmpty() && !FINISHES_3P.isEmpty();
    }

    public static TilePosition getTile(int index) {
        List<TilePosition> path = is3PlayerReady() ? PATH_3P : PATH_4P;
        int safeIndex = (index % path.size() + path.size()) % path.size();
        return path.get(safeIndex);
    }

    public static int getStartTileIndex(int playerSlot) {
        int[] indices = is3PlayerReady() ? PLAYER_START_INDICES_3P : PLAYER_START_INDICES_4P;
        return indices[playerSlot % indices.length];
    }

    public static TilePosition getSpawnPosition(int playerSlot, int pawnId) {
        List<List<TilePosition>> spawns = is3PlayerReady() ? SPAWNS_3P : SPAWNS_4P;
        return spawns.get(playerSlot % spawns.size()).get(pawnId % 4);
    }

    public static TilePosition getFinishPosition(int playerSlot, int pawnId) {
        List<List<TilePosition>> finishes = is3PlayerReady() ? FINISHES_3P : FINISHES_4P;
        return finishes.get(playerSlot % finishes.size()).get(pawnId % 4);
    }

    public static boolean isItemTile(int index) {
        List<TilePosition> path = is3PlayerReady() ? PATH_3P : PATH_4P;
        int safeIndex = (index % path.size() + path.size()) % path.size();

        if (is3PlayerReady()) {
            return safeIndex == 4 || safeIndex == 12 || safeIndex == 20;
        } else {
            return safeIndex == 5 || safeIndex == 14 || safeIndex == 23 || safeIndex == 32;
        }
    }

    public static int getPathLength() {
        List<TilePosition> path = is3PlayerReady() ? PATH_3P : PATH_4P;
        return path.size();
    }
}