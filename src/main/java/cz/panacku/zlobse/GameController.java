package cz.panacku.zlobse;

import cz.panacku.zlobse.GameEngine;
import cz.panacku.zlobse.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private ImageView itemSlot1;
    @FXML private ImageView itemSlot2;
    @FXML private ImageView itemSlot3;

    private GameEngine gameEngine;

    @FXML
    public void initialize() {
        // Vytvoření 4 hráčů podle zadání (4 frakce/postavy)
        List<Player> players = new ArrayList<>();
        players.add(new Player("Hráč 1", "Rytíř", "RED"));
        players.add(new Player("Hráč 2", "Kouzelník", "BLUE"));
        players.add(new Player("Hráč 3", "Bandita", "GREEN"));
        players.add(new Player("Hráč 4", "Nekromant", "YELLOW"));

        gameEngine = new GameEngine(players);
        updateUI();
    }

    @FXML
    private void hoditKostkou(ActionEvent event) {
        if (gameEngine.hasRolled()) {
            showAlert("Už jsi házel!", "Musíš nejprve táhnout figurkou.");
            return;
        }

        int hod = gameEngine.rollDice();
        showAlert("Hod kostkou", "Hodil jsi číslo: " + hod);
        updateUI();
    }

    @FXML
    private void pouzitSchopnost(ActionEvent event) {
        gameEngine.useAbility();
        updateUI();
    }

    @FXML
    private void zpetDoMenu(ActionEvent event) {
        try {
            Parent menuRoot = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(menuRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUI() {
        Player current = gameEngine.getCurrentPlayer();
        String[] inv = current.getInventory();

        setSlotImage(itemSlot1, inv[0]);
        setSlotImage(itemSlot2, inv[1]);
        setSlotImage(itemSlot3, inv[2]);
    }

    private void setSlotImage(ImageView slot, String itemName) {
        if (itemName != null && !itemName.isEmpty()) {
            try {
                slot.setImage(new Image(getClass().getResourceAsStream("/images/" + itemName.toLowerCase() + ".png")));
            } catch (Exception e) {
                slot.setImage(null);
            }
        } else {
            slot.setImage(null);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}