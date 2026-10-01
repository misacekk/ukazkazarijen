package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class BoardPath {

    private static int activePlayerCount = 4; // Výchozí režim

    // --- SOUŘADNICE PRO 2 HRÁČE (1v1) ---
    private static final List<TilePosition> PATH_2P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_2P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_2P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_2P = {0, 15};

    // --- SOUŘADNICE PRO 3 HRÁČE (1v1v1) ---
    private static final List<TilePosition> PATH_3P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_3P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_3P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_3P = {0, 8, 16};

    // --- SOUŘADNICE PRO 4 HRÁČE (1v1v1v1) ---
    private static final List<TilePosition> PATH_4P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_4P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_4P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_4P = {0, 9, 18, 27};

    static {
        init2PlayerPath();
        init3PlayerPath();
        init4PlayerPath();
    }

    private static void init2PlayerPath() {
        // HLAVNÍ DRÁHA (30 POLÍČEK)
        PATH_2P.add(new TilePosition(212.0, 400.0)); // 0 - Start P1
        PATH_2P.add(new TilePosition(241.0, 327.0));
        PATH_2P.add(new TilePosition(302.0, 267.0));
        PATH_2P.add(new TilePosition(370.0, 227.0));
        PATH_2P.add(new TilePosition(444.0, 198.0));
        PATH_2P.add(new TilePosition(516.0, 177.0)); // 5 - Truhla 1
        PATH_2P.add(new TilePosition(594.0, 168.0));
        PATH_2P.add(new TilePosition(668.0, 168.0));
        PATH_2P.add(new TilePosition(743.0, 167.0));
        PATH_2P.add(new TilePosition(817.0, 178.0));
        PATH_2P.add(new TilePosition(893.0, 186.0)); // 10 - Truhla 2
        PATH_2P.add(new TilePosition(964.0, 208.0));
        PATH_2P.add(new TilePosition(1041.0, 228.0));
        PATH_2P.add(new TilePosition(1111.0, 268.0));
        PATH_2P.add(new TilePosition(1157.0, 330.0));
        PATH_2P.add(new TilePosition(1188.0, 399.0)); // 15 - Start P2
        PATH_2P.add(new TilePosition(1158.0, 477.0));
        PATH_2P.add(new TilePosition(1099.0, 536.0));
        PATH_2P.add(new TilePosition(1028.0, 575.0));
        PATH_2P.add(new TilePosition(953.0, 605.0));
        PATH_2P.add(new TilePosition(879.0, 627.0)); // 20 - Truhla 3
        PATH_2P.add(new TilePosition(804.0, 634.0));
        PATH_2P.add(new TilePosition(730.0, 635.0));
        PATH_2P.add(new TilePosition(656.0, 634.0));
        PATH_2P.add(new TilePosition(581.0, 624.0));
        PATH_2P.add(new TilePosition(506.0, 613.0)); // 25 - Truhla 4
        PATH_2P.add(new TilePosition(433.0, 596.0));
        PATH_2P.add(new TilePosition(360.0, 574.0));
        PATH_2P.add(new TilePosition(288.0, 535.0));
        PATH_2P.add(new TilePosition(242.0, 468.0));

        // SPAWNY (P1 a P2)
        List<TilePosition> spawnP1 = List.of(
                new TilePosition(67.0, 365.0),
                new TilePosition(137.0, 371.0),
                new TilePosition(75.0, 433.0),
                new TilePosition(139.0, 435.0)
        );
        List<TilePosition> spawnP2 = List.of(
                new TilePosition(1254.0, 368.0),
                new TilePosition(1321.0, 368.0),
                new TilePosition(1254.0, 432.0),
                new TilePosition(1322.0, 434.0)
        );
        SPAWNS_2P.addAll(List.of(spawnP1, spawnP2));

        // CÍLE (P1 a P2)
        List<TilePosition> finishP1 = List.of(
                new TilePosition(313.0, 368.0),
                new TilePosition(379.0, 370.0),
                new TilePosition(313.0, 435.0),
                new TilePosition(380.0, 435.0)
        );
        List<TilePosition> finishP2 = List.of(
                new TilePosition(996.0, 370.0),
                new TilePosition(1065.0, 373.0),
                new TilePosition(1000.0, 441.0),
                new TilePosition(1066.0, 443.0)
        );
        FINISHES_2P.addAll(List.of(finishP1, finishP2));
    }

    private static void init3PlayerPath() {
        // HLAVNÍ DRÁHA (24 POLÍČEK)
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

        // SPAWNY
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

        // CÍLE
        List<TilePosition> finishP1 = List.of(
                new TilePosition(699.0, 297.0),
                new TilePosition(650.0, 372.0),
                new TilePosition(700.0, 373.0),
                new TilePosition(747.0, 374.0)
        );
        List<TilePosition> finishP2 = List.of(
                new TilePosition(815.0, 484.0),
                new TilePosition(814.0, 536.0),
                new TilePosition(769.0, 577.0),
                new TilePosition(858.0, 577.0)
        );
        List<TilePosition> finishP3 = List.of(
                new TilePosition(523.0, 580.0),
                new TilePosition(571.0, 493.0),
                new TilePosition(589.0, 536.0),
                new TilePosition(621.0, 579.0)
        );
        FINISHES_3P.addAll(List.of(finishP1, finishP2, finishP3));
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

    public static void setPlayerCount(int count) {
        activePlayerCount = count;
    }

    private static boolean is2PlayerReady() {
        return activePlayerCount == 2 && !PATH_2P.isEmpty() && !SPAWNS_2P.isEmpty() && !FINISHES_2P.isEmpty();
    }

    private static boolean is3PlayerReady() {
        return activePlayerCount == 3 && !PATH_3P.isEmpty() && !SPAWNS_3P.isEmpty() && !FINISHES_3P.isEmpty();
    }

    private static List<TilePosition> getActivePath() {
        if (is2PlayerReady()) return PATH_2P;
        if (is3PlayerReady()) return PATH_3P;
        return PATH_4P;
    }

    public static TilePosition getTile(int index) {
        List<TilePosition> path = getActivePath();
        int safeIndex = (index % path.size() + path.size()) % path.size();
        return path.get(safeIndex);
    }

    public static int getStartTileIndex(int playerSlot) {
        int[] indices = is2PlayerReady() ? PLAYER_START_INDICES_2P : (is3PlayerReady() ? PLAYER_START_INDICES_3P : PLAYER_START_INDICES_4P);
        return indices[playerSlot % indices.length];
    }

    public static TilePosition getSpawnPosition(int playerSlot, int pawnId) {
        List<List<TilePosition>> spawns = is2PlayerReady() ? SPAWNS_2P : (is3PlayerReady() ? SPAWNS_3P : SPAWNS_4P);
        return spawns.get(playerSlot % spawns.size()).get(pawnId % 4);
    }

    public static TilePosition getFinishPosition(int playerSlot, int pawnId) {
        List<List<TilePosition>> finishes = is2PlayerReady() ? FINISHES_2P : (is3PlayerReady() ? FINISHES_3P : FINISHES_4P);
        return finishes.get(playerSlot % finishes.size()).get(pawnId % 4);
    }

    public static boolean isItemTile(int index) {
        List<TilePosition> path = getActivePath();
        int safeIndex = (index % path.size() + path.size()) % path.size();

        if (is2PlayerReady()) {
            return safeIndex == 5 || safeIndex == 10 || safeIndex == 20 || safeIndex == 25;
        } else if (is3PlayerReady()) {
            return safeIndex == 4 || safeIndex == 12 || safeIndex == 20;
        } else {
            return safeIndex == 5 || safeIndex == 14 || safeIndex == 23 || safeIndex == 32;
        }
    }

    public static int getPathLength() {
        return getActivePath().size();
    }
}