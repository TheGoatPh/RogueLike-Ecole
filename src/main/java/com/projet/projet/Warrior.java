package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Warrior extends Player {
    private boolean isDefending;
    
    public Warrior() {
        super(150, 20, 2.5); // Plus de vie, dégâts moyens, moins rapide
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
        isDefending = !isDefending;
        if (isDefending) {
            System.out.println("Le guerrier se met en position défensive !");
        } else {
            System.out.println("Le guerrier arrête de se défendre !");
        }
    }

    protected void updateSprite() {
        // Mise à jour du sprite en fonction de la direction
    }
} 
