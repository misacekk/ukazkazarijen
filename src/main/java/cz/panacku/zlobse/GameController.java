package cz.panacku.zlobse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import java.util.List;
import java.util.Random;

public class GameController {

    @FXML private VBox selectionContainer;
    @FXML private VBox modeSelectionBox;
    @FXML private VBox heroSelectionBox;
    @FXML private Label selectionTitleLabel;
    @FXML private Button btnRytir, btnKouzelnik, btnBandita, btnNekromant;

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
    private final List<ImageView> playerTokens = new ArrayList<>();
    private int currentPlayerIndex = 0;

    private final Random random = new Random();

    private static final double BOARD_WIDTH = 1400.0;
    private static final double BOARD_HEIGHT = 800.0;
    private static final double TOKEN_SIZE = 50.0;
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
    @FXML private void zvolitRezim4() { startHeroSelection(4); }

    private void startHeroSelection(int count) {
        this.targetPlayerCount = count;
        if (modeSelectionBox != null) modeSelectionBox.setVisible(false);
        if (heroSelectionBox != null) heroSelectionBox.setVisible(true);
        updateHeroSelectionUI();
    }

    @FXML private void zvolitRytire() { addPlayerHero(HeroClass.RYTIR); }
    @FXML private void zvolitKouzelnika() { addPlayerHero(HeroClass.KOUZELNIK); }
    @FXML private void zvolitBanditu() { addPlayerHero(HeroClass.BANDITA); }
    @FXML private void zvolitNekromanta() { addPlayerHero(HeroClass.NEKROMANT); }

    private void addPlayerHero(HeroClass heroClass) {
        int newPlayerId = players.size() + 1;
        Player p = new Player(newPlayerId, heroClass);
        players.add(p);

        ImageView tokenView = new ImageView();
        tokenView.setFitWidth(TOKEN_SIZE);
        tokenView.setFitHeight(TOKEN_SIZE);
        tokenView.setPreserveRatio(true);

        InputStream is = getClass().getResourceAsStream("/images/" + heroClass.getImageFileName());
        if (is != null) tokenView.setImage(new Image(is));

        playerTokens.add(tokenView);
        if (tokenLayer != null) tokenLayer.getChildren().add(tokenView);

        updateSingleTokenPosition(p, tokenView);

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
    }

    private void disableIfTaken(Button btn, HeroClass heroClass) {
        if (btn == null) return;
        boolean taken = players.stream().anyMatch(p -> p.getHeroClass() == heroClass);
        btn.setDisable(taken);
    }

    private Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    private ImageView getCurrentToken() { return playerTokens.get(currentPlayerIndex); }

    private void startTurn() {
        Player p = getCurrentPlayer();
        p.resetTurn();
        updateInventoryUI();
        if (statusLabel != null) {
            statusLabel.setText("Na tahu je Hráč " + p.getId() + " (" + p.getHeroClass().getName() + ")");
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
            int finalDiceValue = random.nextInt(6) + 1;
            setDiceImage(finalDiceValue);

            if (p.isInHouse()) {
                if (finalDiceValue == 6) {
                    p.setInHouse(false);
                    updateSingleTokenPosition(p, getCurrentToken());
                    p.setHasRolledThisTurn(false);
                    checkKickout(p);
                    if (statusLabel != null) statusLabel.setText("Hráč " + p.getId() + " hodil 6 a vyjel na hrací plochu! Hází znovu.");
                } else {
                    if (statusLabel != null) statusLabel.setText("Hráč " + p.getId() + " hodil " + finalDiceValue + ". Pro vyjetí z domečku potřebuješ 6.");
                    nextTurn();
                }
            } else {
                int steps = finalDiceValue;
                if (p.isDoubleRollActive()) {
                    steps *= 2;
                    p.setDoubleRollActive(false);
                    if (statusLabel != null) statusLabel.setText("Dvojitý hod aktivován! Posun o " + steps + " políček.");
                }

                animateMovement(p, getCurrentToken(), steps, () -> {
                    checkKickout(p);
                    if (finalDiceValue == 6) {
                        p.setHasRolledThisTurn(false);
                        if (statusLabel != null) statusLabel.setText("Hodil jsi 6! Házíš znovu.");
                    } else {
                        nextTurn();
                    }
                });
            }
        });

        diceAnimation.play();
    }

    private void checkKickout(Player attacker) {
        if (attacker.isInHouse() || attacker.isFinished()) return;

        for (Player defender : players) {
            if (defender != attacker && !defender.isInHouse() && !defender.isFinished()) {
                if (defender.getCurrentTileIndex() == attacker.getCurrentTileIndex()) {
                    defender.setInHouse(true);
                    int defenderIndex = players.indexOf(defender);
                    updateSingleTokenPosition(defender, playerTokens.get(defenderIndex));

                    if (statusLabel != null) {
                        statusLabel.setText("💥 Hráč " + attacker.getId() + " (" + attacker.getHeroClass().getName()
                                + ") vyhodil Hráče " + defender.getId() + " (" + defender.getHeroClass().getName() + ") zpět na spawn!");
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
                if (statusLabel != null) statusLabel.setText("Rytíř má pasivní schopnost.");
                break;

            case KOUZELNIK:
                p.setDoubleRollActive(true);
                p.setAbilityUsed(true); // Označeno jako použité
                if (statusLabel != null) statusLabel.setText("Kouzelník seslal Dvojitý hod! Tvůj následující hod bude zdvojnásoben. (Schopnost vyčerpána)");
                break;

            case BANDITA:
                int nextIdx = (currentPlayerIndex + 1) % players.size();
                Player victim = players.get(nextIdx);
                victim.setSkipped(true);
                p.setHasRolledThisTurn(false);
                p.setAbilityUsed(true); // Označeno jako použité
                if (statusLabel != null) statusLabel.setText("Bandita ukradl tah Hráči " + victim.getId() + "! Házíš znovu a soupeř bude stát. (Schopnost vyčerpána)");
                break;

            case NEKROMANT:
                if (statusLabel != null) statusLabel.setText("Nekromant má pasivní schopnost.");
                break;
        }
    }

    private void animateMovement(Player player, ImageView token, int steps, Runnable onComplete) {
        Timeline timeline = new Timeline();

        for (int i = 1; i <= steps; i++) {
            final int stepNum = i;
            KeyFrame kf = new KeyFrame(Duration.millis(220 * stepNum), event -> {
                player.stepToTile(player.getCurrentTileIndex() + 1);
                player.addSteps(1);
                updateSingleTokenPosition(player, token);
            });
            timeline.getKeyFrames().add(kf);
        }

        timeline.setOnFinished(event -> {
            if (BoardPath.isItemTile(player.getCurrentTileIndex())) {
                if (player.getInventory().size() < 3) {
                    Item item = Item.getRandomItem();
                    player.addItem(item);
                    updateInventoryUI();
                    if (statusLabel != null) statusLabel.setText("Stoupl jsi na truhlu a získal: " + item.getName());
                }
            }
            if (onComplete != null) onComplete.run();
        });

        timeline.play();
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
                Player target = null;
                for (Player other : players) {
                    if (other != p && !other.isInHouse() && !other.isFinished()) {
                        target = other;
                        break;
                    }
                }
                if (target != null) {
                    int tempTile = p.getCurrentTileIndex();
                    p.stepToTile(target.getCurrentTileIndex());
                    target.stepToTile(tempTile);

                    updateSingleTokenPosition(p, getCurrentToken());
                    updateSingleTokenPosition(target, playerTokens.get(players.indexOf(target)));
                    if (statusLabel != null) statusLabel.setText("Prohodil jsi si pozici s Hráčem " + target.getId() + "!");
                }
                break;

            case STASTNA_SEDMA:
                if (!p.isInHouse() && !p.isFinished()) {
                    animateMovement(p, getCurrentToken(), 7, () -> checkKickout(p));
                    if (statusLabel != null) statusLabel.setText("Použil jsi Šťastnou sedmu a posunul se o 7 políček!");
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

    private void updateSingleTokenPosition(Player p, ImageView token) {
        if (token == null) return;
        double offset = TOKEN_SIZE / 2.0;

        if (p.isInHouse()) {
            token.setLayoutX(p.getHeroClass().getHomeX() - offset);
            token.setLayoutY(p.getHeroClass().getHomeY() - offset);
        } else {
            TilePosition tile = BoardPath.getTile(p.getCurrentTileIndex());
            token.setLayoutX(tile.getX() - offset);
            token.setLayoutY(tile.getY() - offset);
        }
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

    @FXML
    private void zpetDoMenu() {
        App.zmenScenu("/Menu.fxml");
    }
}