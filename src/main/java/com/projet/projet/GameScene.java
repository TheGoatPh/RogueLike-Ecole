package com.projet.projet;

import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.ArrayList;

public class GameScene {
    private static final int GAME_WIDTH = 1920;
    private static final int GAME_HEIGHT = 1080;
    private Pane root;
    private Player player;
    private Set<KeyCode> activeKeys;
    private Scene scene;
    private Rectangle platform;
    private List<Rectangle> platforms;
    private List<Rectangle> traversablePlatforms;
    private boolean isJumping = false;
    private double verticalVelocity = 0;
    private static final double GRAVITY = 0.2;
    private static final double JUMP_FORCE = -15;
    
    public GameScene(Player player) {
        this.player = player;
        this.root = new Pane();
        this.activeKeys = new HashSet<>();
        this.platforms = new ArrayList<>();
        this.traversablePlatforms = new ArrayList<>();
        
        System.out.println("GameScene créée"); // Debug
        
        // Charge l'image de fond
        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream("/com/projet/projet/images/background.jpg"));
            ImageView background = new ImageView(backgroundImage);
            background.setFitWidth(GAME_WIDTH);
            background.setFitHeight(GAME_HEIGHT);
            root.getChildren().add(background);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fond: " + e.getMessage());
            Rectangle fallbackBackground = new Rectangle(GAME_WIDTH, GAME_HEIGHT, Color.DARKGRAY);
            root.getChildren().add(fallbackBackground);
        }
        
        // Création des trois plateformes en haut avec positions ajustées
        Rectangle rectangleGauche = new Rectangle(300, 40, Color.DARKRED);
        rectangleGauche.setX(150);
        rectangleGauche.setY(420);
        
        Rectangle rectangleCentre = new Rectangle(300, 40, Color.DARKGREEN);
        rectangleCentre.setX(800);
        rectangleCentre.setY(360);
        
        Rectangle rectangleDroit = new Rectangle(350, 40, Color.DARKBLUE);
        rectangleDroit.setX(1480);
        rectangleDroit.setY(420);
        
        // Plateforme principale invisible
        platform = new Rectangle(1300, 40, Color.TRANSPARENT);
        platform.setX(300);
        platform.setY(660);
        
        // Ajout des plateformes traversables
        traversablePlatforms.add(rectangleGauche);
        traversablePlatforms.add(rectangleCentre);
        traversablePlatforms.add(rectangleDroit);
        
        // Ajout de la plateforme principale non-traversable
        platforms.add(platform);
        
        // Ajout de tous les rectangles à la scène
        root.getChildren().addAll(platform, rectangleGauche, rectangleCentre, rectangleDroit);
        
        // Ajoute le sprite du joueur à la scène
        root.getChildren().add(player.sprite);
        
        // Position initiale du sprite
        player.sprite.setX(player.x);
        player.sprite.setY(player.y);
        System.out.println("Position initiale - X: " + player.x + ", Y: " + player.y); // Debug
        
        // Crée la Scene
        scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        
        // Configure les contrôles sur la Scene au lieu du Pane
        setupControls();
        startGameLoop();
    }
    
    private void setupControls() {
        System.out.println("Configuration des contrôles..."); // Debug
        
        // Ajoute les gestionnaires d'événements à la Scene
        scene.setOnKeyPressed(e -> {
            System.out.println("Touche pressée: " + e.getCode()); // Debug
            activeKeys.add(e.getCode());
            if (e.getCode() == KeyCode.UP && !isJumping) {  // Changé de SPACE à UP
                jump();
            }
        });
        
        scene.setOnKeyReleased(e -> {
            System.out.println("Touche relâchée: " + e.getCode()); // Debug
            activeKeys.remove(e.getCode());
        });
    }
    
    private void startGameLoop() {
        System.out.println("Démarrage de la boucle de jeu"); // Debug
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }
    
    private void update() {
        handleMovement();
        applyGravity();
        checkCollision();
    }
    
    private void handleMovement() {
        if (activeKeys.contains(KeyCode.LEFT)) {
            player.moveLeft();
            System.out.println("Déplacement gauche - X: " + player.x); // Debug
        }
        if (activeKeys.contains(KeyCode.RIGHT)) {
            player.moveRight();
            System.out.println("Déplacement droite - X: " + player.x); // Debug
        }
    }
    
    private void jump() {
        if (!isJumping) {
            verticalVelocity = JUMP_FORCE;
            isJumping = true;
        }
    }
    
    private void applyGravity() {
        verticalVelocity += GRAVITY;
        player.y += verticalVelocity;
        player.sprite.setY(player.y);
    }
    
    private void checkCollision() {
        boolean onPlatform = false;
        
        // Vérification pour toutes les plateformes
        for (Rectangle currentPlatform : traversablePlatforms) {
            if (player.sprite.getBoundsInParent().intersects(currentPlatform.getBoundsInParent())) {
                if (verticalVelocity > 0 && // Si le joueur descend
                    player.y + player.sprite.getFitHeight() - 10 <= currentPlatform.getY() && // Si le joueur est au-dessus
                    !activeKeys.contains(KeyCode.DOWN)) { // Et qu'il n'appuie pas sur bas
                    player.y = currentPlatform.getY() - player.sprite.getFitHeight();
                    player.sprite.setY(player.y);
                    verticalVelocity = 0;
                    isJumping = false;
                    onPlatform = true;
                    break;
                }
            }
        }
        
        // Vérification pour la plateforme principale (non traversable)
        if (player.sprite.getBoundsInParent().intersects(platform.getBoundsInParent())) {
            if (verticalVelocity > 0) { // Si le joueur descend
                player.y = platform.getY() - player.sprite.getFitHeight();
                player.sprite.setY(player.y);
                verticalVelocity = 0;
                isJumping = false;
                onPlatform = true;
            }
        }
        
        if (!onPlatform && !isJumping) {
            isJumping = true;
        }
        
        // Empêcher le joueur de sortir de l'écran
        if (player.x < 0) player.x = 0;
        if (player.x > GAME_WIDTH - player.sprite.getFitWidth()) 
            player.x = GAME_WIDTH - player.sprite.getFitWidth();
        player.sprite.setX(player.x);
    }
    
    public Scene getScene() {
        return scene;
    }
} 
