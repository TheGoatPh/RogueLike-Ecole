package com.projet.projet;

import javafx.scene.image.Image;

public class Warrior extends Player {
    private boolean isDefending;
    
    public Warrior() {
        super(150, 20, 2.5); // Plus de vie, dégâts moyens, moins rapide
        try {
            String imagePath = "/com/projet/projet/images/warrior.png";
            sprite.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image du warrior: " + e.getMessage());
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
} 
