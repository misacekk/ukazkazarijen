module cz.panacku.zlobse {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.panacku.zlobse to javafx.fxml;
    exports cz.panacku.zlobse;
}