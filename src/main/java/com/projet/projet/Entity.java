package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Entity {
    public double x;
    public double y;
    public ImageView sprite;
    protected Image leftSprite;
    protected Image rightSprite;
    protected double speed;
    protected int maxHealth;
    protected int currentHealth;
    protected int attackDamage;

    public Entity(int maxHealth, int attackDamage, double speed) {
        this.x = 400;
        this.y = 300;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackDamage = attackDamage;
        
        sprite = new ImageView();
        sprite.setFitWidth(50);
        sprite.setFitHeight(50);
        sprite.setPreserveRatio(true);
    }

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