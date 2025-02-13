package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Wizard extends Player {
    private int mana;
    
    public Wizard() {
        super(100, 15, 3.0); // vie = 100, dégâts = 15, vitesse = 3
        this.mana = 100;
        try {
            String imagePath = "/com/projet/projet/images/wizard_left.png";
            sprite.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image du wizard: " + e.getMessage());
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

    protected void updateSprite() {
        // Mise à jour du sprite en fonction de la direction
    }
} 
