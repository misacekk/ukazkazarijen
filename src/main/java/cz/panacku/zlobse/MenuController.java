package cz.panacku.zlobse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController {

    @FXML
    private void spustitHru(ActionEvent event) {
        App.zmenScenu("/Game.fxml");
    }

    @FXML
    private void otevritLore(ActionEvent event) {
        App.zmenScenu("/Lore.fxml");
    }

    @FXML
    private void ukoncitAplikaci(ActionEvent event) {
        System.exit(0);
    }
}