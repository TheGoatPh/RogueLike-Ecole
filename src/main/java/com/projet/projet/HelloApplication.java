package com.projet.projet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    // Augmentation de la taille de la fenêtre
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private GameManager gameManager;

    @Override
    public void start(Stage stage) {
        gameManager = new GameManager();
        Scene scene = new Scene(gameManager.getClassSelectionMenu(), WINDOW_WIDTH, WINDOW_HEIGHT);
        
        stage.setTitle("RogueLike Game");
        stage.setScene(scene);
        stage.setResizable(false); // Empêche le redimensionnement
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
} 
