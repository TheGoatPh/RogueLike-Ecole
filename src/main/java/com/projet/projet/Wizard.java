package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Wizard extends Player {
    private boolean isLightningActive = false;
    private static final long SPECIAL_ABILITY_COOLDOWN = 15000; // 15 secondes
    
    public Wizard() {
        super(100, 20, 2.0); // vie = 100, degats = 20, vitesse = 2
        this.attackCooldown = 1000; // Attaque normale (1 seconde)
        try {
            // Chargement des deux sprites
            String leftImagePath = "/com/projet/projet/images/wizard_left.png";
            String rightImagePath = "/com/projet/projet/images/wizard_right.png";
            
            this.leftSprite = new Image(getClass().getResourceAsStream(leftImagePath));
            this.rightSprite = new Image(getClass().getResourceAsStream(rightImagePath));
            
            // Par def tourné vers la droite
            sprite.setImage(rightSprite);
        } catch (Exception e) {
            System.err.println("Impossible de charger les images du wizard: " + e.getMessage());
        }
    }
    
    @Override
    public void attack() {
        System.out.println("Le magicien lance un sort !");
    }
    
    @Override
    public void useSpecialAbility() {
        if (canUseSpecialAbility()) {
            isSpecialActive = true;
            isLightningActive = true;
            lastSpecialAbilityTime = System.currentTimeMillis();
            specialAbilityEndTime = System.currentTimeMillis() + 500; // 0.5 seconde d'effet
            System.out.println("Le magicien déchaîne la foudre sur tous les ennemis !");
        }
    }

    public boolean isLightningActive() {
        if (isLightningActive && System.currentTimeMillis() > specialAbilityEndTime) {
            isLightningActive = false;
            isSpecialActive = false;
        }
        return isLightningActive;
    }

    @Override
    public String getClassName() {
        return "WIZARD";
    }
} 
