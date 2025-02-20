package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Player extends Entity {
    protected static final long DEFAULT_SPECIAL_ABILITY_COOLDOWN = 15000; // 15 secondes par défaut
    protected long lastSpecialAbilityTime = 0;
    protected long specialAbilityEndTime = 0;
    protected boolean isSpecialActive = false;
    protected long attackCooldown;
    protected long lastAttackTime = 0;
    
    public Player(int maxHealth, int attackDamage, double speed) {
        super(maxHealth, attackDamage, speed);
    }
    
    public abstract void attack();
    public abstract void useSpecialAbility();
    
    protected boolean canUseSpecialAbility() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastSpecialAbilityTime >= DEFAULT_SPECIAL_ABILITY_COOLDOWN && !isSpecialActive;
    }
    
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= attackCooldown;
    }
    
    public void resetAttackCooldown() {
        lastAttackTime = System.currentTimeMillis();
    }
    
    public boolean isSpecialActive() {
        if (isSpecialActive && System.currentTimeMillis() > specialAbilityEndTime) {
            isSpecialActive = false;
        }
        return isSpecialActive;
    }

    public double getSpecialCooldownPercentage() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastSpecialAbilityTime;
        return Math.min(1.0, (double) elapsedTime / DEFAULT_SPECIAL_ABILITY_COOLDOWN);
    }
} 
