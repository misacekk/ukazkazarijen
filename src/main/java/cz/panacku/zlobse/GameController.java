package cz.panacku.zlobse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

public class GameController {

    @FXML private Pane boardPane;
    @FXML private Button rollDiceButton;
    @FXML private Button endTurnButton;
    @FXML private Label diceResultLabel;
    @FXML private Label statusLabel;

    private Circle playerToken;
    private Player player;
    private final Random random = new Random();

    @FXML
    public void initialize() {
        // Výchozí hrdina při spuštění hry
        selectHero(HeroClass.NEKROMANT);
    }

    // --- HANDLERY PRO TLAČÍTKA VOLBY HRDINŮ ---

    @FXML
    private void zvolitNekromanta() {
        selectHero(HeroClass.NEKROMANT);
    }

    @FXML
    private void zvolitKouzelnika() {
        selectHero(HeroClass.KOUZELNIK);
    }

    @FXML
    private void zvolitBandITU() {
        selectHero(HeroClass.BANDITA);
    }

    @FXML
    private void zvolitBandita() {
        selectHero(HeroClass.BANDITA);
    }

    @FXML
    private void zvolitRytire() {
        selectHero(HeroClass.RYTIR);
    }

    // --- HANDLER PRO HOD KOSTKOU (PODLE FXML) ---

    @FXML
    private void hoditKostkou() {
        onRollDice();
    }

    @FXML
    private void onRollDice() {
        if (player.hasRolledThisTurn()) {
            if (statusLabel != null) {
                statusLabel.setText("V tomto tahu už jsi házel! Ukonči tah.");
            }
            return;
        }

        player.setHasRolledThisTurn(true);
        if (rollDiceButton != null) rollDiceButton.setDisable(true);

        int diceValue = random.nextInt(6) + 1;
        if (diceResultLabel != null) diceResultLabel.setText("Padlo: " + diceValue);

        animateMovement(diceValue);
    }

    // --- OSTATNÍ HANDLERY ---

    @FXML
    private void zavritVyberPredmetu() {
        if (statusLabel != null) {
            statusLabel.setText("Výběr předmětů zavřen.");
        }
    }

    @FXML
    private void zpetDoMenu(ActionEvent event) {
        App.zmenScenu("/Menu.fxml");
    }

    @FXML
    private void onEndTurn() {
        resetTurnState();
    }

    @FXML
    private void ukoncitTah() {
        resetTurnState();
    }

    // ------------------------------------------------

    public void selectHero(HeroClass selectedHero) {
        player = new Player(selectedHero);

        if (playerToken == null) {
            playerToken = new Circle(14);
            playerToken.setFill(Color.RED);
            playerToken.setStroke(Color.WHITE);
            playerToken.setStrokeWidth(2.5);
            boardPane.getChildren().add(playerToken);
        }

        updateTokenPosition();
        resetTurnState();
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
            if (statusLabel != null) statusLabel.setText("Pohyb dokončen. Klikni na Ukončit tah.");
            if (endTurnButton != null) endTurnButton.setDisable(false);
        });

        timeline.play();
    }

    private void resetTurnState() {
        player.setHasRolledThisTurn(false);
        if (rollDiceButton != null) rollDiceButton.setDisable(false);
        if (endTurnButton != null) endTurnButton.setDisable(true);
        if (diceResultLabel != null) diceResultLabel.setText("Padlo: -");
        if (statusLabel != null) statusLabel.setText("Aktivní hrdina: " + player.getHeroClass().getName());
    }

    private void updateTokenPosition() {
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