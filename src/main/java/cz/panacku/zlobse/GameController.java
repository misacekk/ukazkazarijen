package cz.panacku.zlobse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.*;

public class GameController {

    @FXML private VBox selectionContainer;
    @FXML private VBox modeSelectionBox;
    @FXML private VBox heroSelectionBox;
    @FXML private Label selectionTitleLabel;

    @FXML private Button btnRytir, btnCarodejnice, btnBandita;
    @FXML private Button btnNekromant, btnElfka, btnKralovna;

    @FXML private VBox gameContent;
    @FXML private StackPane boardContainer;
    @FXML private VBox itemSelectionOverlay;
    @FXML private HBox itemCardsContainer;
    @FXML private VBox bottomBar;
    @FXML private Label statusLabel;
    @FXML private ImageView itemSlot1, itemSlot2, itemSlot3;

    private Pane tokenLayer;
    private ImageView diceImageView;

    private int targetPlayerCount = 2;
    private final List<Player> players = new ArrayList<>();
    private final Map<Pawn, ImageView> pawnTokenMap = new HashMap<>();
    private int currentPlayerIndex = 0;

    private boolean waitingForPawnSelection = false;
    private List<Pawn> movablePawnsCurrentTurn = new ArrayList<>();
    private boolean canSpawnCurrentTurn = false;
    private int lastRolledDiceValue = 0;

    private final Random random = new Random();

    private static final double BOARD_WIDTH = 1400.0;
    private static final double BOARD_HEIGHT = 800.0;
    private static final double TOKEN_SIZE = 45.0;
    private static final double DICE_SIZE = 100.0;

    @FXML
    public void initialize() {
        if (gameContent != null) gameContent.setVisible(true);
        if (selectionContainer != null) selectionContainer.setVisible(true);
        if (modeSelectionBox != null) modeSelectionBox.setVisible(true);
        if (heroSelectionBox != null) heroSelectionBox.setVisible(false);
        if (bottomBar != null) bottomBar.setVisible(false);
        if (itemSelectionOverlay != null) itemSelectionOverlay.setVisible(false);

        setupBoardUI();
    }

    @FXML private void zvolitRezim2() { startHeroSelection(2); }
    @FXML private void zvolitRezim3() { startHeroSelection(3); }
    @FXML private void zvolitRezim4() { startHeroSelection(4); }

    private void startHeroSelection(int count) {
        this.targetPlayerCount = count;
        if (modeSelectionBox != null) modeSelectionBox.setVisible(false);
        if (heroSelectionBox != null) heroSelectionBox.setVisible(true);
        updateHeroSelectionUI();
    }

    @FXML private void zvolitRytire() { addPlayerHero(HeroClass.RYTIR); }
    @FXML private void zvolitCarodejnici() { addPlayerHero(HeroClass.CARODEJNICE); }
    @FXML private void zvolitBanditu() { addPlayerHero(HeroClass.BANDITA); }
    @FXML private void zvolitNekromanta() { addPlayerHero(HeroClass.NEKROMANT); }
    @FXML private void zvolitElfku() { addPlayerHero(HeroClass.ELFKA); }
    @FXML private void zvolitKralovnu() { addPlayerHero(HeroClass.KRALOVNA); }

    private void addPlayerHero(HeroClass heroClass) {
        int newPlayerId = players.size() + 1;
        Player p = new Player(newPlayerId, heroClass);
        players.add(p);

        for (Pawn pawn : p.getPawns()) {
            ImageView tokenView = new ImageView();
            tokenView.setFitWidth(TOKEN_SIZE);
            tokenView.setFitHeight(TOKEN_SIZE);
            tokenView.setPreserveRatio(true);

            InputStream is = getClass().getResourceAsStream("/images/" + heroClass.getImageFileName());
            if (is != null) tokenView.setImage(new Image(is));

            tokenView.setOnMouseClicked(e -> handlePawnClick(pawn));

            pawnTokenMap.put(pawn, tokenView);
            if (tokenLayer != null) tokenLayer.getChildren().add(tokenView);

            updatePawnPosition(pawn);
        }

        if (players.size() < targetPlayerCount) {
            updateHeroSelectionUI();
        } else {
            if (selectionContainer != null) selectionContainer.setVisible(false);
            if (bottomBar != null) bottomBar.setVisible(true);
            currentPlayerIndex = 0;
            startTurn();
        }
    }

    private void updateHeroSelectionUI() {
        int nextPlayerNum = players.size() + 1;
        if (selectionTitleLabel != null) {
            selectionTitleLabel.setText("Hráč " + nextPlayerNum + ": Vyber si svého hrdinu");
        }

        disableIfTaken(btnRytir, HeroClass.RYTIR);
        disableIfTaken(btnCarodejnice, HeroClass.CARODEJNICE);
        disableIfTaken(btnBandita, HeroClass.BANDITA);
        disableIfTaken(btnNekromant, HeroClass.NEKROMANT);
        disableIfTaken(btnElfka, HeroClass.ELFKA);
        disableIfTaken(btnKralovna, HeroClass.KRALOVNA);
    }

    private void disableIfTaken(Button btn, HeroClass heroClass) {
        if (btn == null) return;
        boolean taken = players.stream().anyMatch(p -> p.getHeroClass() == heroClass);
        btn.setDisable(taken);
    }

    private Player getCurrentPlayer() { return players.get(currentPlayerIndex); }

    private void startTurn() {
        Player p = getCurrentPlayer();
        p.resetTurn();
        waitingForPawnSelection = false;
        movablePawnsCurrentTurn.clear();
        canSpawnCurrentTurn = false;
        updateInventoryUI();

        if (statusLabel != null) {
            statusLabel.setText("Na tahu je Hráč " + p.getId() + " (" + p.getHeroClass().getName() + ") | Domečky: " + p.getScore() + "/" + Player.TARGET_SCORE);
        }
    }

    private void nextTurn() {
        int attempts = 0;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            attempts++;
            Player p = getCurrentPlayer();

            if (p.isSkipped()) {
                p.setSkipped(false);
                if (statusLabel != null) {
                    statusLabel.setText("Hráč " + p.getId() + " vynechává kolo!");
                }
            } else if (!p.isFinished()) {
                break;
            }
        } while (attempts < players.size() * 2);

        if (getCurrentPlayer().isFinished()) {
            if (statusLabel != null) statusLabel.setText("Hra skončila!");
            return;
        }

        startTurn();
    }

    @FXML
    private void hoditKostkou() {
        Player p = getCurrentPlayer();
        if (p.hasRolledThisTurn()) {
            if (statusLabel != null) statusLabel.setText("V tomto tahu už jsi házel nebo použil předmět!");
            return;
        }

        p.setHasRolledThisTurn(true);

        Timeline diceAnimation = new Timeline();
        for (int i = 0; i < 10; i++) {
            KeyFrame kf = new KeyFrame(Duration.millis(80 * (i + 1)), event -> setDiceImage(random.nextInt(6) + 1));
            diceAnimation.getKeyFrames().add(kf);
        }

        diceAnimation.setOnFinished(event -> {
            int diceValue = random.nextInt(6) + 1;
            setDiceImage(diceValue);
            this.lastRolledDiceValue = diceValue;

            canSpawnCurrentTurn = (diceValue == 6) && (p.getPawnInHouse() != null);
            movablePawnsCurrentTurn = p.getPawnsOnBoard();

            if (!canSpawnCurrentTurn && movablePawnsCurrentTurn.isEmpty()) {
                if (statusLabel != null) {
                    statusLabel.setText("Hráč " + p.getId() + " hodil " + diceValue + ", ale nemá žádný možný tah.");
                }
                nextTurn();
                return;
            }

            if (canSpawnCurrentTurn && movablePawnsCurrentTurn.isEmpty()) {
                executeSpawn(p.getPawnInHouse(), diceValue);
            } else if (!canSpawnCurrentTurn && movablePawnsCurrentTurn.size() == 1) {
                executePawnMove(movablePawnsCurrentTurn.get(0), diceValue);
            } else {
                waitingForPawnSelection = true;
                if (statusLabel != null) {
                    statusLabel.setText("Hodil jsi " + diceValue + "! Klikni na figurku na spawnu nebo na desce.");
                }
            }
        });

        diceAnimation.play();
    }

    private void handlePawnClick(Pawn clickedPawn) {
        if (!waitingForPawnSelection) return;

        Player p = getCurrentPlayer();
        if (clickedPawn.getOwner() != p) return;

        if (clickedPawn.isInHouse()) {
            if (canSpawnCurrentTurn) {
                waitingForPawnSelection = false;
                executeSpawn(clickedPawn, lastRolledDiceValue);
            }
        } else if (!clickedPawn.isInFinish()) {
            if (movablePawnsCurrentTurn.contains(clickedPawn)) {
                waitingForPawnSelection = false;
                executePawnMove(clickedPawn, lastRolledDiceValue);
            }
        }
    }

    private void executeSpawn(Pawn pawn, int diceValue) {
        Player p = pawn.getOwner();
        pawn.setInHouse(false);
        updatePawnPosition(pawn);
        checkKickout(pawn);

        if (statusLabel != null) {
            statusLabel.setText("Hráč " + p.getId() + " nasadil novou figurku! Hází znovu.");
        }
        p.setHasRolledThisTurn(false);
    }

    private void executePawnMove(Pawn pawn, int diceValue) {
        Player p = pawn.getOwner();

        if (pawn.isAtEntranceTile()) {
            if (diceValue == 1) {
                pawn.setInFinish(true);
                updatePawnPosition(pawn);

                if (p.isFinished()) {
                    zobrazitVitezneOkno(p);
                    return;
                }
            }
            nextTurn();
            return;
        }

        int steps = diceValue;
        if (p.isDoubleRollActive()) {
            steps *= 2;
            p.setDoubleRollActive(false);
        }

        int stepsToEntrance = pawn.getStepsToEntrance();
        int actualSteps = Math.min(steps, stepsToEntrance);

        animatePawnMovement(pawn, actualSteps, () -> {
            checkKickout(pawn);
            checkItemPickUp(pawn);

            if (diceValue == 6) {
                p.setHasRolledThisTurn(false);
                if (statusLabel != null && !BoardPath.isItemTile(pawn.getCurrentTileIndex())) {
                    statusLabel.setText("Hodil jsi 6! Házíš znovu.");
                }
            } else {
                nextTurn();
            }
        });
    }

    private void checkItemPickUp(Pawn pawn) {
        if (pawn.isInHouse() || pawn.isInFinish()) return;

        if (BoardPath.isItemTile(pawn.getCurrentTileIndex())) {
            Player owner = pawn.getOwner();
            if (owner.getInventory().size() < 3) {
                Item reward = Item.getRandomItem();
                owner.addItem(reward);
                updateInventoryUI();
                if (statusLabel != null) {
                    statusLabel.setText("Hráč " + owner.getId() + " otevřel truhlu a získal: " + reward.getName() + "!");
                }
            } else {
                if (statusLabel != null) {
                    statusLabel.setText("Hráč " + owner.getId() + " stoupl na truhlu, ale má plný inventář (3/3)!");
                }
            }
        }
    }

    private void checkKickout(Pawn attackerPawn) {
        if (attackerPawn.isInHouse() || attackerPawn.isInFinish()) return;

        for (Player defender : players) {
            if (defender == attackerPawn.getOwner()) continue;

            for (Pawn defenderPawn : defender.getPawns()) {
                if (!defenderPawn.isInHouse() && !defenderPawn.isInFinish()) {
                    if (defenderPawn.getCurrentTileIndex() == attackerPawn.getCurrentTileIndex()) {
                        if (defender.getHeroClass() == HeroClass.RYTIR) continue;

                        if (defender.getHeroClass() == HeroClass.NEKROMANT) {
                            defenderPawn.setInHouse(false);
                            defenderPawn.stepToTile(defender.getStartTileIndex());
                            defenderPawn.setTotalStepsMoved(0);
                            updatePawnPosition(defenderPawn);
                        } else {
                            defenderPawn.resetToSpawn();
                            updatePawnPosition(defenderPawn);
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void pouzitSchopnost() {
        Player p = getCurrentPlayer();
        if (p.hasUsedAbility()) return;

        switch (p.getHeroClass()) {
            case CARODEJNICE:
                p.setDoubleRollActive(true);
                p.setAbilityUsed(true);
                break;
            case BANDITA:
                int nextIdx = (currentPlayerIndex + 1) % players.size();
                Player victim = players.get(nextIdx);
                if (victim.getHeroClass() != HeroClass.KRALOVNA) {
                    victim.setSkipped(true);
                    p.setHasRolledThisTurn(false);
                    p.setAbilityUsed(true);
                }
                break;
            case ELFKA:
                Pawn targetEnemyPawn = findTargetForElfka(p);
                if (targetEnemyPawn != null && targetEnemyPawn.getOwner().getHeroClass() != HeroClass.KRALOVNA) {
                    targetEnemyPawn.resetToSpawn();
                    updatePawnPosition(targetEnemyPawn);
                    p.setAbilityUsed(true);
                }
                break;
            default:
                break;
        }
    }

    private Pawn findTargetForElfka(Player elfkaPlayer) {
        for (Pawn myPawn : elfkaPlayer.getPawnsOnBoard()) {
            for (Player other : players) {
                if (other == elfkaPlayer) continue;
                for (Pawn enemyPawn : other.getPawnsOnBoard()) {
                    int dist = (enemyPawn.getCurrentTileIndex() - myPawn.getCurrentTileIndex() + BoardPath.getPathLength()) % BoardPath.getPathLength();
                    if (dist >= 1 && dist <= 6) return enemyPawn;
                }
            }
        }
        return null;
    }

    private void animatePawnMovement(Pawn pawn, int steps, Runnable onComplete) {
        Timeline timeline = new Timeline();
        for (int i = 1; i <= steps; i++) {
            final int stepNum = i;
            KeyFrame kf = new KeyFrame(Duration.millis(200 * stepNum), event -> {
                pawn.stepToTile(pawn.getCurrentTileIndex() + 1);
                pawn.addSteps(1);
                updatePawnPosition(pawn);
            });
            timeline.getKeyFrames().add(kf);
        }
        timeline.setOnFinished(event -> {
            if (onComplete != null) onComplete.run();
        });
        timeline.play();
    }

    private void updatePawnPosition(Pawn pawn) {
        ImageView token = pawnTokenMap.get(pawn);
        if (token == null) return;

        double offset = TOKEN_SIZE / 2.0;

        if (pawn.isInHouse()) {
            TilePosition spawnPos = BoardPath.getSpawnPosition(pawn.getOwner().getSlotIndex(), pawn.getId());
            token.setLayoutX(spawnPos.getX() - offset);
            token.setLayoutY(spawnPos.getY() - offset);
        } else if (pawn.isInFinish()) {
            TilePosition finishPos = BoardPath.getFinishPosition(pawn.getOwner().getSlotIndex(), pawn.getId());
            token.setLayoutX(finishPos.getX() - offset);
            token.setLayoutY(finishPos.getY() - offset);
        } else {
            TilePosition tile = BoardPath.getTile(pawn.getCurrentTileIndex());
            token.setLayoutX(tile.getX() - offset);
            token.setLayoutY(tile.getY() - offset);
        }
    }

    @FXML
    private void pouzitPredmet() {
        if (itemSelectionOverlay != null) {
            populateItemSelectionOverlay();
            itemSelectionOverlay.setVisible(true);
        }
    }

    @FXML private void zavritVyberPredmetu() { if (itemSelectionOverlay != null) itemSelectionOverlay.setVisible(false); }

    private void populateItemSelectionOverlay() {
        if (itemCardsContainer == null) return;
        itemCardsContainer.getChildren().clear();

        Player p = getCurrentPlayer();
        List<Item> inventory = p.getInventory();

        if (inventory.isEmpty()) return;

        for (Item item : inventory) {
            VBox card = new VBox(8);
            card.setAlignment(Pos.CENTER);
            card.setPrefSize(200, 240);
            card.setStyle("-fx-background-color: #2c2c2e; -fx-border-color: #FFD700; -fx-border-radius: 8px; -fx-padding: 10px;");

            Label nameLabel = new Label(item.getName());
            nameLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 14px;");

            Label descLabel = new Label(item.getDescription());
            descLabel.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 11px;");
            descLabel.setWrapText(true);

            Button useBtn = new Button("Použít");
            useBtn.setOnAction(e -> {
                boolean success = applyItemEffect(item);
                if (success) {
                    p.getInventory().remove(item);
                    updateInventoryUI();
                    zavritVyberPredmetu();
                }
            });

            card.getChildren().addAll(nameLabel, descLabel, useBtn);
            itemCardsContainer.getChildren().add(card);
        }
    }

    // 🎯 AKTIVACE EFEKTŮ PŘEDMĚTŮ
    private boolean applyItemEffect(Item item) {
        Player p = getCurrentPlayer();

        switch (item) {
            case TELEPORT:
                List<Pawn> myPawns = p.getPawnsOnBoard();
                if (myPawns.isEmpty()) {
                    if (statusLabel != null) statusLabel.setText("Nemáš žádnou figurku na desce pro teleportation!");
                    return false;
                }

                Pawn myPawn = myPawns.get(0);
                Pawn closestEnemyPawn = null;
                int minDistance = Integer.MAX_VALUE;

                for (Player enemy : players) {
                    if (enemy == p) continue;
                    for (Pawn enemyPawn : enemy.getPawnsOnBoard()) {
                        int dist = Math.abs(enemyPawn.getCurrentTileIndex() - myPawn.getCurrentTileIndex());
                        if (dist < minDistance) {
                            minDistance = dist;
                            closestEnemyPawn = enemyPawn;
                        }
                    }
                }

                if (closestEnemyPawn == null) {
                    if (statusLabel != null) statusLabel.setText("Žádný soupeř nemá figurku na hrací desce!");
                    return false;
                }

                // Prohození pozic
                int myTile = myPawn.getCurrentTileIndex();
                int mySteps = myPawn.getTotalStepsMoved();

                myPawn.stepToTile(closestEnemyPawn.getCurrentTileIndex());
                myPawn.setTotalStepsMoved(closestEnemyPawn.getTotalStepsMoved());

                closestEnemyPawn.stepToTile(myTile);
                closestEnemyPawn.setTotalStepsMoved(mySteps);

                updatePawnPosition(myPawn);
                updatePawnPosition(closestEnemyPawn);

                if (statusLabel != null) {
                    statusLabel.setText("Prohodil sis pozici s figurkou Hráče " + closestEnemyPawn.getOwner().getId() + "!");
                }
                return true;

            case STASTNA_SEDMA:
                if (p.hasRolledThisTurn()) {
                    if (statusLabel != null) statusLabel.setText("V tomto tahu už jsi házel!");
                    return false;
                }

                List<Pawn> movablePawns = p.getPawnsOnBoard();
                if (movablePawns.isEmpty()) {
                    if (statusLabel != null) statusLabel.setText("Nemáš na desce žádnou figurku pro posun o 7!");
                    return false;
                }

                p.setHasRolledThisTurn(true);
                this.lastRolledDiceValue = 7;

                if (movablePawns.size() == 1) {
                    executePawnMove(movablePawns.get(0), 7);
                } else {
                    waitingForPawnSelection = true;
                    movablePawnsCurrentTurn = movablePawns;
                    if (statusLabel != null) {
                        statusLabel.setText("Šťastná sedma! Klikni na figurku, se kterou se posuneš o 7 políček.");
                    }
                }
                return true;

            case CMAJZ:
                List<Player> validVictims = new ArrayList<>();
                for (Player enemy : players) {
                    if (enemy != p && !enemy.getInventory().isEmpty()) {
                        validVictims.add(enemy);
                    }
                }

                if (validVictims.isEmpty()) {
                    if (statusLabel != null) statusLabel.setText("Žádný soupeř nemá žádné předměty v inventáři!");
                    return false;
                }

                Player victim = validVictims.get(random.nextInt(validVictims.size()));
                Item stolenItem = victim.getInventory().remove(random.nextInt(victim.getInventory().size()));
                p.addItem(stolenItem);

                if (statusLabel != null) {
                    statusLabel.setText("Ukradl jsi předmět " + stolenItem.getName() + " Hráči " + victim.getId() + "!");
                }
                return true;
        }

        return false;
    }

    private void setDiceImage(int diceValue) {
        if (diceImageView == null) return;
        InputStream is = getClass().getResourceAsStream("/images/kostka" + diceValue + ".png");
        if (is != null) diceImageView.setImage(new Image(is));
    }

    private void setupBoardUI() {
        if (boardContainer == null) return;
        boardContainer.getChildren().clear();

        ImageView boardImage = new ImageView();
        InputStream is = getClass().getResourceAsStream("/images/board1.png");
        if (is != null) boardImage.setImage(new Image(is));
        boardImage.setFitWidth(BOARD_WIDTH);
        boardImage.setFitHeight(BOARD_HEIGHT);

        tokenLayer = new Pane();
        tokenLayer.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);

        tokenLayer.setOnMouseClicked(event -> {
            System.out.println("PATH.add(new TilePosition(" + event.getX() + ", " + event.getY() + "));");
        });

        diceImageView = new ImageView();
        diceImageView.setFitWidth(DICE_SIZE);
        diceImageView.setFitHeight(DICE_SIZE);
        diceImageView.setLayoutX((BOARD_WIDTH - DICE_SIZE) / 2.0);
        diceImageView.setLayoutY((BOARD_HEIGHT - DICE_SIZE) / 2.0);

        tokenLayer.getChildren().add(diceImageView);
        boardContainer.getChildren().addAll(boardImage, tokenLayer);
    }

    private void updateInventoryUI() {
        Player p = getCurrentPlayer();
        ImageView[] slots = {itemSlot1, itemSlot2, itemSlot3};
        List<Item> inv = p.getInventory();

        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) continue;
            if (i < inv.size()) {
                InputStream is = getClass().getResourceAsStream("/images/" + inv.get(i).getImageFileName());
                if (is != null) slots[i].setImage(new Image(is));
            } else {
                slots[i].setImage(null);
            }
        }
    }

    private void zobrazitVitezneOkno(Player vitez) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Konec hry!");
        alert.setHeaderText("🎉 VÍTĚZSTVÍ! 🎉");
        alert.setContentText("Hráč " + vitez.getId() + " VYHRÁVÁ HRU!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) App.zmenScenu("/Menu.fxml");
        });
    }

    @FXML private void zpetDoMenu() { App.zmenScenu("/Menu.fxml"); }
}