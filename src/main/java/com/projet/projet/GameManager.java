package com.projet.projet;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class GameManager {
    private Player currentPlayer;
    private GameScene gameScene;
    private VBox menu;

    public VBox getClassSelectionMenu() {
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

        // Sous-titre
        Text subTitle = new Text("Choose Your Hero");
        subTitle.setFont(Font.font("Arial", 24));
        subTitle.setFill(Color.LIGHTGRAY);
        subTitle.setEffect(new DropShadow(5, Color.BLACK));

        // Style des boutons simplifié
        String buttonStyle = "-fx-background-color: #4CAF50;" +
                           "-fx-text-fill: white;" +
                           "-fx-font-size: 20px;" +
                           "-fx-padding: 15 50;" +
                           "-fx-background-radius: 25;";

        // Création des boutons avec descriptions
        VBox assassinBox = createCharacterButton("Assassin", "Fast & Deadly", buttonStyle);
        VBox wizardBox = createCharacterButton("Wizard", "Magic & Shield", buttonStyle);
        VBox warriorBox = createCharacterButton("Warrior", "Strong & Tough", buttonStyle);
        VBox doctorBox = createCharacterButton("Doctor", "Healer & Support", buttonStyle);

        menu.getChildren().addAll(mainTitle, subTitle, assassinBox, wizardBox, warriorBox, doctorBox);
        return menu;
    }

    private VBox createCharacterButton(String name, String description, String buttonStyle) {
        Button button = new Button(name);
        button.setStyle(buttonStyle);
        button.setPrefWidth(300);
        
        Text desc = new Text(description);
        desc.setFill(Color.LIGHTGRAY);
        desc.setFont(Font.font("Arial", 14));
        
        button.setOnAction(e -> startGame(name.toUpperCase()));
        
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(button, desc);
        
        return box;
    }

    private void startGame(String playerClass) {
        currentPlayer = PlayerFactory.createPlayer(playerClass);
        gameScene = new GameScene(currentPlayer);
        Stage stage = (Stage) menu.getScene().getWindow();
        stage.setScene(gameScene.getScene());
    }
} 
