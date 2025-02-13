package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Doctor extends Player {
    private int healingPower;
    
    public Doctor() {
        super(120, 10, 3.0); // vie = 120, dégâts = 10, vitesse = 3
        this.healingPower = 100;
        try {
            // Chargement des deux sprites
            String leftImagePath = "/com/projet/projet/images/doctor_left.png";
            String rightImagePath = "/com/projet/projet/images/doctor_right.png";
            
            this.leftSprite = new Image(getClass().getResourceAsStream(leftImagePath));
            this.rightSprite = new Image(getClass().getResourceAsStream(rightImagePath));
            
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
        if (healingPower >= 30) {
            // Zone de soin
            healingPower -= 30;
            System.out.println("Le docteur crée une zone de soin !");
        }
    }

    protected void updateSprite() {
        // Mise à jour du sprite en fonction de la direction
    }
} 
