package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Assassin extends Player {
    public Assassin() {
        super(80, 25, 5.0); // Moins de vie, plus de dégâts, plus rapide
        try {
            String imagePath = "/com/projet/projet/images/assasin.png";
            sprite.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image de l'assassin: " + e.getMessage());
        }
    }

    private void loadSprites() {
        // Chargement des sprites
        Image rightSprite = new Image(getClass().getResourceAsStream("/assasin.png"));
        Image leftSprite = new Image(getClass().getResourceAsStream("/assasin.png"));
        sprite = new ImageView(rightSprite);
    }

    @Override
    public void attack() {
        System.out.println("L'assassin frappe avec sa dague !");
    }

    @Override
    public void useSpecialAbility() {
        System.out.println("L'assassin devient furtif !");
    }

    protected void updateSprite() {
        // Mise à jour du sprite en fonction de la direction
    }
} 
