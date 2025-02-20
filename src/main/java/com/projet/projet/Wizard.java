package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Wizard extends Player {
    private int mana;
    
    public Wizard() {
        super(100, 20, 3.0); // vie = 100, dégâts = 20, vitesse = 3
        this.attackCooldown = 1000; // Attaque normale (1 seconde)
        this.mana = 100;
        try {
            // Chargement des deux sprites
            String leftImagePath = "/com/projet/projet/images/wizard_left.png";
            String rightImagePath = "/com/projet/projet/images/wizard_right.png";
            
            this.leftSprite = new Image(getClass().getResourceAsStream(leftImagePath));
            this.rightSprite = new Image(getClass().getResourceAsStream(rightImagePath));
            
            // Par défaut, on commence tourné vers la droite
            sprite.setImage(rightSprite);
        } catch (Exception e) {
            System.err.println("Impossible de charger les images du wizard: " + e.getMessage());
        }
    }
    
    @Override
    public void attack() {
        if (mana >= 10) {
            // Lance un sort
            mana -= 10;
            System.out.println("Le magicien lance un sort !");
        }
    }
    
    @Override
    public void useSpecialAbility() {
        if (mana >= 30) {
            // Crée un bouclier
            mana -= 30;
            System.out.println("Le magicien crée un bouclier !");
        }
    }


} 
