package com.projet.projet;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameManager {
    private Player currentPlayer;
    private GameScene gameScene;
    private VBox menu;

    public VBox getClassSelectionMenu() {
        menu = new VBox(20); // Stockage de la référence au menu
        menu.setAlignment(Pos.CENTER);

        Button assassinBtn = new Button("Assassin");
        Button wizardBtn = new Button("Wizard");
        Button warriorBtn = new Button("Warrior");
        Button doctorBtn = new Button("Doctor");

        assassinBtn.setOnAction(e -> startGame("ASSASSIN"));
        wizardBtn.setOnAction(e -> startGame("WIZARD"));
        warriorBtn.setOnAction(e -> startGame("WARRIOR"));
        doctorBtn.setOnAction(e -> startGame("DOCTOR"));

        menu.getChildren().addAll(assassinBtn, wizardBtn, warriorBtn, doctorBtn);
        return menu;
    }

    private void startGame(String playerClass) {
        currentPlayer = PlayerFactory.createPlayer(playerClass);
        gameScene = new GameScene(currentPlayer);
        // Utilise la nouvelle Scene du GameScene
        Stage stage = (Stage) menu.getScene().getWindow();
        stage.setScene(gameScene.getScene());
    }
} 
