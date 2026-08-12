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
import java.util.List;
import java.util.Random;

public class GameController {

    @FXML private VBox selectionContainer;
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
    private ImageView playerToken;
    private Player player;
    private final Random random = new Random();

    private static final double TOKEN_SIZE = 40.0;

    @FXML
    public void initialize() {
        if (gameContent != null) gameContent.setVisible(true);
        setupBoardUI();

        if (selectionContainer != null) selectionContainer.setVisible(true);
        if (bottomBar != null) bottomBar.setVisible(false);
        if (itemSelectionOverlay != null) itemSelectionOverlay.setVisible(false);
    }

    // --- HANDLERY PRO VOLBU HRDINŮ ---

    @FXML private void zvolitRytire() { selectHero(HeroClass.RYTIR); }
    @FXML private void zvolitKouzelnika() { selectHero(HeroClass.KOUZELNIK); }
    @FXML private void zvolitBanditu() { selectHero(HeroClass.BANDITA); }
    @FXML private void zvolitNekromanta() { selectHero(HeroClass.NEKROMANT); }

    // --- HANDLERY PRO HRU A OVLÁDÁNÍ ---

    @FXML
    private void hoditKostkou() {
        if (player == null) return;

        if (player.hasRolledThisTurn()) {
            if (statusLabel != null) {
                statusLabel.setText("V tomto tahu už jsi házel!");
            }
            return;
        }

        player.setHasRolledThisTurn(true);
        int diceValue = random.nextInt(6) + 1;

        if (player.isInHouse()) {
            if (diceValue == 6) {
                // Při hození 6 vyskočí z domečku na své startovní políčko
                player.stepToTile(player.getHeroClass().getStartIndex());
                updateTokenPosition();
                player.setHasRolledThisTurn(false);

                if (statusLabel != null) {
                    statusLabel.setText("Hodil jsi 6! Hráč " + player.getHeroClass().getName() + " vyjel z domečku na startovní políčko.");
                }
            } else {
                player.setHasRolledThisTurn(false);
                if (statusLabel != null) {
                    statusLabel.setText("Hodil jsi " + diceValue + ". Pro nasazení z domečku musíš hodit 6!");
                }
            }
        } else {
            if (statusLabel != null) {
                statusLabel.setText("Hráč " + player.getHeroClass().getName() + " hodil: " + diceValue);
            }
            animateMovement(diceValue);
        }
    }

    @FXML
    private void pouzitPredmet() {
        if (itemSelectionOverlay != null) {
            populateItemSelectionOverlay();
            itemSelectionOverlay.setVisible(true);
        }
    }

    @FXML
    private void zavritVyberPredmetu() {
        if (itemSelectionOverlay != null) {
            itemSelectionOverlay.setVisible(false);
        }
    }

    @FXML
    private void pouzitSchopnost() {
        if (statusLabel != null && player != null) {
            statusLabel.setText("Použita schopnost hrdiny: " + player.getHeroClass().getName());
        }
    }

    @FXML
    private void zpetDoMenu() {
        App.zmenScenu("/Menu.fxml");
    }

    // --- LOGIKA A UI HERNÍ DESKY ---

    private void selectHero(HeroClass selectedHero) {
        player = new Player(selectedHero);

        if (selectionContainer != null) selectionContainer.setVisible(false);
        if (bottomBar != null) bottomBar.setVisible(true);

        InputStream is = getClass().getResourceAsStream("/images/" + selectedHero.getImageFileName());
        if (is != null && playerToken != null) {
            playerToken.setImage(new Image(is));
        }

        updateTokenPosition();
        updateInventoryUI();

        if (statusLabel != null) {
            statusLabel.setText("Na tahu: " + player.getHeroClass().getName() + " (Musíš hodit 6 pro vyjetí z domečku)");
        }
    }

    private void setupBoardUI() {
        if (boardContainer == null) return;

        boardContainer.getChildren().clear();

        ImageView boardImage = new ImageView();
        InputStream is = getClass().getResourceAsStream("/images/board.png");
        if (is != null) {
            boardImage.setImage(new Image(is));
        }

        boardImage.setFitWidth(1200);
        boardImage.setFitHeight(700);
        boardImage.setPreserveRatio(true);

        tokenLayer = new Pane();
        tokenLayer.setPrefSize(1200, 700);
        tokenLayer.setMaxSize(1200, 700);

        playerToken = new ImageView();
        playerToken.setFitWidth(TOKEN_SIZE);
        playerToken.setFitHeight(TOKEN_SIZE);
        playerToken.setPreserveRatio(true);

        tokenLayer.getChildren().add(playerToken);
        boardContainer.getChildren().addAll(boardImage, tokenLayer);
    }

    private void animateMovement(int steps) {
        Timeline timeline = new Timeline();

        for (int i = 1; i <= steps; i++) {
            final int stepNumber = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(250 * stepNumber), event -> {
                player.stepToTile(player.getCurrentTileIndex() + 1);
                updateTokenPosition();
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setOnFinished(event -> {
            player.setHasRolledThisTurn(false);

            // Kontrola dopadu na truhlu (index 4 nebo index 15)
            if (!player.isInHouse() && BoardPath.isItemTile(player.getCurrentTileIndex())) {
                if (player.getInventory().size() < 3) {
                    Item newItem = Item.getRandomItem();
                    player.addItem(newItem);
                    updateInventoryUI();
                    if (statusLabel != null) {
                        statusLabel.setText("Našel jsi truhlu! Získal jsi: " + newItem.getName());
                    }
                } else if (statusLabel != null) {
                    statusLabel.setText("Našel jsi truhlu, ale inventář máš plný!");
                }
            }
        });

        timeline.play();
    }

    private void updateInventoryUI() {
        if (player == null) return;

        ImageView[] slots = {itemSlot1, itemSlot2, itemSlot3};
        List<Item> inventory = player.getInventory();

        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) continue;

            if (i < inventory.size()) {
                Item item = inventory.get(i);
                InputStream is = getClass().getResourceAsStream("/images/" + item.getImageFileName());
                if (is != null) {
                    slots[i].setImage(new Image(is));
                } else {
                    slots[i].setImage(null);
                }
            } else {
                slots[i].setImage(null);
            }
        }
    }

    private void populateItemSelectionOverlay() {
        if (itemCardsContainer == null || player == null) return;

        itemCardsContainer.getChildren().clear();
        List<Item> inventory = player.getInventory();

        if (inventory.isEmpty()) {
            Label emptyLabel = new Label("V inventáři zatím nemáš žádné předměty.");
            emptyLabel.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 16px; -fx-font-style: italic;");
            itemCardsContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Item item : inventory) {
            VBox card = new VBox(12);
            card.setAlignment(Pos.CENTER);
            card.setPrefSize(220, 260);
            card.setStyle("-fx-background-color: #2c2c2e; -fx-border-color: #FFD700; -fx-border-width: 1.5px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");

            ImageView imgView = new ImageView();
            InputStream is = getClass().getResourceAsStream("/images/" + item.getImageFileName());
            if (is != null) {
                imgView.setImage(new Image(is));
            }
            imgView.setFitWidth(50);
            imgView.setFitHeight(50);
            imgView.setPreserveRatio(true);

            Label nameLabel = new Label(item.getName());
            nameLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 16px;");

            Label descLabel = new Label(item.getDescription());
            descLabel.setStyle("-fx-text-fill: #d1d1d6; -fx-font-size: 12px; -fx-text-alignment: center;");
            descLabel.setWrapText(true);
            descLabel.setMaxWidth(190);

            Button useBtn = new Button("Použít");
            useBtn.setStyle("-fx-background-color: #2ed573; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 4px; -fx-padding: 6px 16px;");
            useBtn.setOnAction(e -> {
                player.getInventory().remove(item);
                updateInventoryUI();
                zavritVyberPredmetu();
                if (statusLabel != null) {
                    statusLabel.setText("Použil jsi předmět: " + item.getName());
                }
            });

            card.getChildren().addAll(imgView, nameLabel, descLabel, useBtn);
            itemCardsContainer.getChildren().add(card);
        }
    }

    private void updateTokenPosition() {
        if (playerToken == null || player == null) return;

        double offset = TOKEN_SIZE / 2.0;

        if (player.isInHouse()) {
            playerToken.setLayoutX(player.getHeroClass().getHomeX() - offset);
            playerToken.setLayoutY(player.getHeroClass().getHomeY() - offset);
        } else {
            TilePosition tile = BoardPath.getTile(player.getCurrentTileIndex());
            playerToken.setLayoutX(tile.getX() - offset);
            playerToken.setLayoutY(tile.getY() - offset);
        }
    }
}