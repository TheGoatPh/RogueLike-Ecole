package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Warrior extends Player {
    private boolean isDefending;
    
    public Warrior() {
        super(150, 25, 2.5); // Plus de vie, dégâts élevés, moins rapide
        this.attackCooldown = 1500; // Attaque lente (1.5 secondes)
        this.isDefending = false;
        try {
            // Chargement des deux sprites
            String leftImagePath = "/com/projet/projet/images/warrior_left.png";
            String rightImagePath = "/com/projet/projet/images/warrior_right.png";
            
            this.leftSprite = new Image(getClass().getResourceAsStream(leftImagePath));
            this.rightSprite = new Image(getClass().getResourceAsStream(rightImagePath));
            
            // Par défaut, on commence tourné vers la droite
            sprite.setImage(rightSprite);
        } catch (Exception e) {
            System.err.println("Impossible de charger les images du warrior: " + e.getMessage());
        }
    }
    
    @Override
    public void attack() {
        System.out.println("Le guerrier frappe avec son épée !");
    }
    
    @Override
    public void useSpecialAbility() {
        if (canUseSpecialAbility()) {
            int missingHealth = maxHealth - currentHealth;
            int shieldAmount = missingHealth / 3;
            currentHealth += shieldAmount;
            isSpecialActive = true;
            lastSpecialAbilityTime = System.currentTimeMillis();
            specialAbilityEndTime = System.currentTimeMillis() + 5000; // 5 secondes
            System.out.println("Le guerrier active son bouclier !");
        }
    }


} 
