package com.projet.projet;

import javafx.scene.image.Image;

public class Doctor extends Player {
    private int medkits;
    private final int HEAL_AMOUNT = 50;

    public Doctor() {
        super(100, 10, 3.0); // Vie moyenne, faibles dégâts, vitesse moyenne
        this.medkits = 3;
        try {
            String imagePath = "/com/projet/projet/images/doctor.png";
            sprite.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image du doctor: " + e.getMessage());
        }
    }
    
    @Override
    public void attack() {
        System.out.println("Le docteur attaque avec sa seringue !");
    }
    
    @Override
    public void useSpecialAbility() {
        if (medkits > 0) {
            health += HEAL_AMOUNT;
            medkits--;
            System.out.println("Le docteur utilise un medkit ! Santé : " + health);
        } else {
            System.out.println("Plus de medkits disponibles !");
        }
    }
} 
