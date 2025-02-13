package com.projet.projet;

import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class GameScene {
    private static final int GAME_WIDTH = 1200;
    private static final int GAME_HEIGHT = 800;
    private Pane root;
    private Player player;
    private Set<KeyCode> activeKeys;
    
    public GameScene(Player player) {
        this.player = player;
        this.root = new Pane();
        this.activeKeys = new HashSet<>();
        
        System.out.println("GameScene créée"); // Debug
        
        // Crée un fond gris foncé
        Rectangle background = new Rectangle(GAME_WIDTH, GAME_HEIGHT, Color.DARKGRAY);
        root.getChildren().add(background);
        
        // Ajoute le sprite du joueur à la scène
        root.getChildren().add(player.sprite);
        
        // Position initiale du sprite
        player.sprite.setX(player.x);
        player.sprite.setY(player.y);
        System.out.println("Position initiale - X: " + player.x + ", Y: " + player.y); // Debug
        
        // Configure les contrôles
        setupControls();
    }
    
    private void setupControls() {
        System.out.println("Configuration des contrôles..."); // Debug
        
        root.setFocusTraversable(true);
        
        // Capture uniquement les flèches directionnelles
        root.setOnKeyPressed(e -> {
            System.out.println("Touche pressée: " + e.getCode()); // Debug
            if (e.getCode() == KeyCode.LEFT || 
                e.getCode() == KeyCode.RIGHT || 
                e.getCode() == KeyCode.UP || 
                e.getCode() == KeyCode.DOWN) {
                activeKeys.add(e.getCode());
                System.out.println("Touche ajoutée aux touches actives: " + e.getCode()); // Debug
                System.out.println("Touches actives: " + activeKeys); // Debug
            }
        });
        
        root.setOnKeyReleased(e -> {
            System.out.println("Touche relâchée: " + e.getCode()); // Debug
            activeKeys.remove(e.getCode());
        });
        
        // Demande le focus pour recevoir les événements clavier
        root.requestFocus();
        System.out.println("Focus demandé"); // Debug
        
        // Démarre la boucle de jeu
        startGameLoop();
    }
    
    private void startGameLoop() {
        System.out.println("Démarrage de la boucle de jeu"); // Debug
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame();
            }
        }.start();
    }
    
    private void updateGame() {
        // Gestion des déplacements avec les flèches uniquement
        if (activeKeys.contains(KeyCode.LEFT)) {
            player.moveLeft();
            System.out.println("Déplacement gauche - X: " + player.x); // Debug
        }
        if (activeKeys.contains(KeyCode.RIGHT)) {
            player.moveRight();
            System.out.println("Déplacement droite - X: " + player.x); // Debug
        }
        if (activeKeys.contains(KeyCode.UP)) {
            player.moveUp();
            System.out.println("Déplacement haut - Y: " + player.y); // Debug
        }
        if (activeKeys.contains(KeyCode.DOWN)) {
            player.moveDown();
            System.out.println("Déplacement bas - Y: " + player.y); // Debug
        }
        
        // Limites de la zone de jeu
        double oldX = player.x;
        double oldY = player.y;
        player.x = Math.max(0, Math.min(player.x, GAME_WIDTH - 50));
        player.y = Math.max(0, Math.min(player.y, GAME_HEIGHT - 50));
        
        if (oldX != player.x || oldY != player.y) {
            System.out.println("Position ajustée - X: " + player.x + ", Y: " + player.y); // Debug
        }
        
        // Met à jour la position du sprite
        player.sprite.setX(player.x);
        player.sprite.setY(player.y);
    }
    
    public Pane getRoot() {
        return root;
    }
} 
