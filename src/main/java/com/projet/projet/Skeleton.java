package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Skeleton {
    private double x;
    private double y;
    private ImageView sprite;
    private double speed = 2.0;
    private static final int SPRITE_SIZE = 50;
    private double verticalVelocity = 0;
    private static final double GRAVITY = 0.2;
    
    public Skeleton(double x, double y) {
        this.x = x;
        this.y = y;
        
        try {
            String imagePath = "/com/projet/projet/images/skeleton.png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            sprite = new ImageView(image);
            sprite.setFitWidth(SPRITE_SIZE);
            sprite.setFitHeight(SPRITE_SIZE);
            sprite.setX(x);
            sprite.setY(y);
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image du squelette: " + e.getMessage());
            // Créer un sprite par défaut si l'image ne charge pas
            sprite = new ImageView();
            sprite.setFitWidth(SPRITE_SIZE);
            sprite.setFitHeight(SPRITE_SIZE);
        }
    }
    
    public void moveTowardsPlayer(Player player) {
        // Déplacement horizontal vers le joueur
        if (x < player.x) {
            x += speed;
        } else if (x > player.x) {
            x -= speed;
        }
        
        // Application de la gravité
        verticalVelocity += GRAVITY;
        y += verticalVelocity;
        
        // Mise à jour de la position du sprite
        sprite.setX(x);
        sprite.setY(y);
    }
    
    public void checkCollision(javafx.scene.shape.Rectangle platform) {
        if (sprite.getBoundsInParent().intersects(platform.getBoundsInParent())) {
            if (verticalVelocity > 0) { // Si le squelette descend
                y = platform.getY() - sprite.getFitHeight();
                sprite.setY(y);
                verticalVelocity = 0;
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
