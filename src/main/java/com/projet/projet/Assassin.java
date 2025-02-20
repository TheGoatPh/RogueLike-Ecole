package com.projet.projet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Assassin extends Player {

    public Assassin() {
        super(80, 15, 5.0); // Moins de vie, dégâts réduits, plus rapide
        this.attackCooldown = 500; // Attaque rapide -> 0.5 secondes


        // LOAD LE SPRITE (PERSONNAGE)
        try {
            String imagePath = "/com/projet/projet/images/assassin.png";
            sprite.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image de l'assassin: " + e.getMessage());
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
        // TO DO: IMPLEMENTER
        System.out.println("L'assassin frappe avec sa dague !");
    }


    // ABBILITE DE L'ASSASIN
    @Override
    public void useSpecialAbility() {
        // TO DO: IMPLEMENTER
        System.out.println("L'assassin devient furtif !");
    }

} 
