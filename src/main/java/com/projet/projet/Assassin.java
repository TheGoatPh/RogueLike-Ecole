package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Assassin extends Player {

    public Assassin() {
        super(80, 15, 5.0); // Moins de vie, dégâts réduits, plus rapide
        this.attackCooldown = 500; // Attaque rapide (0.5 secondes)
        
        try {
            // Chargement des deux sprites
            String leftImagePath = "/com/projet/projet/images/assassin_left.png";
            String rightImagePath = "/com/projet/projet/images/assassin_right.png";
            
            this.leftSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream(leftImagePath)));
            this.rightSprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream(rightImagePath)));
            
            // Par défaut, on commence tourné vers la droite
            sprite.setImage(rightSprite);
        } catch (Exception e) {
            System.err.println("Impossible de charger les images de l'assassin: " + e.getMessage());
        }
    }

    /*
    private void loadSprites() {
        // Chargement des sprites
        Image rightSprite = new Image(getClass().getResourceAsStream("/assasin.png"));
        sprite = new ImageView(rightSprite);
    }
    */


    // ATtAQUE DE L'ASSASSIN
    @Override
    public void attack() {
        // Attaque rapide de l'assassin
        System.out.println("L'assassin frappe rapidement avec sa dague !");
    }


    // ABBILITE DE L'ASSASIN
    @Override
    public void useSpecialAbility() {
        if (canUseSpecialAbility()) {
            isSpecialActive = true;
            lastSpecialAbilityTime = System.currentTimeMillis();
            specialAbilityEndTime = System.currentTimeMillis() + 5000; // 5 secondes de furtivité
            System.out.println("L'assassin devient furtif !");
        }
    }

    @Override
    public String getClassName() {
        return "ASSASSIN";
    }
} 
