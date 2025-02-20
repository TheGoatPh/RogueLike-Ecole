package com.projet.projet;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.Objects;

public class Potion {
    private Node sprite;
    private double x;
    private double y;
    private static final int HEAL_AMOUNT = 30;
    
    public Potion(double x, double y) {
        this.x = x;
        this.y = y;
        
        try {
            Image potionImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/projet/projet/images/potion.png")));
            ImageView imageView = new ImageView(potionImage);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            imageView.setX(x);
            imageView.setY(y);
            sprite = imageView;
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image de potion: " + e.getMessage());
            // Fallback au cercle vert si l'image ne charge pas
            Circle circle = new Circle(10, Color.GREEN);
            circle.setCenterX(x + 10);
            circle.setCenterY(y + 10);
            sprite = circle;
        }
    }
    
    public Node getSprite() {
        return sprite;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public int getHealAmount() {
        return HEAL_AMOUNT;
    }
} 