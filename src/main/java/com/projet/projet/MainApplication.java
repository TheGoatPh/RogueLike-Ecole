package com.projet.projet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApplication extends Application {
    // Taille unique pour tous les écrans
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 576;
    private GameManager gameManager;

    @Override
    public void start(Stage stage) {
        stage.setTitle("RogueLike");
        
        // Démarrer avec le menu principal au lieu du menu de sélection des classes
        MainMenu mainMenu = new MainMenu();
        Scene scene = new Scene(mainMenu.getMainMenu(), WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Ajouter un gestionnaire pour la fermeture de la fenêtre
        stage.setOnCloseRequest((WindowEvent event) -> {
            // Si la scène actuelle est une GameScene, sauvegarder la partie
            if (stage.getScene() != null && stage.getScene().getRoot() instanceof GameScene) {
                GameScene gameScene = (GameScene) stage.getScene().getRoot();
                gameScene.saveGame();
            }
        });
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
} 
