package cz.panacku.zlobse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Hlavní kontroler herní obrazovky (JavaFX).
 * Spravuje výběr hrdinů, vykreslování desky, házení kostkou a pohyb figurek.
 */
public class GameController {

    // ==========================================
    // FXML Prvky rozhraní (UI)
    // ==========================================

    // Kontejnery pro výběr režimu a hrdinů
    @FXML private VBox selectionContainer;
    @FXML private VBox modeSelectionBox;
    @FXML private VBox heroSelectionBox;
    @FXML private Label selectionTitleLabel;

    // Tlačítka pro výběr jednotlivých hrdinů
    @FXML private Button btnRytir, btnCarodejnice, btnBandita;
    @FXML private Button btnNekromant, btnElfka, btnKralovna;

    // Hlavní herní plocha a overlaye
    @FXML private VBox gameContent;
    @FXML private Pane boardContainer;
    @FXML private VBox playersContainer;
    @FXML private VBox itemSelectionOverlay;
    @FXML private HBox itemCardsContainer;
    @FXML private VBox bottomBar;
    @FXML private Label statusLabel;
    @FXML private ImageView itemSlot1, itemSlot2, itemSlot3;

    // ==========================================
    // Herní konstanty a vizuální nastavení
    // ==========================================
    private static final double BOARD_WIDTH = 800.0;
    private static final double BOARD_HEIGHT = 800.0;
    private static final double TOKEN_SIZE = 45.0;
    private static final double DICE_SIZE = 100.0;

    // ==========================================
    // Stavové proměnné hry
    // ==========================================
    private Pane tokenLayer;
    private ImageView diceImageView;

    private int targetPlayerCount = 4;
    private final List<Player> players = new ArrayList<>();
    private final Map<Pawn, ImageView> pawnTokenMap = new HashMap<>();
    private int currentPlayerIndex = 0;

    private boolean waitingForPawnSelection = false;
    private List<Pawn> movablePawnsCurrentTurn = new ArrayList<>();
    private boolean canSpawnCurrentTurn = false;
    private int lastRolledDiceValue = 0;

    private final Random random = new Random();

    // ==========================================
    // Inicializace
    // ==========================================

    @FXML
    public void initialize() {
        // Nastavení výchozí viditelnosti UI prvků při načtení scénáře
        if (gameContent != null) gameContent.setVisible(true);
        if (selectionContainer != null) selectionContainer.setVisible(true);
        if (modeSelectionBox != null) modeSelectionBox.setVisible(true);
        if (heroSelectionBox != null) heroSelectionBox.setVisible(false);
        if (bottomBar != null) bottomBar.setVisible(false);
        if (itemSelectionOverlay != null) itemSelectionOverlay.setVisible(false);

        setupBoardUI();
    }

    // ==========================================
    // Výběr herního režimu a hrdinů
    // ==========================================

    @FXML private void zvolitRezim2() { startHeroSelection(2); }
    @FXML private void zvolitRezim3() { startHeroSelection(3); }
    @FXML private void zvolitRezim4() { startHeroSelection(4); }

    /**
     * Zahájí fázi výběru hrdinů pro zadaný počet hráčů.
     */
    private void startHeroSelection(int count) {
        this.targetPlayerCount = count;
        BoardPath.setPlayerCount(count);

        setupBoardUI();

        if (modeSelectionBox != null) modeSelectionBox.setVisible(false);
        if (heroSelectionBox != null) heroSelectionBox.setVisible(true);

        updateHeroSelectionUI();
    }

    // Handlery pro tlačítka vývěru hrdinů
    @FXML private void zvolitRytire()      { addPlayerHero(HeroClass.RYTIR); }
    @FXML private void zvolitCarodejnici() { addPlayerHero(HeroClass.CARODEJNICE); }
    @FXML private void zvolitBanditu()     { addPlayerHero(HeroClass.BANDITA); }
    @FXML private void zvolitNekromanta()  { addPlayerHero(HeroClass.NEKROMANT); }
    @FXML private void zvolitElfku()       { addPlayerHero(HeroClass.ELFKA); }
    @FXML private void zvolitKralovnu()    { addPlayerHero(HeroClass.KRALOVNA); }

    /**
     * Přidá nového hráče se zvoleným hrdinou a vytvoří vizuální žetony jeho figurek.
     */
    private void addPlayerHero(HeroClass heroClass) {
        int newPlayerId = players.size() + 1;
        Player p = new Player(newPlayerId, heroClass);
        players.add(p);

        // Vytvoření grafických reprezentací pro všechny figurky hráče
        for (Pawn pawn : p.getPawns()) {
            ImageView tokenView = new ImageView();
            tokenView.setFitWidth(TOKEN_SIZE);
            tokenView.setFitHeight(TOKEN_SIZE);
            tokenView.setPreserveRatio(true);

            InputStream is = getClass().getResourceAsStream("/images/" + heroClass.getImageFileName());
            if (is != null) {
                tokenView.setImage(new Image(is));
            }

            tokenView.setOnMouseClicked(e -> handlePawnClick(pawn));

            pawnTokenMap.put(pawn, tokenView);
            if (tokenLayer != null) {
                tokenLayer.getChildren().add(tokenView);
            }

            updatePawnPosition(pawn);
        }

        // Pokračování ve výběru nebo zahájení samotné hry
        if (players.size() < targetPlayerCount) {
            updateHeroSelectionUI();
        } else {
            if (selectionContainer != null) selectionContainer.setVisible(false);
            if (bottomBar != null) bottomBar.setVisible(true);

            currentPlayerIndex = 0;
            updatePlayersPanel();
            startTurn();
        }
    }

    /**
     * Aktualizuje nadpis a deaktivuje již zabrané hrdiny.
     */
    private void updateHeroSelectionUI() {
        int nextPlayerNum = players.size() + 1;
        if (selectionTitleLabel != null) {
            selectionTitleLabel.setText("Hráč " + nextPlayerNum + ": Vyber si svého hrdinu");
        }

        disableIfTaken(btnRytir, HeroClass.RYTIR);
        disableIfTaken(btnCarodejnice, HeroClass.CARODEJNICE);
        disableIfTaken(btnBandita, HeroClass.BANDITA);
        disableIfTaken(btnNekromant, HeroClass.NEKROMANT);
        disableIfTaken(btnElfka, HeroClass.ELFKA);
        disableIfTaken(btnKralovna, HeroClass.KRALOVNA);
    }

    private void disableIfTaken(Button btn, HeroClass heroClass) {
        if (btn == null) return;
        boolean taken = players.stream().anyMatch(p -> p.getHeroClass() == heroClass);
        btn.setDisable(taken);
    }

    // ==========================================
    // Řízení průběhu tahu a herní logiky
    // ==========================================

    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Zahájí tah aktuálního hráče a resetuje stavové proměnné tahu.
     */
    private void startTurn() {
        Player p = getCurrentPlayer();
        p.resetTurn();
        waitingForPawnSelection = false;
        movablePawnsCurrentTurn.clear();
        canSpawnCurrentTurn = false;

        updatePlayersPanel();

        if (statusLabel != null) {
            statusLabel.setText("Na tahu je Hráč " + p.getId() + " (" + p.getHeroClass().getName() + ")");
        }
    }

    /**
     * Předá tah dalšímu hráči v pořadí.
     */
    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        startTurn();
    }

    /**
     * Spustí animaci hodu kostkou a vyhodnotí dostupné akce hráče.
     */
    @FXML
    private void hoditKostkou() {
        Player p = getCurrentPlayer();
        if (p.hasRolledThisTurn()) {
            if (statusLabel != null) statusLabel.setText("V tomto tahu už jsi házel!");
            return;
        }

        p.setHasRolledThisTurn(true);

        // Animace protáčení kostky
        Timeline diceAnimation = new Timeline();
        for (int i = 0; i < 10; i++) {
            KeyFrame kf = new KeyFrame(
                    Duration.millis(80 * (i + 1)),
                    event -> setDiceImage(random.nextInt(6) + 1)
            );
            diceAnimation.getKeyFrames().add(kf);
        }

        // Vyhodnocení výsledku po dokončení animace
        diceAnimation.setOnFinished(event -> {
            int diceValue = random.nextInt(6) + 1;
            setDiceImage(diceValue);
            this.lastRolledDiceValue = diceValue;

            // Nasadit novou figurku lze jen při hodu 6, pokud hráč nemá žádnou figurku na desce
            canSpawnCurrentTurn = (diceValue == 6) && p.getPawnsOnBoard().isEmpty() && (p.getPawnInHouse() != null);
            movablePawnsCurrentTurn = p.getPawnsOnBoard();

            // Případ 1: Nemá figurku na desce a nepadla 6 -> Konec tahu
            if (!canSpawnCurrentTurn && movablePawnsCurrentTurn.isEmpty()) {
                if (statusLabel != null) {
                    statusLabel.setText("Hráč " + p.getId() + " hodil " + diceValue + ", ale nemá žádnou figurku na desce.");
                }
                nextTurn();
                return;
            }

            // Případ 2: Automatické nasazení (padla 6 a startovací pole je volné/jediné možné)
            if (canSpawnCurrentTurn && movablePawnsCurrentTurn.isEmpty()) {
                executeSpawn(p.getPawnInHouse(), diceValue);
            }
            // Případ 3: Automatický pohyb (hráč má na desce právě 1 figurku)
            else if (!canSpawnCurrentTurn && movablePawnsCurrentTurn.size() == 1) {
                executePawnMove(movablePawnsCurrentTurn.get(0), diceValue);
            }
            // Případ 4: Výběr hráče (více možností tahu)
            else {
                waitingForPawnSelection = true;
                if (statusLabel != null) {
                    statusLabel.setText("Hodil jsi " + diceValue + "! Klikni na figurku pro vykonání tahu.");
                }
            }
        });

        diceAnimation.play();
    }

    /**
     * Reaguje na kliknutí uživatele na figurku na hracím plánu nebo v panelu.
     */
    private void handlePawnClick(Pawn clickedPawn) {
        if (!waitingForPawnSelection) return;

        Player p = getCurrentPlayer();
        if (clickedPawn.getOwner() != p) return;

        if (clickedPawn.isInHouse()) {
            if (canSpawnCurrentTurn) {
                waitingForPawnSelection = false;
                executeSpawn(clickedPawn, lastRolledDiceValue);
            }
        } else {
            if (movablePawnsCurrentTurn.contains(clickedPawn)) {
                waitingForPawnSelection = false;
                executePawnMove(clickedPawn, lastRolledDiceValue);
            }
        }
    }

    /**
     * Provede nasazení figurky z domečku na startovní políčko.
     */
    private void executeSpawn(Pawn pawn, int diceValue) {
        Player p = pawn.getOwner();
        pawn.setInHouse(false);
        pawn.stepToTile(p.getStartTileIndex());
        pawn.setTotalStepsMoved(0);

        updatePawnPosition(pawn);
        updatePlayersPanel();

        if (statusLabel != null) {
            statusLabel.setText("Hráč " + p.getId() + " nasadil figurku! Hází znovu.");
        }
        p.setHasRolledThisTurn(false); // Při padnutí 6 hází hráč znovu
    }

    /**
     * Provede pohyb figurky po dráze.
     */
    private void executePawnMove(Pawn pawn, int diceValue) {
        Player p = pawn.getOwner();

        animatePawnMovement(pawn, diceValue, () -> {
            updatePlayersPanel();

            if (diceValue == 6) {
                p.setHasRolledThisTurn(false);
                if (statusLabel != null) {
                    statusLabel.setText("Hodil jsi 6! Házíš znovu.");
                }
            } else {
                nextTurn();
            }
        });
    }

    /**
     * Animuje plynulý krok za krokem po hrací desce.
     */
    private void animatePawnMovement(Pawn pawn, int steps, Runnable onComplete) {
        Timeline timeline = new Timeline();

        for (int i = 1; i <= steps; i++) {
            final int stepNum = i;
            KeyFrame kf = new KeyFrame(
                    Duration.millis(200 * stepNum),
                    event -> {
                        pawn.stepToTile(pawn.getCurrentTileIndex() + 1);
                        pawn.addSteps(1);
                        updatePawnPosition(pawn);
                    }
            );
            timeline.getKeyFrames().add(kf);
        }

        timeline.setOnFinished(event -> {
            if (onComplete != null) onComplete.run();
        });

        timeline.play();
    }

    // ==========================================
    // Vykreslování a Aktualizace UI
    // ==========================================

    /**
     * Aktualizuje X/Y pozici figurky na základě jejího indexu políčka.
     */
    private void updatePawnPosition(Pawn pawn) {
        ImageView token = pawnTokenMap.get(pawn);
        if (token == null) return;

        if (pawn.isInHouse()) {
            token.setVisible(false);
            return;
        }

        token.setVisible(true);
        double offset = TOKEN_SIZE / 2.0;

        TilePosition tile = BoardPath.getTile(pawn.getCurrentTileIndex());
        token.setLayoutX(tile.getX() - offset);
        token.setLayoutY(tile.getY() - offset);
    }

    /**
     * Změní obrázek kostky podle hozené hodnoty.
     */
    private void setDiceImage(int diceValue) {
        if (diceImageView == null) return;
        InputStream is = getClass().getResourceAsStream("/images/kostka" + diceValue + ".png");
        if (is != null) {
            diceImageView.setImage(new Image(is));
        }
    }

    /**
     * Inicializuje herní desku, pozadí, vrstvu s figurkami a kostku ve středu desky.
     */
    private void setupBoardUI() {
        if (boardContainer == null) return;
        boardContainer.getChildren().clear();

        // Načtení pozadí desky podle zvoleného počtu hráčů
        String imageName;
        if (targetPlayerCount == 2) {
            imageName = "/images/deskapro2.png";
        } else if (targetPlayerCount == 3) {
            imageName = "/images/deskapro3.png";
        } else {
            imageName = "/images/deskapro4.png";
        }

        InputStream is = getClass().getResourceAsStream(imageName);
        if (is == null) {
            is = getClass().getResourceAsStream("/images/deskapro4.png");
        }

        ImageView boardImage = new ImageView();
        if (is != null) boardImage.setImage(new Image(is));
        boardImage.setFitWidth(BOARD_WIDTH);
        boardImage.setFitHeight(BOARD_HEIGHT);

        // Vrstva pro žetony a kostku
        tokenLayer = new Pane();
        tokenLayer.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
        tokenLayer.setMaxSize(BOARD_WIDTH, BOARD_HEIGHT);

        // Umístění kostky uprostřed desky
        diceImageView = new ImageView();
        diceImageView.setFitWidth(DICE_SIZE);
        diceImageView.setFitHeight(DICE_SIZE);
        diceImageView.setLayoutX((BOARD_WIDTH - DICE_SIZE) / 2.0);
        diceImageView.setLayoutY((BOARD_HEIGHT - DICE_SIZE) / 2.0);

        setDiceImage(1);

        diceImageView.setCursor(Cursor.HAND);
        diceImageView.setOnMouseClicked(e -> hoditKostkou());

        tokenLayer.getChildren().add(diceImageView);
        boardContainer.getChildren().addAll(boardImage, tokenLayer);
    }

    /**
     * Přebuduje postranní panel s přehledem hráčů a stavu jejich figurek.
     */
    private void updatePlayersPanel() {
        if (playersContainer == null) return;
        playersContainer.getChildren().clear();

        Label title = new Label("HRÁČI A FIGURKY");
        title.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 18px; -fx-font-weight: bold;");
        playersContainer.getChildren().add(title);

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            boolean isCurrent = (i == currentPlayerIndex);

            VBox playerBox = new VBox(8);

            String borderStyle = isCurrent
                    ? "-fx-background-color: #2c2c2e; -fx-border-color: #FFD700; -fx-border-width: 2px; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 10px;"
                    : "-fx-background-color: #242426; -fx-border-color: #3a3a3c; -fx-border-width: 1px; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 10px;";
            playerBox.setStyle(borderStyle);

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);

            Label playerLabel = new Label("Hráč " + p.getId() + ":");
            playerLabel.setStyle("-fx-text-fill: " + (isCurrent ? "#FFD700" : "#FFFFFF") + "; -fx-font-weight: bold; -fx-font-size: 13px;");

            HBox pawnsBox = new HBox(6);
            pawnsBox.setAlignment(Pos.CENTER_LEFT);

            for (Pawn pawn : p.getPawns()) {
                ImageView pawnIcon = new ImageView();
                pawnIcon.setFitWidth(28);
                pawnIcon.setFitHeight(28);
                pawnIcon.setPreserveRatio(true);

                InputStream is = getClass().getResourceAsStream("/images/" + p.getHeroClass().getImageFileName());
                if (is != null) {
                    pawnIcon.setImage(new Image(is));
                }

                // Průhlednost určuje, zda je figurka v domečku nebo ve hře
                pawnIcon.setOpacity(pawn.isInHouse() ? 0.4 : 1.0);
                pawnIcon.setCursor(Cursor.HAND);

                Pawn currentPawn = pawn;
                pawnIcon.setOnMouseClicked(e -> handlePawnClick(currentPawn));

                pawnsBox.getChildren().add(pawnIcon);
            }

            row.getChildren().addAll(playerLabel, pawnsBox);
            playerBox.getChildren().add(row);
            playersContainer.getChildren().add(playerBox);
        }
    }

    // ==========================================
    // Akce předmětů a navigační metody
    // ==========================================

    @FXML
    private void pouzitPredmet() {
        // Logika pro použití předmětu
    }

    @FXML
    private void zavritVyberPredmetu() {
        if (itemSelectionOverlay != null) {
            itemSelectionOverlay.setVisible(false);
        }
    }

    @FXML
    private void pouzitSchopnost() {
        // Logika pro aktivaci speciální schopnosti hrdiny
    }

    @FXML
    private void zpetDoMenu() {
        App.zmenScenu("/Menu.fxml");
    }
}