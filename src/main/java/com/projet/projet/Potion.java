package com.projet.projet;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Potion {
    private Circle sprite;
    private double x;
    private double y;
    private static final int HEAL_AMOUNT = 30;
    
    public Potion(double x, double y) {
        this.x = x;
        this.y = y;
        this.sprite = new Circle(x, y, 10, Color.GREEN);
    }
    
    public Circle getSprite() {
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