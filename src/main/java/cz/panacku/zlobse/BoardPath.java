package cz.panacku.zlobse;

import java.util.ArrayList;
import java.util.List;

public class BoardPath {

    private static int activePlayerCount = 4;

    private static final List<TilePosition> PATH_2P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_2P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_2P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_2P = {0, 11};

    private static final List<TilePosition> PATH_3P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_3P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_3P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_3P = {0, 10, 20};

    private static final List<TilePosition> PATH_4P = new ArrayList<>();
    private static final List<List<TilePosition>> SPAWNS_4P = new ArrayList<>();
    private static final List<List<TilePosition>> FINISHES_4P = new ArrayList<>();
    private static final int[] PLAYER_START_INDICES_4P = {0, 10, 20, 30};

    static {
        init2PlayerPath();
        init3PlayerPath();
        init4PlayerPath();
    }

    private static void init2PlayerPath() {
        PATH_2P.add(new TilePosition(409.0, 103.0));
        PATH_2P.add(new TilePosition(486.0, 132.0));
        PATH_2P.add(new TilePosition(559.0, 183.0));
        PATH_2P.add(new TilePosition(617.0, 234.0));
        PATH_2P.add(new TilePosition(662.0, 294.0));
        PATH_2P.add(new TilePosition(696.0, 365.0));
        PATH_2P.add(new TilePosition(696.0, 446.0));
        PATH_2P.add(new TilePosition(671.0, 520.0));
        PATH_2P.add(new TilePosition(611.0, 579.0));
        PATH_2P.add(new TilePosition(542.0, 618.0));
        PATH_2P.add(new TilePosition(468.0, 666.0));
        PATH_2P.add(new TilePosition(391.0, 698.0));
        PATH_2P.add(new TilePosition(313.0, 670.0));
        PATH_2P.add(new TilePosition(243.0, 616.0));
        PATH_2P.add(new TilePosition(182.0, 567.0));
        PATH_2P.add(new TilePosition(138.0, 505.0));
        PATH_2P.add(new TilePosition(104.0, 435.0));
        PATH_2P.add(new TilePosition(102.0, 354.0));
        PATH_2P.add(new TilePosition(132.0, 281.0));
        PATH_2P.add(new TilePosition(190.0, 222.0));
        PATH_2P.add(new TilePosition(258.0, 182.0));
        PATH_2P.add(new TilePosition(329.0, 131.0));

        List<TilePosition> spawnP1 = List.of(
                new TilePosition(351.0, 40.0), new TilePosition(411.0, 40.0),
                new TilePosition(351.0, 80.0), new TilePosition(411.0, 80.0)
        );
        List<TilePosition> spawnP2 = List.of(
                new TilePosition(351.0, 720.0), new TilePosition(411.0, 720.0),
                new TilePosition(351.0, 760.0), new TilePosition(411.0, 760.0)
        );
        SPAWNS_2P.addAll(List.of(spawnP1, spawnP2));

        List<TilePosition> finishP1 = List.of(
                new TilePosition(361.0, 224.0), new TilePosition(445.0, 224.0),
                new TilePosition(365.0, 299.0), new TilePosition(448.0, 304.0)
        );
        List<TilePosition> finishP2 = List.of(
                new TilePosition(351.0, 481.0), new TilePosition(443.0, 493.0),
                new TilePosition(359.0, 571.0), new TilePosition(447.0, 579.0)
        );
        FINISHES_2P.addAll(List.of(finishP1, finishP2));
    }

    private static void init3PlayerPath() {
        PATH_3P.add(new TilePosition(399.0, 36.0));
        PATH_3P.add(new TilePosition(477.0, 64.0));
        PATH_3P.add(new TilePosition(538.0, 100.0));
        PATH_3P.add(new TilePosition(599.0, 135.0));
        PATH_3P.add(new TilePosition(664.0, 173.0));
        PATH_3P.add(new TilePosition(730.0, 224.0));
        PATH_3P.add(new TilePosition(728.0, 295.0));
        PATH_3P.add(new TilePosition(728.0, 366.0));
        PATH_3P.add(new TilePosition(729.0, 431.0));
        PATH_3P.add(new TilePosition(729.0, 502.0));
        PATH_3P.add(new TilePosition(730.0, 572.0));
        PATH_3P.add(new TilePosition(665.0, 621.0));
        PATH_3P.add(new TilePosition(605.0, 658.0));
        PATH_3P.add(new TilePosition(544.0, 691.0));
        PATH_3P.add(new TilePosition(479.0, 729.0));
        PATH_3P.add(new TilePosition(399.0, 758.0));
        PATH_3P.add(new TilePosition(324.0, 730.0));
        PATH_3P.add(new TilePosition(262.0, 696.0));
        PATH_3P.add(new TilePosition(201.0, 662.0));
        PATH_3P.add(new TilePosition(136.0, 623.0));
        PATH_3P.add(new TilePosition(72.0, 571.0));
        PATH_3P.add(new TilePosition(71.0, 499.0));
        PATH_3P.add(new TilePosition(71.0, 429.0));
        PATH_3P.add(new TilePosition(73.0, 363.0));
        PATH_3P.add(new TilePosition(71.0, 296.0));
        PATH_3P.add(new TilePosition(76.0, 222.0));
        PATH_3P.add(new TilePosition(134.0, 173.0));
        PATH_3P.add(new TilePosition(195.0, 138.0));
        PATH_3P.add(new TilePosition(255.0, 103.0));
        PATH_3P.add(new TilePosition(321.0, 65.0));

        List<TilePosition> spawnP1 = List.of(
                new TilePosition(371.0, 10.0), new TilePosition(431.0, 10.0),
                new TilePosition(371.0, 40.0), new TilePosition(431.0, 40.0)
        );
        List<TilePosition> spawnP2 = List.of(
                new TilePosition(761.0, 590.0), new TilePosition(791.0, 590.0),
                new TilePosition(761.0, 620.0), new TilePosition(791.0, 620.0)
        );
        List<TilePosition> spawnP3 = List.of(
                new TilePosition(11.0, 590.0), new TilePosition(41.0, 590.0),
                new TilePosition(11.0, 620.0), new TilePosition(41.0, 620.0)
        );
        SPAWNS_3P.addAll(List.of(spawnP1, spawnP2, spawnP3));

        List<TilePosition> finishP1 = List.of(
                new TilePosition(357.0, 160.0), new TilePosition(434.0, 161.0),
                new TilePosition(368.0, 232.0), new TilePosition(437.0, 232.0)
        );
        List<TilePosition> finishP2 = List.of(
                new TilePosition(554.0, 440.0), new TilePosition(629.0, 441.0),
                new TilePosition(556.0, 516.0), new TilePosition(633.0, 517.0)
        );
        List<TilePosition> finishP3 = List.of(
                new TilePosition(161.0, 440.0), new TilePosition(241.0, 446.0),
                new TilePosition(163.0, 513.0), new TilePosition(244.0, 521.0)
        );
        FINISHES_3P.addAll(List.of(finishP1, finishP2, finishP3));
    }

    private static void init4PlayerPath() {
        PATH_4P.add(new TilePosition(46.0, 47.0));
        PATH_4P.add(new TilePosition(114.0, 47.0));
        PATH_4P.add(new TilePosition(184.0, 46.0));
        PATH_4P.add(new TilePosition(254.0, 47.0));
        PATH_4P.add(new TilePosition(326.0, 47.0));
        PATH_4P.add(new TilePosition(396.0, 47.0));
        PATH_4P.add(new TilePosition(465.0, 49.0));
        PATH_4P.add(new TilePosition(537.0, 46.0));
        PATH_4P.add(new TilePosition(607.0, 47.0));
        PATH_4P.add(new TilePosition(676.0, 48.0));
        PATH_4P.add(new TilePosition(754.0, 47.0));
        PATH_4P.add(new TilePosition(753.0, 119.0));
        PATH_4P.add(new TilePosition(754.0, 190.0));
        PATH_4P.add(new TilePosition(753.0, 259.0));
        PATH_4P.add(new TilePosition(754.0, 330.0));
        PATH_4P.add(new TilePosition(754.0, 401.0));
        PATH_4P.add(new TilePosition(754.0, 471.0));
        PATH_4P.add(new TilePosition(754.0, 542.0));
        PATH_4P.add(new TilePosition(754.0, 611.0));
        PATH_4P.add(new TilePosition(754.0, 682.0));
        PATH_4P.add(new TilePosition(754.0, 753.0));
        PATH_4P.add(new TilePosition(677.0, 753.0));
        PATH_4P.add(new TilePosition(608.0, 752.0));
        PATH_4P.add(new TilePosition(538.0, 752.0));
        PATH_4P.add(new TilePosition(465.0, 753.0));
        PATH_4P.add(new TilePosition(396.0, 753.0));
        PATH_4P.add(new TilePosition(325.0, 753.0));
        PATH_4P.add(new TilePosition(254.0, 752.0));
        PATH_4P.add(new TilePosition(184.0, 753.0));
        PATH_4P.add(new TilePosition(113.0, 752.0));
        PATH_4P.add(new TilePosition(46.0, 752.0));
        PATH_4P.add(new TilePosition(47.0, 682.0));
        PATH_4P.add(new TilePosition(46.0, 612.0));
        PATH_4P.add(new TilePosition(46.0, 540.0));
        PATH_4P.add(new TilePosition(45.0, 472.0));
        PATH_4P.add(new TilePosition(46.0, 401.0));
        PATH_4P.add(new TilePosition(46.0, 330.0));
        PATH_4P.add(new TilePosition(46.0, 259.0));
        PATH_4P.add(new TilePosition(45.0, 187.0));
        PATH_4P.add(new TilePosition(44.0, 118.0));

        List<TilePosition> spawnP1 = List.of(new TilePosition(-25.0, 49.0), new TilePosition(1.0, 49.0), new TilePosition(-25.0, 80.0), new TilePosition(1.0, 80.0));
        List<TilePosition> spawnP2 = List.of(new TilePosition(801.0, 49.0), new TilePosition(831.0, 49.0), new TilePosition(801.0, 80.0), new TilePosition(831.0, 80.0));
        List<TilePosition> spawnP3 = List.of(new TilePosition(801.0, 720.0), new TilePosition(831.0, 720.0), new TilePosition(801.0, 750.0), new TilePosition(831.0, 750.0));
        List<TilePosition> spawnP4 = List.of(new TilePosition(-25.0, 720.0), new TilePosition(1.0, 720.0), new TilePosition(-25.0, 750.0), new TilePosition(1.0, 750.0));
        SPAWNS_4P.addAll(List.of(spawnP1, spawnP2, spawnP3, spawnP4));

        List<TilePosition> finishP1 = List.of(
                new TilePosition(153.0, 161.0), new TilePosition(230.0, 159.0),
                new TilePosition(151.0, 234.0), new TilePosition(230.0, 241.0)
        );
        List<TilePosition> finishP2 = List.of(
                new TilePosition(552.0, 156.0), new TilePosition(638.0, 158.0),
                new TilePosition(561.0, 238.0), new TilePosition(641.0, 239.0)
        );
        List<TilePosition> finishP3 = List.of(
                new TilePosition(555.0, 567.0), new TilePosition(641.0, 565.0),
                new TilePosition(558.0, 642.0), new TilePosition(638.0, 646.0)
        );
        List<TilePosition> finishP4 = List.of(
                new TilePosition(150.0, 565.0), new TilePosition(234.0, 566.0),
                new TilePosition(146.0, 647.0), new TilePosition(230.0, 651.0)
        );
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
            return safeIndex == 6 || safeIndex == 17;
        } else if (is3PlayerReady()) {
            return safeIndex == 5 || safeIndex == 15 || safeIndex == 25;
        } else {
            return safeIndex == 5 || safeIndex == 15 || safeIndex == 25 || safeIndex == 35;
        }
    }

    public static int getPathLength() {
        return getActivePath().size();
    }
}