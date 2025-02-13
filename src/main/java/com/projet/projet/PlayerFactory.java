package com.projet.projet;

public class PlayerFactory {
    public static Player createPlayer(String playerClass) {
        return switch (playerClass) {
            case "ASSASSIN" -> new Assassin();
            case "WIZARD" -> new Wizard();
            case "WARRIOR" -> new Warrior();
            case "DOCTOR" -> new Doctor();
            default -> throw new IllegalArgumentException("Invalid player class");
        };
    }
} 
