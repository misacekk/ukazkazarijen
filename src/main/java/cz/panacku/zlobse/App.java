package cz.panacku.zlobse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("Panečku, zlob se!");

        // Načte hlavní MENU, ne rovnou hru
        zmenScenu("/Menu.fxml");
        primaryStage.show();
    }

    public static void zmenScenu(String fxmlSoubor) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlSoubor));
            Parent root = loader.load();

            if (primaryStage.getScene() == null) {
                primaryStage.setScene(new Scene(root, 1600, 1000));
            } else {
                primaryStage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            System.err.println("Chyba při načítání FXML: " + fxmlSoubor);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}