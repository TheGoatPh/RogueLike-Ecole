package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Dragon {
    private double x;
    private double y;
    private ImageView sprite;
    private double speed = 0.8;
    private double velocityY = 0;
    private final double gravity = 0.4;
    private Image idleImage;
    private Image attackImage;
    private boolean isAttacking = false;
    private long lastAttackTime = 0;
    private static final long ATTACK_COOLDOWN = 2000; // 2 secs entre chaque attaque
    private static final double ATTACK_RANGE = 60; // Range d'attaque
    private static final int ATTACK_DAMAGE = 5; // Dammage par attaque
    private int maxHealth = 50;
    private int currentHealth = 50;
    private Rectangle healthBar;
    private static final int HEALTH_BAR_WIDTH = 50;
    private static final int HEALTH_BAR_HEIGHT = 5;
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 1000; // 1 seconde en millisecondes
    
    public Dragon(double x, double y) {
        //super(x, y);
        this.x = x;
        this.y = y;
        
        // Création de la barre de vie
        healthBar = new Rectangle(x, y - 10, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBar.setFill(Color.RED);

        // CHARGEMENT DU Sprite du dragon et mise sur la map
        try {
            this.idleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/projet/projet/images/demon-idle.gif")));
            this.attackImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/projet/projet/images/demon-attack.gif")));
            this.sprite = new ImageView(idleImage);
            this.sprite.setX(x);
            this.sprite.setY(y);
            this.sprite.setFitWidth(160);
            this.sprite.setFitHeight(160);
        } catch (Exception e) {
            System.err.println("Erreur chargement image dragon: " + e.getMessage());
        }
    }
    
    public void moveTowardsPlayer(Player player) {
        // Calcul de la distance avec le joueur
        double distanceToPlayer = Math.abs(x - player.x);
        
        // Si le démon est dans la range d'attaque
        if (distanceToPlayer <= ATTACK_RANGE) {
            // Vérifier si le cooldown est passé
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
                attack(player);
                lastAttackTime = currentTime;
            }
        } else {
            // Sinon, se déplacer vers le joueur
            if (x < player.x) {
                x += speed;
            } else if (x > player.x) {
                x -= speed;
            }
            sprite.setImage(idleImage);
            isAttacking = false;
        }
        
        // Application de la gravité
        velocityY += gravity;
        y += velocityY;
        
        // Mise à jour de la position du sprite
        sprite.setX(x);
        sprite.setY(y);
        
        // Mise à jour de la position de la barre de vie
        healthBar.setX(x + (sprite.getFitWidth() - HEALTH_BAR_WIDTH) / 2);
        healthBar.setY(y - 10);
    }
    
    private void attack(Player player) {
        isAttacking = true;
        sprite.setImage(attackImage);
        
        // Vérifier si le joueur est à portée et infliger des dégâts
        double distanceToPlayer = Math.abs(x - player.x);
        if (distanceToPlayer <= ATTACK_RANGE) {
            player.takeDamage(ATTACK_DAMAGE);
        }
        
        // Retourner à l'animation idle après un court délai
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Durée de l'animation d'attaque
                if (!isAttacking) {
                    sprite.setImage(idleImage);
                }
            } catch (InterruptedException e) {
                System.err.println("Erreur chargement image dragon: " + e.getMessage());
            }
        }).start();
    }
    
    public void checkCollision(Rectangle platform) {
        if (y + sprite.getFitHeight() > platform.getY() && 
            y < platform.getY() + platform.getHeight() &&
            x + sprite.getFitWidth() > platform.getX() && 
            x < platform.getX() + platform.getWidth()) {
            
            if (velocityY > 0) {
                y = platform.getY() - sprite.getFitHeight();
                velocityY = 0;
            }
        }
    }
    
    public ImageView getSprite() {
        return sprite;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public void takeDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
        updateHealthBar();
    }

    private void updateHealthBar() {
        double healthPercentage = (double) currentHealth / maxHealth;
        healthBar.setWidth(HEALTH_BAR_WIDTH * healthPercentage);
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public Rectangle getHealthBar() {
        return healthBar;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
} 
