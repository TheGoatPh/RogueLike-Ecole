package com.projet.projet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

public class HelloApplication extends Application {
    // Augmentation de la taille de la fenêtre
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;
    private GameManager gameManager;

    @Override
    public void start(Stage stage) {
        gameManager = new GameManager();
        Scene scene = new Scene(gameManager.getClassSelectionMenu(), WINDOW_WIDTH, WINDOW_HEIGHT);
        
        stage.setTitle("RogueLike Game");
        stage.setScene(scene);
        stage.setResizable(false); // Empêche le redimensionnement
        
        // Centre la fenêtre sur l'écran
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX((bounds.getWidth() - WINDOW_WIDTH) / 2);
        stage.setY((bounds.getHeight() - WINDOW_HEIGHT) / 2);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
} 
