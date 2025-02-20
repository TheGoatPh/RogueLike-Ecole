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
    protected long lastAttackTime;
    protected int attackDamage;
    protected long attackCooldown; // en millisecondes
    protected long lastSpecialAbilityTime = 0;
    protected static final long SPECIAL_ABILITY_COOLDOWN = 10000; // 10 secondes
    protected boolean isSpecialActive = false;
    protected long specialAbilityEndTime = 0;
    
    public Player(int health, int damage, double speed) {
        this.x = 400;
        this.y = 300;
        this.speed = speed;
        this.maxHealth = health;
        this.currentHealth = health;
        this.attackDamage = damage;
        this.lastAttackTime = 0;
        
        sprite = new ImageView();
        sprite.setFitWidth(50);
        sprite.setFitHeight(50);
        sprite.setPreserveRatio(true);
    }
    
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= attackCooldown;
    }
    
    public void performAttack() {
        if (canAttack()) {
            attack();
            lastAttackTime = System.currentTimeMillis();
        }
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

    public boolean canUseSpecialAbility() {
        return System.currentTimeMillis() - lastSpecialAbilityTime >= SPECIAL_ABILITY_COOLDOWN;
    }

    public double getSpecialCooldownPercentage() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastSpecialAbilityTime;
        return Math.min(1.0, (double) elapsedTime / SPECIAL_ABILITY_COOLDOWN);
    }
} 
