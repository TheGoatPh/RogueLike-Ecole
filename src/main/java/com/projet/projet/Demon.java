package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Demon {
    private double x;
    private double y;
    private ImageView sprite;
    private double speed = 0.8;
    private double velocityY = 0;
    private final double gravity = 0.4;
    private boolean isJumping = false;
    private Image idleImage;
    private Image attackImage;
    private boolean isAttacking = false;
    private long lastAttackTime = 0;
    private static final long ATTACK_COOLDOWN = 2000; // 2 secondes entre chaque attaque
    private static final double ATTACK_RANGE = 150; // Portée d'attaque
    private static final int ATTACK_DAMAGE = 5; // Dégâts par attaque
    
    public Demon(double x, double y) {
        this.x = x;
        this.y = y;
        
        try {
            this.idleImage = new Image(getClass().getResourceAsStream("/com/projet/projet/images/demon-idle.gif"));
            this.attackImage = new Image(getClass().getResourceAsStream("/com/projet/projet/images/demon-attack.gif"));
            this.sprite = new ImageView(idleImage);
            this.sprite.setX(x);
            this.sprite.setY(y);
            this.sprite.setFitWidth(240);  // Augmenté de 160 à 240
            this.sprite.setFitHeight(240); // Augmenté de 160 à 240
        } catch (Exception e) {
            System.err.println("Erreur chargement image demon: " + e.getMessage());
        }
    }
    
    public void moveTowardsPlayer(Player player) {
        // Calcul de la distance avec le joueur
        double distanceToPlayer = Math.abs(x - player.x);
        
        // Si le démon est dans la portée d'attaque
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
                e.printStackTrace();
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
                isJumping = false;
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
} 