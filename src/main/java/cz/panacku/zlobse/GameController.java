package cz.panacku.zlobse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.InputStream;
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
    private Circle playerToken;
    private Player player;
    private final Random random = new Random();

    @FXML
    public void initialize() {
        // Herní deska se vykreslí ihned na pozadí
        if (gameContent != null) gameContent.setVisible(true);
        setupBoardUI();

        // Výběr postav se zobrazí v popředí přes desku
        if (selectionContainer != null) selectionContainer.setVisible(true);

        // Dolní lišta se odkryje až po výboru postavy
        if (bottomBar != null) bottomBar.setVisible(false);
        if (itemSelectionOverlay != null) itemSelectionOverlay.setVisible(false);
    }

    // --- HANDLERY PRO VOLBU HRDINŮ ---

    @FXML
    private void zvolitRytire() {
        selectHero(HeroClass.RYTIR);
    }

    @FXML
    private void zvolitKouzelnika() {
        selectHero(HeroClass.KOUZELNIK);
    }

    @FXML
    private void zvolitBanditu() {
        selectHero(HeroClass.BANDITA);
    }

    @FXML
    private void zvolitNekromanta() {
        selectHero(HeroClass.NEKROMANT);
    }

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

        if (statusLabel != null) {
            statusLabel.setText("Hráč " + player.getHeroClass().getName() + " hodil: " + diceValue);
        }

        animateMovement(diceValue);
    }

    @FXML
    private void pouzitPredmet() {
        if (itemSelectionOverlay != null) {
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

        // Skryje overlay s výběrem postav a zobrazí dolní lištu (inventář, kostku)
        if (selectionContainer != null) selectionContainer.setVisible(false);
        if (bottomBar != null) bottomBar.setVisible(true);

        updateTokenPosition();

        if (statusLabel != null) {
            statusLabel.setText("Na tahu: " + player.getHeroClass().getName());
        }
    }

    private void setupBoardUI() {
        if (boardContainer == null) return;

        boardContainer.getChildren().clear();

        ImageView boardImage = new ImageView();

        // Zkustí načíst obrázek z classpath (složka resources)
        InputStream is = getClass().getResourceAsStream("/images/board.png");

        if (is != null) {
            boardImage.setImage(new Image(is));
            System.out.println("Obrázek desky byl úspěšně načten.");
        } else {
            System.err.println("CHYBA: Soubor /herni_deska.png nebyl nalezen ve složce resources!");
        }

        boardImage.setFitWidth(1200);
        boardImage.setFitHeight(700);
        boardImage.setPreserveRatio(true);

        tokenLayer = new Pane();
        tokenLayer.setPrefSize(1200, 700);
        tokenLayer.setMaxSize(1200, 700);

        playerToken = new Circle(14);
        playerToken.setFill(Color.RED);
        playerToken.setStroke(Color.WHITE);
        playerToken.setStrokeWidth(2.5);

        tokenLayer.getChildren().add(playerToken);
        boardContainer.getChildren().addAll(boardImage, tokenLayer);
    }

    private void animateMovement(int steps) {
        Timeline timeline = new Timeline();

        for (int i = 1; i <= steps; i++) {
            final int stepNumber = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(250 * stepNumber), event -> {
                if (player.isInHouse()) {
                    player.stepToTile(player.getHeroClass().getStartIndex());
                } else {
                    player.stepToTile(player.getCurrentTileIndex() + 1);
                }
                updateTokenPosition();
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setOnFinished(event -> {
            player.setHasRolledThisTurn(false);
        });

        timeline.play();
    }

    private void updateTokenPosition() {
        if (playerToken == null || player == null) return;

        if (player.isInHouse()) {
            playerToken.setLayoutX(player.getHeroClass().getHomeX());
            playerToken.setLayoutY(player.getHeroClass().getHomeY());
        } else {
            TilePosition tile = BoardPath.getTile(player.getCurrentTileIndex());
            playerToken.setLayoutX(tile.getX());
            playerToken.setLayoutY(tile.getY());
        }
    }
}