package cz.panacku.zlobse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoreController {

    @FXML
    private void zpetDoMenu(ActionEvent event) {
        App.zmenScenu("/Menu.fxml");
    }
}