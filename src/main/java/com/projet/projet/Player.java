package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Player {
    // Position du joueur
    protected double x;
    protected double y;
    
    // Statistiques du joueur
    protected int health;
    protected int damage;
    protected double speed;
    
    // Sprite du joueur
    protected ImageView sprite;
    
    // Ajout des variables pour les sprites directionnels
    protected Image leftSprite;
    protected Image rightSprite;
    
    public Player(int health, int damage, double speed) {
        // Initialisation des statistiques
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        
        // Position initiale ajustée
        this.x = 900;    // Même position X
        this.y = 550;    // Plus haut (était 650)
        
        // Chargement du sprite par défaut
        try {
            String imagePath = "/com/projet/projet/images/default_player.png";
            sprite = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
            sprite.setFitWidth(100);
            sprite.setFitHeight(100);
            sprite.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image par défaut: " + e.getMessage());
            sprite = new ImageView();
        }
    }
    
    // Méthodes de déplacement
    public void moveLeft() {
        x -= speed;
        sprite.setX(x);
        sprite.setImage(leftSprite); // Change le sprite vers la gauche
    }
    
    public void moveRight() {
        x += speed;
        sprite.setX(x);
        sprite.setImage(rightSprite); // Change le sprite vers la droite
    }
    
    // Nouvelles méthodes de déplacement vertical
    public void moveUp() {
        y -= speed;
        sprite.setY(y);
    }
    
    public void moveDown() {
        y += speed;
        sprite.setY(y);
    }
    
    // Méthodes abstraites que chaque classe devra implémenter
    public abstract void attack();
    public abstract void useSpecialAbility();
} 
