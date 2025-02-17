package com.projet.projet;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu {
    private static final int GAME_WIDTH = HelloApplication.WINDOW_WIDTH;
    private static final int GAME_HEIGHT = HelloApplication.WINDOW_HEIGHT;
    private VBox menu;

    public VBox getMainMenu() {
        menu = new VBox(40);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-background-color: linear-gradient(to bottom, #1a2a3a, #2C3E50);");

        // Titre principal
        Text mainTitle = new Text("RogueLike");
        mainTitle.setFont(Font.font("Arial Black", 72));
        mainTitle.setFill(Color.WHITE);
        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.rgb(0, 200, 0, 0.5));
        titleShadow.setRadius(15);
        mainTitle.setEffect(titleShadow);

        // Style commun pour les boutons
        String buttonStyle = "-fx-background-color: #4CAF50;" +
                           "-fx-text-fill: white;" +
                           "-fx-font-size: 24px;" +
                           "-fx-background-radius: 25;" +
                           "-fx-padding: 15 30;";

        Button newGameButton = new Button("Nouvelle Partie");
        newGameButton.setPrefWidth(300);
        newGameButton.setStyle(buttonStyle);

        Button continueButton = new Button("Continuer");
        continueButton.setPrefWidth(300);
        continueButton.setStyle(buttonStyle.replace("#4CAF50", "#2196F3"));
        
        // Activer/désactiver le bouton Continuer selon l'existence d'une sauvegarde
        continueButton.setDisable(!SaveManager.hasSaveFile());

        newGameButton.setOnAction(e -> startNewGame());
        continueButton.setOnAction(e -> loadGame());

        // Créer le texte AM Project avec un style élégant
        Text amProjectText = new Text("AM Project");
        amProjectText.setFont(Font.font("Brush Script MT", 48));
        amProjectText.setFill(Color.GOLD);
        
        // Ajouter un effet de lueur dorée
        DropShadow amShadow = new DropShadow();
        amShadow.setColor(Color.rgb(255, 215, 0, 0.5)); // Couleur or semi-transparente
        amShadow.setRadius(20);
        amShadow.setSpread(0.4);
        amProjectText.setEffect(amShadow);
        
        // Créer un VBox séparé pour le texte AM Project
        VBox amProjectBox = new VBox(amProjectText);
        amProjectBox.setAlignment(Pos.CENTER_RIGHT);
        amProjectBox.setPadding(new javafx.geometry.Insets(20, 40, 0, 0)); // Marge en haut et à droite

        // Ajouter tous les éléments
        menu.getChildren().addAll(mainTitle, newGameButton, continueButton, amProjectBox);
        return menu;
    }

    private void startNewGame() {
        // Supprimer l'ancienne sauvegarde si elle existe
        SaveManager.deleteSave();
        
        // Afficher le menu de sélection des classes
        Stage stage = (Stage) menu.getScene().getWindow();
        GameManager gameManager = new GameManager();
        stage.setScene(new Scene(gameManager.getClassSelectionMenu(), GAME_WIDTH, GAME_HEIGHT));
    }

    private void loadGame() {
        SaveData saveData = SaveManager.loadGame();
        if (saveData != null) {
            // Créer le joueur approprié
            Player player = null;
            switch (saveData.getPlayerClass()) {
                case "WARRIOR":
                    player = new Warrior();
                    break;
                case "WIZARD":
                    player = new Wizard();
                    break;
                case "ASSASSIN":
                    player = new Assassin();
                    break;
                case "DOCTOR":
                    player = new Doctor();
                    break;
            }

            if (player != null) {
                // Créer une nouvelle scène de jeu avec les données sauvegardées
                Stage stage = (Stage) menu.getScene().getWindow();
                GameScene gameScene = new GameScene(player, saveData);
                stage.setScene(gameScene.getCurrentScene());
            }
        }
    }
} 