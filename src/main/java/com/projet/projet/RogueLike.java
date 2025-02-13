package com.projet.projet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RogueLike extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private GameManager gameManager;

    @Override
    public void start(Stage stage) {
        gameManager = new GameManager();
        Scene scene = new Scene(gameManager.getClassSelectionMenu(), WINDOW_WIDTH, WINDOW_HEIGHT);
        
        stage.setTitle("RogueLike Game - AM Studio");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
