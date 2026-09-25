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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameController {

    @FXML private VBox selectionContainer;
    @FXML private VBox modeSelectionBox;
    @FXML private VBox heroSelectionBox;
    @FXML private Label selectionTitleLabel;

    // Tlačítka pro 6 hrdinů (3 nahoře, 3 dole)
    @FXML private Button btnRytir, btnKouzelnik, btnBandita;
    @FXML private Button btnNekromant, btnLovkyne, btnPaladinka;

    @FXML private VBox gameContent;
    @FXML private StackPane boardContainer;
    @FXML private VBox itemSelectionOverlay;
    @FXML private HBox itemCardsContainer;
    @FXML private VBox bottomBar;
    @FXML private Label statusLabel;
    @FXML private ImageView itemSlot1;
    @FXML private ImageView itemSlot2;
    @FXML private ImageView itemSlot3;

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

    // Výběr režimů hry (1v1, 1v1v1, 1v1v1v1)
    @FXML private void zvolitRezim2() { startHeroSelection(2); }
    @FXML private void zvolitRezim3() { startHeroSelection(3); }
    @FXML private void zvolitRezim4() { startHeroSelection(4); }

    private void startHeroSelection(int count) {
        this.targetPlayerCount = count;
        if (modeSelectionBox != null) modeSelectionBox.setVisible(false);
        if (heroSelectionBox != null) heroSelectionBox.setVisible(true);
        updateHeroSelectionUI();
    }

    // Handlery pro tlačítka hrdinů
    @FXML private void zvolitRytire() { addPlayerHero(HeroClass.RYTIR); }
    @FXML private void zvolitKouzelnika() { addPlayerHero(HeroClass.KOUZELNIK); }
    @FXML private void zvolitBanditu() { addPlayerHero(HeroClass.BANDITA); }
    @FXML private void zvolitNekromanta() { addPlayerHero(HeroClass.NEKROMANT); }
    @FXML private void zvolitLovkyni() { addPlayerHero(HeroClass.LOVKYNE); }
    @FXML private void zvolitPaladinku() { addPlayerHero(HeroClass.PALADINKA); }

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
        disableIfTaken(btnKouzelnik, HeroClass.KOUZELNIK);
        disableIfTaken(btnBandita, HeroClass.BANDITA);
        disableIfTaken(btnNekromant, HeroClass.NEKROMANT);
        disableIfTaken(btnLovkyne, HeroClass.LOVKYNE);
        disableIfTaken(btnPaladinka, HeroClass.PALADINKA);
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
                    statusLabel.setText("Hráč " + p.getId() + " vynechává kolo kvůli Krádeži tahu!");
                }
            } else if (!p.isFinished()) {
                break;
            }
        } while (attempts < players.size() * 2);

        if (getCurrentPlayer().isFinished()) {
            if (statusLabel != null) statusLabel.setText("Hra skončila! Všichni hráči dokončili hru.");
            return;
        }

        startTurn();
    }

    @FXML
    private void hoditKostkou() {
        Player p = getCurrentPlayer();
        if (p.hasRolledThisTurn()) {
            if (statusLabel != null) statusLabel.setText("V tomto tahu už jsi házel!");
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

            int requiredSpawnRoll = (p.getHeroClass() == HeroClass.PALADINKA) ? 5 : 6;
            canSpawnCurrentTurn = (diceValue >= requiredSpawnRoll) && (p.getPawnInHouse() != null);
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
                    statusLabel.setText("Hodil jsi " + diceValue + "! Klikni na figurku na spawnu nebo na desce, se kterou chceš jet.");
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
                } else {
                    if (statusLabel != null) {
                        statusLabel.setText("🏠 Figurka Hráče " + p.getId() + " dorazila do domečku! (" + p.getScore() + "/4)");
                    }
                }
            } else {
                if (statusLabel != null) {
                    statusLabel.setText("Figurka je před domečkem! Pro vstup potřebuješ hodit 1.");
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
            if (pawn.isAtEntranceTile()) {
                if (statusLabel != null) {
                    statusLabel.setText("Figurka obkroužila desku a stojí před domečkem!");
                }
                nextTurn();
            } else if (diceValue == 6) {
                p.setHasRolledThisTurn(false);
                if (statusLabel != null) statusLabel.setText("Hodil jsi 6! Házíš znovu.");
            } else {
                nextTurn();
            }
        });
    }

    private void checkKickout(Pawn attackerPawn) {
        if (attackerPawn.isInHouse() || attackerPawn.isInFinish()) return;

        for (Player defender : players) {
            if (defender == attackerPawn.getOwner()) continue;

            for (Pawn defenderPawn : defender.getPawns()) {
                if (!defenderPawn.isInHouse() && !defenderPawn.isInFinish()) {
                    if (defenderPawn.getCurrentTileIndex() == attackerPawn.getCurrentTileIndex()) {
                        if (defender.getHeroClass() == HeroClass.RYTIR) {
                            if (statusLabel != null) {
                                statusLabel.setText("🛡️ Hráč " + defender.getId() + " (Rytíř) zblokoval vyhození Ocelovým štítem!");
                            }
                            continue;
                        }

                        if (defender.getHeroClass() == HeroClass.NEKROMANT) {
                            defenderPawn.setInHouse(false);
                            defenderPawn.stepToTile(defender.getHeroClass().getStartIndex());
                            defenderPawn.setTotalStepsMoved(0);
                            updatePawnPosition(defenderPawn);

                            if (statusLabel != null) {
                                statusLabel.setText("💀 Nekromantova figurka byla vyhozena, ale díky Reanimaci se ihned objevila na startu!");
                            }
                        } else {
                            defenderPawn.resetToSpawn();
                            updatePawnPosition(defenderPawn);

                            if (statusLabel != null) {
                                statusLabel.setText("💥 Figurka Hráče " + attackerPawn.getOwner().getId() + " vyhodila figurku Hráče " + defender.getId() + "!");
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void pouzitSchopnost() {
        Player p = getCurrentPlayer();
        if (p.hasUsedAbility()) {
            if (statusLabel != null) statusLabel.setText("Svou speciální schopnost jsi již v této hře použil!");
            return;
        }

        switch (p.getHeroClass()) {
            case RYTIR:
            case NEKROMANT:
            case PALADINKA:
                if (statusLabel != null) statusLabel.setText(p.getHeroClass().getName() + " má pasivní schopnost.");
                break;

            case KOUZELNIK:
                p.setDoubleRollActive(true);
                p.setAbilityUsed(true);
                if (statusLabel != null) statusLabel.setText("Kouzelník seslal Dvojitý hod! Následující hod se zdvojnásobí.");
                break;

            case BANDITA:
                int nextIdx = (currentPlayerIndex + 1) % players.size();
                Player victim = players.get(nextIdx);
                victim.setSkipped(true);
                p.setHasRolledThisTurn(false);
                p.setAbilityUsed(true);
                if (statusLabel != null) statusLabel.setText("Bandita ukradl tah Hráči " + victim.getId() + "! Házíš znovu.");
                break;

            case LOVKYNE:
                Pawn targetEnemyPawn = findTargetForLovkyne(p);
                if (targetEnemyPawn != null) {
                    targetEnemyPawn.resetToSpawn();
                    updatePawnPosition(targetEnemyPawn);
                    p.setAbilityUsed(true);
                    if (statusLabel != null) {
                        statusLabel.setText("🎯 Lovkyně použila Přesnou mušku a odstřelila soupeřovu figurku na spawn!");
                    }
                } else {
                    if (statusLabel != null) statusLabel.setText("Žádná soupeřova figurka není v dosahu Přesné mušky (do 6 políček před tebou).");
                }
                break;
        }
    }

    private Pawn findTargetForLovkyne(Player lovkynePlayer) {
        for (Pawn myPawn : lovkynePlayer.getPawnsOnBoard()) {
            for (Player other : players) {
                if (other == lovkynePlayer) continue;
                for (Pawn enemyPawn : other.getPawnsOnBoard()) {
                    int dist = (enemyPawn.getCurrentTileIndex() - myPawn.getCurrentTileIndex() + BoardPath.getPathLength()) % BoardPath.getPathLength();
                    if (dist >= 1 && dist <= 6) {
                        return enemyPawn;
                    }
                }
            }
        }
        return null;
    }

    private void animatePawnMovement(Pawn pawn, int steps, Runnable onComplete) {
        Timeline timeline = new Timeline();

        for (int i = 1; i <= steps; i++) {
            final int stepNum = i;
            KeyFrame kf = new KeyFrame(Duration.millis(220 * stepNum), event -> {
                pawn.stepToTile(pawn.getCurrentTileIndex() + 1);
                pawn.addSteps(1);
                updatePawnPosition(pawn);
            });
            timeline.getKeyFrames().add(kf);
        }

        timeline.setOnFinished(event -> {
            Player p = pawn.getOwner();
            if (BoardPath.isItemTile(pawn.getCurrentTileIndex())) {
                if (p.getInventory().size() < 3) {
                    Item item = Item.getRandomItem();
                    p.addItem(item);
                    updateInventoryUI();
                    if (statusLabel != null) statusLabel.setText("Stoupl jsi na truhlu a získal: " + item.getName());
                }
            }
            if (onComplete != null) onComplete.run();
        });

        timeline.play();
    }

    private void updatePawnPosition(Pawn pawn) {
        ImageView token = pawnTokenMap.get(pawn);
        if (token == null) return;

        double offset = TOKEN_SIZE / 2.0;

        if (pawn.isInHouse()) {
            double homeX = pawn.getOwner().getHeroClass().getHomeX();
            double homeY = pawn.getOwner().getHeroClass().getHomeY();
            double offsetX = (pawn.getId() % 2) * 30.0;
            double offsetY = (pawn.getId() / 2) * 30.0;
            token.setLayoutX(homeX - offset + offsetX);
            token.setLayoutY(homeY - offset + offsetY);
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

        if (inventory.isEmpty()) {
            Label emptyLabel = new Label("V inventáři nemáš žádné předměty.");
            emptyLabel.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 16px;");
            itemCardsContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Item item : inventory) {
            VBox card = new VBox(10);
            card.setAlignment(Pos.CENTER);
            card.setPrefSize(200, 240);
            card.setStyle("-fx-background-color: #2c2c2e; -fx-border-color: #FFD700; -fx-border-radius: 8px; -fx-padding: 10px;");

            ImageView imgView = new ImageView();
            InputStream is = getClass().getResourceAsStream("/images/" + item.getImageFileName());
            if (is != null) imgView.setImage(new Image(is));
            imgView.setFitWidth(45);
            imgView.setFitHeight(45);

            Label nameLabel = new Label(item.getName());
            nameLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;");

            Label descLabel = new Label(item.getDescription());
            descLabel.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");
            descLabel.setWrapText(true);

            Button useBtn = new Button("Použít");
            useBtn.setOnAction(e -> {
                p.getInventory().remove(item);
                applyItemEffect(item);
                updateInventoryUI();
                zavritVyberPredmetu();
            });

            card.getChildren().addAll(imgView, nameLabel, descLabel, useBtn);
            itemCardsContainer.getChildren().add(card);
        }
    }

    private void applyItemEffect(Item item) {
        Player p = getCurrentPlayer();
        switch (item) {
            case TELEPORT:
                Pawn myPawn = p.getPawnsOnBoard().isEmpty() ? null : p.getPawnsOnBoard().get(0);
                Pawn targetPawn = null;
                for (Player other : players) {
                    if (other != p && !other.getPawnsOnBoard().isEmpty()) {
                        targetPawn = other.getPawnsOnBoard().get(0);
                        break;
                    }
                }
                if (myPawn != null && targetPawn != null) {
                    int tempTile = myPawn.getCurrentTileIndex();
                    int tempSteps = myPawn.getTotalStepsMoved();

                    myPawn.stepToTile(targetPawn.getCurrentTileIndex());
                    myPawn.setTotalStepsMoved(targetPawn.getTotalStepsMoved());

                    targetPawn.stepToTile(tempTile);
                    targetPawn.setTotalStepsMoved(tempSteps);

                    updatePawnPosition(myPawn);
                    updatePawnPosition(targetPawn);
                    if (statusLabel != null) statusLabel.setText("Prohodil jsi pozici figurky s soupeřem!");
                }
                break;

            case STASTNA_SEDMA:
                if (!p.getPawnsOnBoard().isEmpty()) {
                    Pawn target = p.getPawnsOnBoard().get(0);
                    int stepsToEntrance = target.getStepsToEntrance();
                    int actualSteps = Math.min(7, stepsToEntrance);
                    animatePawnMovement(target, actualSteps, () -> checkKickout(target));
                    if (statusLabel != null) statusLabel.setText("Použil jsi Šťastnou sedmu!");
                }
                break;

            case CMAJZ:
                Player victim = null;
                for (Player other : players) {
                    if (other != p && !other.getInventory().isEmpty()) {
                        victim = other;
                        break;
                    }
                }
                if (victim != null) {
                    Item stolen = victim.getInventory().remove(0);
                    p.addItem(stolen);
                    updateInventoryUI();
                    if (statusLabel != null) statusLabel.setText("Čmajzl jsi předmět " + stolen.getName() + " Hráči " + victim.getId() + "!");
                }
                break;
        }
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
        InputStream is = getClass().getResourceAsStream("/images/board.png");
        if (is != null) boardImage.setImage(new Image(is));
        boardImage.setFitWidth(BOARD_WIDTH);
        boardImage.setFitHeight(BOARD_HEIGHT);

        tokenLayer = new Pane();
        tokenLayer.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);

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
        alert.setContentText("Hráč " + vitez.getId() + " (" + vitez.getHeroClass().getName()
                + ") úspěšně dovedl všechny 4 figurky do domečku a VYHRÁVÁ HRU!");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                App.zmenScenu("/Menu.fxml");
            }
        });
    }

    @FXML
    private void zpetDoMenu() {
        App.zmenScenu("/Menu.fxml");
    }
}