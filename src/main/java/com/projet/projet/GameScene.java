package com.projet.projet;

import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Circle;
import javafx.scene.Node;

public class GameScene extends Pane {
    private static final int GAME_WIDTH = MainApplication.WINDOW_WIDTH;
    private static final int GAME_HEIGHT = MainApplication.WINDOW_HEIGHT;
    private Player player;
    private Set<KeyCode> activeKeys;
    private Scene scene;
    private Rectangle platform;
    private List<Rectangle> platforms;
    private List<Rectangle> traversablePlatforms;
    private boolean isJumping = false;
    private double verticalVelocity = 0;
    private static final double GRAVITY = 0.4;
    private static final double JUMP_FORCE = -15.0;
    private List<Dragon> dragons;
    private Rectangle healthBarBackground;
    private Rectangle healthBarForeground;
    private static final int HEALTH_BAR_WIDTH = 200;
    private static final int HEALTH_BAR_HEIGHT = 20;
    private boolean isGameOver = false;
    private VBox gameOverScreen;
    private static final double FALL_DEATH_Y = GAME_HEIGHT + 100; // Point de chute fatale
    private int demonKillCount = 0;
    private Text killCountText;
    private Text timerText;
    private long startTime;
    private Timeline spawnTimeline;
    private Random random = new Random();
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 1000; // 1 second
    private Rectangle inventorySlot1;
    private Rectangle inventorySlot2;
    private ImageView potionIndicator1;
    private ImageView potionIndicator2;
    private List<Potion> potions;
    private Timeline potionSpawnTimeline;
    private boolean hasPotion1 = false;
    private boolean hasPotion2 = false;
    private int currentWave = 0;
    private static final int MAX_WAVES = 30;
    private Text waveText;
    private Text waveBigAnnouncement;
    private boolean isWaveInProgress = false;
    private Timeline waveSpawnTimeline;
    private int remainingDemonsToSpawn = 0;
    private VBox pauseMenu;
    private boolean isPaused = false;
    private Rectangle cooldownBar;
    private Text specialAbilityText;
    
    public GameScene(Player player) {
        this(player, null);
    }
    
    public GameScene(Player player, SaveData saveData) {
        super();
        this.player = player;
        this.activeKeys = new HashSet<>();
        this.platforms = new ArrayList<>();
        this.traversablePlatforms = new ArrayList<>();
        this.dragons = new ArrayList<>();
        this.potions = new ArrayList<>();
        
        if (saveData != null) {
            this.currentWave = saveData.getCurrentWave() - 1;
            this.demonKillCount = saveData.getDemonKillCount();
            this.startTime = System.currentTimeMillis() - (saveData.getPlayTime() * 1000);
            this.hasPotion1 = saveData.isHasPotion1();
            this.hasPotion2 = saveData.isHasPotion2();
            player.currentHealth = saveData.getCurrentHealth();
        } else {
            this.currentWave = 0;
            this.startTime = System.currentTimeMillis();
        }
        
        setupUI();
        setupControls();
        startGameLoop();
        createGameOverScreen();
        createPauseMenu();
        startNextWave();
    }

    private void setupUI() {
        setupBackground();
        setupPlatforms();
        setupHealthBar();
        setupWaveUI();
        setupInventory();
        setupSpecialAbility();
        
        // Initialisation du timer
        timerText = new Text("0s");
        timerText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        timerText.setFill(Color.WHITE);
        timerText.setStroke(Color.BLACK);
        timerText.setStrokeWidth(2);
        timerText.setX(GAME_WIDTH / 2 - 50);
        timerText.setY(50);
        this.getChildren().add(timerText);
        
        scene = new Scene(this, GAME_WIDTH, GAME_HEIGHT);
    }

    private void setupBackground() {
        try {
            Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/projet/projet/images/background.jpg")));
            ImageView background = new ImageView(backgroundImage);
            background.setFitWidth(GAME_WIDTH);
            background.setFitHeight(GAME_HEIGHT);
            this.getChildren().add(background);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fond: " + e.getMessage());
            Rectangle fallbackBackground = new Rectangle(GAME_WIDTH, GAME_HEIGHT, Color.DARKGRAY);
            this.getChildren().add(fallbackBackground);
        }
    }

    private void setupSpecialAbility() {
        VBox specialAbilityContainer = new VBox(5);
        specialAbilityContainer.setTranslateX(20);
        specialAbilityContainer.setTranslateY(80);

        specialAbilityText = new Text("Capacité Spéciale (Z)");
        specialAbilityText.setFont(Font.font("Arial Black", FontWeight.BOLD, 14));
        specialAbilityText.setFill(Color.WHITE);
        specialAbilityText.setStroke(Color.BLACK);
        specialAbilityText.setStrokeWidth(1);

        cooldownBar = new Rectangle(120, 8);
        cooldownBar.setArcWidth(10);
        cooldownBar.setArcHeight(10);
        cooldownBar.setStroke(Color.WHITE);
        cooldownBar.setStrokeWidth(2);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(10);

        specialAbilityText.setEffect(glow);
        cooldownBar.setEffect(new DropShadow(5, Color.BLACK));

        specialAbilityContainer.getChildren().addAll(specialAbilityText, cooldownBar);
        this.getChildren().add(specialAbilityContainer);
    }
    
    private void setupControls() {
        // Ajoute les gestionnaires d'événements à la Scene
        scene.setOnKeyPressed(e -> {
            activeKeys.add(e.getCode());
            
            switch (e.getCode()) {
                case UP:
                    if (!isJumping) jump();
                    break;
                case A:
                    if (!isGameOver) handleAttack();
                    break;
                case E:
                    if (hasPotion1) usePotion(1);
                    break;
                case R:
                    if (hasPotion2) usePotion(2);
                    break;
                case Z:
                    if (!isGameOver) player.useSpecialAbility();
                    break;
                case ESCAPE:
                    togglePauseMenu();
                    break;
            }
        });
        
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }
    
    private boolean isInRange(Player player, Dragon dragon, double range) {
        double dx = player.x - dragon.getX();
        double dy = player.y - dragon.getY();
        return Math.sqrt(dx * dx + dy * dy) <= range;
    }
    
    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }
    
    private void update() {
        if (!isGameOver && !isPaused) {
            handleMovement();
            applyGravity();
            checkCollision();
            updateDemons();
            updateHealthBar();
            updateTimer();
            checkPotionCollision();
            checkGameOver();
            updateCooldownBar();
            checkSpecialAbilityEffects();
        }
    }
    
    private void handleMovement() {
        if (activeKeys.contains(KeyCode.LEFT)) {
            player.moveLeft();
        }
        if (activeKeys.contains(KeyCode.RIGHT)) {
            player.moveRight();
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
        
        // Vérification pour toutes les plateformes traversables
        for (Rectangle currentPlatform : traversablePlatforms) {
            if (player.sprite.getBoundsInParent().intersects(currentPlatform.getBoundsInParent())) {
                // Si le joueur descend et est au-dessus de la plateforme
                if (verticalVelocity > 0 && 
                    player.y + player.sprite.getFitHeight() - 10 <= currentPlatform.getY() && 
                    !activeKeys.contains(KeyCode.DOWN)) {
                    player.y = currentPlatform.getY() - player.sprite.getFitHeight();
                    player.sprite.setY(player.y);
                    verticalVelocity = 0;
                    isJumping = false;
                    onPlatform = true;
                    break;
                }
                // Empêcher le joueur de passer à travers les plateformes par les côtés
                else if (verticalVelocity >= 0 && player.y + player.sprite.getFitHeight() > currentPlatform.getY()) {
                    double playerRight = player.x + player.sprite.getFitWidth();
                    double platformRight = currentPlatform.getX() + currentPlatform.getWidth();
                    double overlap = 5; // Marge de tolérance

                    // Collision depuis la gauche
                    if (playerRight > currentPlatform.getX() && playerRight < currentPlatform.getX() + overlap) {
                        player.x = currentPlatform.getX() - player.sprite.getFitWidth();
                    }
                    // Collision depuis la droite
                    else if (player.x < platformRight && player.x > platformRight - overlap) {
                        player.x = platformRight;
                    }
                    player.sprite.setX(player.x);
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
        
        // Changement de couleur en fonction de la vie
        if (healthPercentage > 0.6) {
            healthBarForeground.setFill(Color.rgb(0, 255, 0)); // Vert
        } else if (healthPercentage > 0.3) {
            healthBarForeground.setFill(Color.rgb(255, 165, 0)); // Orange
        } else {
            healthBarForeground.setFill(Color.rgb(255, 0, 0)); // Rouge
        }
    }
    
    private void handleDemonCollision(Dragon dragon) {
        if (dragon.getSprite().getBoundsInParent().intersects(player.sprite.getBoundsInParent())) {
            if (player instanceof Assassin && ((Assassin)player).isSpecialActive) {
                return; // Pas de dégâts en mode furtif
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDamageTime >= DAMAGE_COOLDOWN) {
                player.takeDamage(5);
                lastDamageTime = currentTime;
            }
        }
    }
    
    private void updateDemons() {
        // Supprimer les démons morts de la liste
        dragons.removeIf(dragon -> dragon.isDead());

        for (Dragon dragon : dragons) {
            // Vérifier si une plateforme bloque le chemin direct vers le joueur
            boolean canSeePlayer = true;
            
            // Vérifier toutes les plateformes entre le démon et le joueur
            for (Rectangle platform : platforms) {
                if (isBlockingPath(dragon, player, platform)) {
                    canSeePlayer = false;
                    break;
                }
            }
            for (Rectangle platform : traversablePlatforms) {
                if (isBlockingPath(dragon, player, platform)) {
                    canSeePlayer = false;
                    break;
                }
            }
            
            // Le démon ne se déplace que s'il peut "voir" le joueur
            if (canSeePlayer) {
                dragon.moveTowardsPlayer(player);
                handleDemonCollision(dragon);
            }
            
            // Toujours appliquer la gravité et les collisions
            for (Rectangle platform : platforms) {
                dragon.checkCollision(platform);
            }
            for (Rectangle platform : traversablePlatforms) {
                dragon.checkCollision(platform);
            }
        }

        // Vérifier si la vague est terminée (plus de démons à spawn ET plus de démons vivants)
        if (isWaveInProgress && remainingDemonsToSpawn == 0 && dragons.isEmpty()) {
            isWaveInProgress = false;
            if (currentWave < MAX_WAVES) {
                startNextWave();
            }
        }
    }
    
    private boolean isBlockingPath(Dragon dragon, Player player, Rectangle platform) {
        // Vérifier si la plateforme est entre le démon et le joueur
        double demonX = dragon.getX() + dragon.getSprite().getFitWidth() / 2;
        double demonY = dragon.getY() + dragon.getSprite().getFitHeight() / 2;
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
            
            // Arrêter le spawn de nouveaux démons
            if (waveSpawnTimeline != null) {
                waveSpawnTimeline.stop();
            }
            
            // Nettoyer les démons existants
            for (Dragon dragon : new ArrayList<>(dragons)) {
                this.getChildren().removeAll(dragon.getSprite(), dragon.getHealthBar());
            }
            dragons.clear();
            
            // Mettre à jour le texte en fonction de la cause de la mort
            Text causeText = (Text) gameOverScreen.getChildren().get(1);
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
        this.getChildren().add(gameOverScreen);
    }

    private void restartGame() {
        // Créer une nouvelle instance de la même classe de joueur
        Player newPlayer;
        if (player instanceof Warrior) {
            newPlayer = new Warrior();
        } else if (player instanceof Wizard) {
            newPlayer = new Wizard();
        } else if (player instanceof Assassin) {
            newPlayer = new Assassin();
        } else if (player instanceof Doctor) {
            newPlayer = new Doctor();
        } else {
            return; // Ne devrait jamais arriver
        }
        
        // Créer une nouvelle scène avec le même type de joueur
        Stage stage = (Stage) scene.getWindow();
        stage.setScene(new GameScene(newPlayer, null).getCurrentScene());
    }
    
    private void returnToMenu() {
        saveGame();
        Stage stage = (Stage) scene.getWindow();
        stage.setScene(new Scene(new MainMenu().getMainMenu(), GAME_WIDTH, GAME_HEIGHT));
    }
    
    private void spawnDemon() {
        if (isGameOver) return; // Ne pas spawner de démons si le jeu est terminé
        
        double spawnX = platform.getX() + random.nextDouble() * (platform.getWidth() - 100);
        double spawnY = platform.getY() - 130;
        Dragon dragon = new Dragon(spawnX, spawnY);
        dragons.add(dragon);
        this.getChildren().addAll(dragon.getSprite(), dragon.getHealthBar());
    }
    
    private void updateTimer() {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;
        timerText.setText(String.format("%ds", elapsedSeconds));
    }
    
    private void spawnPotion() {
        Rectangle platform = traversablePlatforms.get(random.nextInt(traversablePlatforms.size()));
        double potionX = platform.getX() + random.nextDouble() * (platform.getWidth() - 20) + 10;
        double potionY = platform.getY() - 20;
        
        Potion potion = new Potion(potionX, potionY);
        potions.add(potion);
        this.getChildren().add(potion.getSprite());
    }
    
    private void usePotion(int slot) {
        player.heal(30);
        if (slot == 1) {
            hasPotion1 = false;
            potionIndicator1.setVisible(false);
        } else {
            hasPotion2 = false;
            potionIndicator2.setVisible(false);
        }
    }
    
    private void checkPotionCollision() {
        for (Potion potion : new ArrayList<>(potions)) {
            if (player.sprite.getBoundsInParent().intersects(potion.getSprite().getBoundsInParent())) {
                if (!hasPotion1) {
                    hasPotion1 = true;
                    potionIndicator1.setVisible(true);
                    this.getChildren().remove(potion.getSprite());
                    potions.remove(potion);
                } else if (!hasPotion2) {
                    hasPotion2 = true;
                    potionIndicator2.setVisible(true);
                    this.getChildren().remove(potion.getSprite());
                    potions.remove(potion);
                }
            }
        }
    }
    
    private void handleAttack() {
        for (Dragon dragon : new ArrayList<>(dragons)) {
            if (isInRange(player, dragon, 140)) {
                if (player instanceof Wizard) {
                    Wizard wizard = (Wizard) player;
                    if (wizard.isLightningActive()) {
                        // One-shot kill avec l'éclair
                        dragon.takeDamage(dragon.getCurrentHealth());
                    } else {
                        // Attaque normale du Wizard
                        dragon.takeDamage(player.attackDamage);
                    }
                } else {
                dragon.takeDamage(player.attackDamage);
                }
                System.out.println("Dragon touché ! Vie restante : " + dragon.getCurrentHealth());
                
                if (dragon.isDead()) {
                    this.getChildren().removeAll(dragon.getSprite(), dragon.getHealthBar());
                    dragons.remove(dragon);
                    demonKillCount++;
                    
                    // Spawn une potion tous les 5 monstres tués si l'inventaire n'est pas plein
                    if (demonKillCount % 5 == 0) {
                        spawnPotion();
                    }

                    // Vérifier si c'était le dernier démon de la vague
                    if (isWaveInProgress && remainingDemonsToSpawn == 0 && dragons.isEmpty()) {
                        isWaveInProgress = false;
                        if (currentWave < MAX_WAVES) {
                            startNextWave();
                        }
                    }
                }
            }
        }
        if (player.canAttack()) {
            player.attack();
            player.resetAttackCooldown();
        }
    }
    
    private void startNextWave() {
        if (currentWave >= MAX_WAVES) return;
        
        currentWave++;
        
        // Calculer le nombre de démons pour cette vague
        remainingDemonsToSpawn = calculateDemonsForWave(currentWave);
        
        // Afficher l'annonce de la vague
        waveBigAnnouncement.setText("VAGUE " + currentWave);
        waveText.setText("Vague: " + currentWave + "/" + MAX_WAVES);
        waveBigAnnouncement.setVisible(true);
        
        // Timeline pour cacher l'annonce après 2.5 secondes et commencer le spawn
        Timeline announcementTimeline = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
            waveBigAnnouncement.setVisible(false);
            startWaveSpawning();
        }));
        announcementTimeline.play();
    }

    private int calculateDemonsForWave(int wave) {
        if (wave == 1) return 1;
        if (wave == 2) return 2;
        if (wave == 3) return 4;
        return Math.min(wave * 2, 15); // Maximum 15 démons par vague
    }

    private void startWaveSpawning() {
        isWaveInProgress = true;
        
        waveSpawnTimeline = new Timeline(new KeyFrame(Duration.seconds(0.7), event -> {
            if (remainingDemonsToSpawn > 0) {
                spawnDemon();
                remainingDemonsToSpawn--;
            } else {
                waveSpawnTimeline.stop();
                // On ne met pas isWaveInProgress à false tant que tous les démons ne sont pas morts
                // La vague reste en cours tant qu'il reste des démons
            }
        }));
        waveSpawnTimeline.setCycleCount(Timeline.INDEFINITE);
        waveSpawnTimeline.play();
    }
    
    private void createPauseMenu() {
        pauseMenu = new VBox(40);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");
        pauseMenu.setPrefSize(GAME_WIDTH, GAME_HEIGHT);

        Text pauseText = new Text("PAUSE");
        pauseText.setFont(Font.font("Arial Black", 80));
        pauseText.setFill(Color.WHITE);
        pauseText.setStroke(Color.BLACK);
        pauseText.setStrokeWidth(2);
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.7));
        shadow.setRadius(10);
        pauseText.setEffect(shadow);

        Button resumeButton = new Button("Reprendre");
        Button restartButton = new Button("Recommencer");
        Button menuButton = new Button("Menu Principal");

        // Style commun pour les boutons
        String buttonStyle = "-fx-background-color: #4CAF50;" +
                           "-fx-text-fill: white;" +
                           "-fx-font-size: 24px;" +
                           "-fx-background-radius: 25;" +
                           "-fx-padding: 15 30;";

        resumeButton.setPrefWidth(250);
        resumeButton.setPrefHeight(50);
        resumeButton.setStyle(buttonStyle);

        restartButton.setPrefWidth(250);
        restartButton.setPrefHeight(50);
        restartButton.setStyle(buttonStyle.replace("#4CAF50", "#2196F3"));

        menuButton.setPrefWidth(250);
        menuButton.setPrefHeight(50);
        menuButton.setStyle(buttonStyle.replace("#4CAF50", "#f44336"));

        resumeButton.setOnAction(e -> togglePauseMenu());
        restartButton.setOnAction(e -> restartGame());
        menuButton.setOnAction(e -> returnToMenu());

        pauseMenu.getChildren().addAll(pauseText, resumeButton, restartButton, menuButton);
        pauseMenu.setVisible(false);
        this.getChildren().add(pauseMenu);
    }

    private void togglePauseMenu() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);
        if (isPaused) {
            saveGame();
        }
    }
    
    public void saveGame() {
        SaveData saveData = new SaveData(
            getPlayerClassName(),
            currentWave,
            player.getCurrentHealth(),
            demonKillCount,
            (System.currentTimeMillis() - startTime) / 1000,
            hasPotion1,
            hasPotion2
        );
        SaveManager.saveGame(saveData);
    }

    private String getPlayerClassName() {
        return player.getClassName();
    }
    
    public Scene getCurrentScene() {
        return scene;
    }

    private void updateCooldownBar() {
        double percentage = player.getSpecialCooldownPercentage();
        cooldownBar.setWidth(100 * percentage);
        cooldownBar.setFill(percentage >= 1.0 ? Color.YELLOW : Color.GRAY);
    }

    private void checkSpecialAbilityEffects() {
        if (player.isSpecialActive && System.currentTimeMillis() >= player.specialAbilityEndTime) {
            player.isSpecialActive = false;
            if (player instanceof Warrior) {
                // Retirer le bouclier
                player.currentHealth = Math.min(player.currentHealth, player.maxHealth);
            } else if (player instanceof Wizard) {
                Wizard wizard = (Wizard) player;
                if (wizard.isLightningActive()) {
                    // Frappe tous les dragons avec la moitié de leurs PV
                    for (Dragon dragon : new ArrayList<>(dragons)) {
                        dragon.takeDamage(dragon.getCurrentHealth() / 2);
                        if (dragon.isDead()) {
                            this.getChildren().removeAll(dragon.getSprite(), dragon.getHealthBar());
                            dragons.remove(dragon);
                            demonKillCount++;
                        }
                    }
                }
            }
        }
    }

    private void setupPlatforms() {
        // Plateforme principale plus haute
        platform = new Rectangle(720, 30, Color.TRANSPARENT);
        platform.setX(115);
        platform.setY(GAME_HEIGHT * 0.65);
        
        // Plateformes latérales ajustées
        Rectangle rectangleGauche = new Rectangle(GAME_WIDTH * 0.2, 30, Color.TRANSPARENT);
        rectangleGauche.setX(GAME_WIDTH * 0.05);
        rectangleGauche.setY(platform.getY() - 160);
        
        Rectangle rectangleDroit = new Rectangle(GAME_WIDTH * 0.2, 30, Color.TRANSPARENT);
        rectangleDroit.setX(GAME_WIDTH * 0.75);
        rectangleDroit.setY(platform.getY() - 160);
        
        // Plateforme centrale ajustée (maintenant traversable et plus haute)
        Rectangle rectangleCentre = new Rectangle(GAME_WIDTH * 0.2, 30, Color.TRANSPARENT);
        rectangleCentre.setX(GAME_WIDTH * 0.4);
        rectangleCentre.setY(platform.getY() - 200); // 40 pixels plus haut que les plateformes latérales
        
        // Ajout des plateformes traversables
        traversablePlatforms.add(rectangleGauche);
        traversablePlatforms.add(rectangleCentre);
        traversablePlatforms.add(rectangleDroit);
        
        // Ajout de la plateforme principale non-traversable
        platforms.add(platform);
        
        // Ajout de tous les rectangles à la scène
        this.getChildren().addAll(platform, rectangleGauche, rectangleCentre, rectangleDroit);
        
        // Ajoute le sprite du joueur à la scène
        this.getChildren().add(player.sprite);
        
        // Position initiale du sprite
        player.sprite.setX(player.x);
        player.sprite.setY(player.y);
    }

    private void setupHealthBar() {
        // Création du conteneur pour la barre de vie
        VBox healthContainer = new VBox(5);
        healthContainer.setTranslateX(20);
        healthContainer.setTranslateY(20);

        // Texte pour les PV
        Text healthText = new Text("SANTÉ");
        healthText.setFont(Font.font("Arial Black", FontWeight.BOLD, 16));
        healthText.setFill(Color.WHITE);
        healthText.setStroke(Color.BLACK);
        healthText.setStrokeWidth(1);

        // Barre de vie principale
        healthBarForeground = new Rectangle(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBarForeground.setArcWidth(20);
        healthBarForeground.setArcHeight(20);
        healthBarForeground.setStroke(Color.WHITE);
        healthBarForeground.setStrokeWidth(2);
        
        // Effet de brillance pour la barre
        DropShadow glow = new DropShadow();
        glow.setColor(Color.WHITE);
        glow.setRadius(15);
        healthBarForeground.setEffect(glow);
        
        // Ajout des éléments au conteneur
        healthContainer.getChildren().addAll(healthText, healthBarForeground);
        this.getChildren().add(healthContainer);
    }

    private void setupWaveUI() {
        waveText = new Text("Vague: 1/" + MAX_WAVES);
        waveText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        waveText.setFill(Color.RED);
        waveText.setStroke(Color.BLACK);
        waveText.setStrokeWidth(1);
        waveText.setX(GAME_WIDTH - 180);
        waveText.setY(30);

        waveBigAnnouncement = new Text("");
        waveBigAnnouncement.setFont(Font.font("Arial Black", 72));
        waveBigAnnouncement.setFill(Color.WHITE);
        waveBigAnnouncement.setStroke(Color.BLACK);
        waveBigAnnouncement.setStrokeWidth(3);
        waveBigAnnouncement.setVisible(false);
        waveBigAnnouncement.setX(GAME_WIDTH / 2 - 200);
        waveBigAnnouncement.setY(GAME_HEIGHT / 2);

        this.getChildren().addAll(waveText, waveBigAnnouncement);
    }

    private void setupInventory() {
        inventorySlot1 = new Rectangle(GAME_WIDTH / 2 - 55, GAME_HEIGHT - 70, 50, 50);
        inventorySlot2 = new Rectangle(GAME_WIDTH / 2 + 5, GAME_HEIGHT - 70, 50, 50);
        
        inventorySlot1.setFill(Color.TRANSPARENT);
        inventorySlot2.setFill(Color.TRANSPARENT);
        inventorySlot1.setStroke(Color.WHITE);
        inventorySlot2.setStroke(Color.WHITE);
        
        try {
            Image potionImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/projet/projet/images/potion.png")));
            
            potionIndicator1 = new ImageView(potionImage);
            potionIndicator2 = new ImageView(potionImage);
            
            potionIndicator1.setFitWidth(40);
            potionIndicator1.setFitHeight(40);
            potionIndicator2.setFitWidth(40);
            potionIndicator2.setFitHeight(40);
            
            potionIndicator1.setX(inventorySlot1.getX() + 5);
            potionIndicator1.setY(inventorySlot1.getY() + 5);
            potionIndicator2.setX(inventorySlot2.getX() + 5);
            potionIndicator2.setY(inventorySlot2.getY() + 5);
            
            potionIndicator1.setVisible(false);
            potionIndicator2.setVisible(false);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image de potion: " + e.getMessage());
        }
        
        this.getChildren().addAll(inventorySlot1, inventorySlot2, potionIndicator1, potionIndicator2);
    }
} 

