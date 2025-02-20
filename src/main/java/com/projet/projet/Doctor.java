package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Doctor extends Player {
    private int healingPower;
    
    public Doctor() {
        super(120, 20, 3.0); // vie = 120, dégâts = 20, vitesse = 3
        this.attackCooldown = 1000; // Attaque normale (1 seconde)
        this.healingPower = 100;
        try {
            // Chargement des deux sprites
            String leftImagePath = "/com/projet/projet/images/doctor_left.png";
            String rightImagePath = "/com/projet/projet/images/doctor_right.png";
            
            this.leftSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream(leftImagePath)));
            this.rightSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream(rightImagePath)));
            
            // Par défaut, on commence tourné vers la droite
            sprite.setImage(rightSprite);
        } catch (Exception e) {
            System.err.println("Impossible de charger les images du doctor: " + e.getMessage());
        }
    }
    
    @Override
    public void attack() {
        if (healingPower >= 10) {
            // Attaque de soin
            healingPower -= 10;
            System.out.println("Le docteur lance une attaque de soin !");
        }
    }

    @Override
    public void useSpecialAbility() {
        if (canUseSpecialAbility()) {
            int missingHealth = maxHealth - currentHealth;
            heal(missingHealth / 3);
            lastSpecialAbilityTime = System.currentTimeMillis();
            System.out.println("Le docteur se soigne !");
        }
    }

} 
