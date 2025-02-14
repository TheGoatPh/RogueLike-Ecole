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
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;

public class GameScene {
    private static final int GAME_WIDTH = HelloApplication.WINDOW_WIDTH;
    private static final int GAME_HEIGHT = HelloApplication.WINDOW_HEIGHT;
    private Pane root;
    private Player player;
    private Set<KeyCode> activeKeys;
    private Scene scene;
    private Rectangle platform;
    private List<Rectangle> platforms;
    private List<Rectangle> traversablePlatforms;
    private boolean isJumping = false;
    private double verticalVelocity = 0;
    private static final double GRAVITY = 0.4;
    private static final double JUMP_FORCE = -10.0;
    private List<Demon> demons;
    private Rectangle healthBarBackground;
    private Rectangle healthBarForeground;
    private static final int HEALTH_BAR_WIDTH = 200;
    private static final int HEALTH_BAR_HEIGHT = 20;
    private boolean isGameOver = false;
    private VBox gameOverScreen;
    private static final double FALL_DEATH_Y = GAME_HEIGHT + 100; // Point de chute fatale
    
    public GameScene(Player player) {
        this.player = player;
        this.root = new Pane();
        this.activeKeys = new HashSet<>();
        this.platforms = new ArrayList<>();
        this.traversablePlatforms = new ArrayList<>();
        this.demons = new ArrayList<>();
        
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
        
        // Ajustement des plateformes pour la nouvelle taille
        double platformY = GAME_HEIGHT * 0.75;
        
        // Plateforme principale plus haute
        platform = new Rectangle(720, 30, Color.TRANSPARENT);
        platform.setX(115);
        platform.setY(GAME_HEIGHT * 0.65);
        
        // Plateformes latérales ajustées
        Rectangle rectangleGauche = new Rectangle(GAME_WIDTH * 0.2, 30, Color.DARKRED);
        rectangleGauche.setX(GAME_WIDTH * 0.1);
        rectangleGauche.setY(platform.getY() - 180); // Baissé de 220 à 180
        
        Rectangle rectangleDroit = new Rectangle(GAME_WIDTH * 0.2, 30, Color.DARKBLUE);
        rectangleDroit.setX(GAME_WIDTH * 0.7);
        rectangleDroit.setY(platform.getY() - 180); // Baissé de 220 à 180
        
        // Plateforme centrale ajustée
        Rectangle rectangleCentre = new Rectangle(GAME_WIDTH * 0.2, 30, Color.DARKGREEN);
        rectangleCentre.setX(GAME_WIDTH * 0.4);
        rectangleCentre.setY(platform.getY() - 220); // Baissé de 260 à 220
        
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
        
        // Création de la barre de vie
        healthBarBackground = new Rectangle(20, 20, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBarBackground.setFill(Color.DARKRED);
        
        healthBarForeground = new Rectangle(20, 20, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBarForeground.setFill(Color.GREEN);
        
        root.getChildren().addAll(healthBarBackground, healthBarForeground);
        
        // Spawn du démon sur la plateforme principale
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            // Spawn le démon sur la plateforme principale
            double spawnX = platform.getX() + platform.getWidth() - 100; // Un peu avant la fin de la plateforme
            double spawnY = platform.getY() - 50; // Juste au-dessus de la plateforme
            Demon demon = new Demon(spawnX, spawnY);
            demons.add(demon);
            root.getChildren().add(demon.getSprite());
        }));
        timeline.play();
        
        // Création de l'écran de Game Over
        createGameOverScreen();
        
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
        if (!isGameOver) {
            handleMovement();
            applyGravity();
            checkCollision();
            updateDemons();
            updateHealthBar();
            checkGameOver();
        }
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
    
    private void updateHealthBar() {
        double healthPercentage = (double) player.getCurrentHealth() / player.getMaxHealth();
        healthBarForeground.setWidth(HEALTH_BAR_WIDTH * healthPercentage);
    }
    
    private void handleDemonCollision(Demon demon) {
        if (demon.getSprite().getBoundsInParent().intersects(player.sprite.getBoundsInParent())) {
            player.takeDamage(10);
        }
    }
    
    private void updateDemons() {
        for (Demon demon : demons) {
            // Vérifier si une plateforme bloque le chemin direct vers le joueur
            boolean canSeePlayer = true;
            
            // Vérifier toutes les plateformes entre le démon et le joueur
            for (Rectangle platform : platforms) {
                if (isBlockingPath(demon, player, platform)) {
                    canSeePlayer = false;
                    break;
                }
            }
            for (Rectangle platform : traversablePlatforms) {
                if (isBlockingPath(demon, player, platform)) {
                    canSeePlayer = false;
                    break;
                }
            }
            
            // Le démon ne se déplace que s'il peut "voir" le joueur
            if (canSeePlayer) {
                demon.moveTowardsPlayer(player);
                handleDemonCollision(demon);
            }
            
            // Toujours appliquer la gravité et les collisions
            for (Rectangle platform : platforms) {
                demon.checkCollision(platform);
            }
            for (Rectangle platform : traversablePlatforms) {
                demon.checkCollision(platform);
            }
        }
    }
    
    private boolean isBlockingPath(Demon demon, Player player, Rectangle platform) {
        // Vérifier si la plateforme est entre le démon et le joueur
        double demonX = demon.getX() + demon.getSprite().getFitWidth() / 2;
        double demonY = demon.getY() + demon.getSprite().getFitHeight() / 2;
        double playerX = player.x + player.sprite.getFitWidth() / 2;
        double playerY = player.y + player.sprite.getFitHeight() / 2;
        
        // Créer une ligne entre le démon et le joueur
        return platform.getBoundsInParent().intersects(
            Math.min(demonX, playerX),
            Math.min(demonY, playerY),
            Math.abs(demonX - playerX),
            Math.abs(demonY - playerY)
        );
    }
    
    private void checkGameOver() {
        // Game over si plus de vie ou si chute
        if ((player.getCurrentHealth() <= 0 || player.y > FALL_DEATH_Y) && !isGameOver) {
            isGameOver = true;
            gameOverScreen.setVisible(true);
            
            // Mettre à jour le texte en fonction de la cause de la mort
            Text causeText = (Text) gameOverScreen.getChildren().get(1); // Le deuxième élément est notre texte de score
            if (player.y > FALL_DEATH_Y) {
                causeText.setText("You Fell to Your Death!");
            } else {
                causeText.setText("You Were Defeated!");
            }
        }
    }

    private void createGameOverScreen() {
        gameOverScreen = new VBox(40);
        gameOverScreen.setAlignment(Pos.CENTER);
        gameOverScreen.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");
        gameOverScreen.setPrefSize(GAME_WIDTH, GAME_HEIGHT);

        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Arial Black", 80));
        gameOverText.setFill(Color.RED);
        
        // Texte de cause de mort (sera mis à jour dans checkGameOver)
        Text causeText = new Text("");
        causeText.setFont(Font.font("Arial", 30));
        causeText.setFill(Color.WHITE);

        Button restartButton = new Button("Restart Game");
        restartButton.setPrefWidth(250);
        restartButton.setPrefHeight(50);
        restartButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 24px;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 15 30;"
        );

        Button menuButton = new Button("Main Menu");
        menuButton.setPrefWidth(250);
        menuButton.setPrefHeight(50);
        menuButton.setStyle(
            "-fx-background-color: #2196F3;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 24px;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 15 30;"
        );

        restartButton.setOnAction(e -> restartGame());
        menuButton.setOnAction(e -> returnToMenu());

        gameOverScreen.getChildren().addAll(gameOverText, causeText, restartButton, menuButton);
        gameOverScreen.setVisible(false);
        root.getChildren().add(gameOverScreen);
    }

    private void restartGame() {
        // Retour au menu principal
        Stage stage = (Stage) scene.getWindow();
        GameManager gameManager = new GameManager();
        stage.setScene(new Scene(gameManager.getClassSelectionMenu(), GAME_WIDTH, GAME_HEIGHT));
    }
    
    private void returnToMenu() {
        Stage stage = (Stage) scene.getWindow();
        GameManager gameManager = new GameManager();
        stage.setScene(new Scene(gameManager.getClassSelectionMenu(), GAME_WIDTH, GAME_HEIGHT));
    }
    
    public Scene getScene() {
        return scene;
    }
} 
