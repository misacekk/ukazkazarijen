package cz.panacku.zlobse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameController {

    @FXML private StackPane boardContainer;
    @FXML private Label statusLabel;
    @FXML private ImageView itemSlot1;
    @FXML private ImageView itemSlot2;
    @FXML private ImageView itemSlot3;

    private GameEngine gameEngine;

    @FXML
    public void initialize() {
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
            statusLabel.setText("⚠️ Už jsi házel! Hodil jsi " + gameEngine.getLastDiceRoll() + ".");
            return;
        }

        int hod = gameEngine.rollDice();
        Player current = gameEngine.getCurrentPlayer();
        statusLabel.setText("🎲 " + current.getName() + " (" + current.getCharacterType() + ") hodil číslo " + hod + "!");
    }

    @FXML
    private void pouzitSchopnost(ActionEvent event) {
        Player current = gameEngine.getCurrentPlayer();

        if (current.useAbility()) {
            statusLabel.setText("⚡ " + current.getName() + " použil schopnost postavy " + current.getCharacterType() + "!");
        } else {
            statusLabel.setText("⚠️ " + current.getName() + " už nemá žádná další použití schopnosti!");
        }

        updateUI();
    }

    @FXML
    private void pouzitPredmet(ActionEvent event) {
        Player current = gameEngine.getCurrentPlayer();
        String[] inv = current.getInventory();

        List<String> dostupnePredmety = new ArrayList<>();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                dostupnePredmety.add((i + 1) + ". " + inv[i]);
            }
        }

        if (dostupnePredmety.isEmpty()) {
            statusLabel.setText("⚠️ Nemáš v inventáři žádné předměty!");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(dostupnePredmety.get(0), dostupnePredmety);
        dialog.setTitle("Výběr předmětu");
        dialog.setHeaderText("Použít předmět hráče " + current.getName());
        dialog.setContentText("Vyber předmět k použití:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(vybrany -> {
            int slotIndex = Character.getNumericValue(vybrany.charAt(0)) - 1;
            String nazevPredmetu = inv[slotIndex];

            current.removeItem(slotIndex);
            updateUI();
            statusLabel.setText("🧪 " + current.getName() + " použil předmět: " + nazevPredmetu + "!");
        });
    }

    @FXML
    private void zpetDoMenu(ActionEvent event) {
        try {
            java.net.URL fxmlLocation = getClass().getResource("/cz/panacku/zlobse/Menu.fxml");
            if (fxmlLocation == null) fxmlLocation = getClass().getResource("/Menu.fxml");
            if (fxmlLocation == null) fxmlLocation = getClass().getResource("Menu.fxml");

            if (fxmlLocation == null) {
                System.err.println("CHYBA: Soubor Menu.fxml nebyl nalezen!");
                return;
            }

            Parent menuRoot = FXMLLoader.load(fxmlLocation);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(menuRoot, 1600, 1000));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUI() {
        Player current = gameEngine.getCurrentPlayer();
        if (statusLabel != null) {
            statusLabel.setText("Na tahu: " + current.getName() + " | Postava: " + current.getCharacterType() +
                    " | Schopnost zbývá: " + current.getAbilityUsesLeft() + "x");
        }

        String[] inv = current.getInventory();
        setSlotImage(itemSlot1, inv[0]);
        setSlotImage(itemSlot2, inv[1]);
        setSlotImage(itemSlot3, inv[2]);
    }

    private void setSlotImage(ImageView slot, String itemName) {
        if (slot == null) return;
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
}