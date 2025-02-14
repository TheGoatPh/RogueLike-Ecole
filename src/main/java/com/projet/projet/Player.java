package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Player {
    public double x;
    public double y;
    public ImageView sprite;
    protected Image leftSprite;
    protected Image rightSprite;
    private double speed;
    protected int maxHealth;
    protected int currentHealth;
    
    public Player(int health, int damage, double speed) {
        this.x = 400;
        this.y = 300;
        this.speed = speed;
        this.maxHealth = health;
        this.currentHealth = health;
        
        sprite = new ImageView();
        sprite.setFitWidth(80);
        sprite.setFitHeight(80);
        sprite.setPreserveRatio(true);
    }
    
    public abstract void attack();
    public abstract void useSpecialAbility();
    
    public void moveLeft() {
        x -= speed;
        sprite.setImage(leftSprite);
    }
    
    public void moveRight() {
        x += speed;
        sprite.setImage(rightSprite);
    }
    
    public void takeDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
    }
    
    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }
    
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
} 
