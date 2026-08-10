package cz.panacku.zlobse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameController {

    @FXML
    private void zpetDoMenu(ActionEvent event) {
        App.zmenScenu("/Menu.fxml");
    }

    @FXML
    private void hoditKostkou(ActionEvent event) {
        System.out.println("Kostka se hází...");
    }
}