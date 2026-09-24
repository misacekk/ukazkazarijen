package cz.panacku.zlobse;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class GameController {

    @FXML private VBox gameContent;
    @FXML private StackPane boardContainer;
    @FXML private VBox selectionContainer;
    @FXML private VBox bottomBar;
    @FXML private VBox itemSelectionOverlay;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        if (gameContent != null) gameContent.setVisible(true);
        if (selectionContainer != null) selectionContainer.setVisible(false);
        if (bottomBar != null) bottomBar.setVisible(true);
        if (itemSelectionOverlay != null) itemSelectionOverlay.setVisible(false);
        if (statusLabel != null) statusLabel.setText("");

        setupBoardUI();
    }

    // Prázdné obsluhy událostí nutné pro načtení Game.fxml bez chyby
    @FXML private void zvolitRezim2() {}
    @FXML private void zvolitRezim4() {}
    @FXML private void zvolitRytire() {}
    @FXML private void zvolitKouzelnika() {}
    @FXML private void zvolitBanditu() {}
    @FXML private void zvolitNekromanta() {}
    @FXML private void hoditKostkou() {}
    @FXML private void pouzitSchopnost() {}
    @FXML private void pouzitPredmet() {}
    @FXML private void zavritVyberPredmetu() {}

    @FXML
    private void zpetDoMenu() {
        App.zmenScenu("/Menu.fxml");
    }

    private void setupBoardUI() {
        if (boardContainer == null) return;
        boardContainer.getChildren().clear();

        ImageView boardImage = new ImageView();
        InputStream is = getClass().getResourceAsStream("/images/board.png");
        if (is != null) boardImage.setImage(new Image(is));
        boardImage.setFitWidth(1400.0);
        boardImage.setFitHeight(800.0);

        ImageView diceImageView = new ImageView();
        diceImageView.setFitWidth(100.0);
        diceImageView.setFitHeight(100.0);
        InputStream diceIs = getClass().getResourceAsStream("/images/kostka6.png");
        if (diceIs != null) diceImageView.setImage(new Image(diceIs));

        boardContainer.getChildren().addAll(boardImage, diceImageView);
    }
}