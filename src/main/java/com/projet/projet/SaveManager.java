package com.projet.projet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;

public class SaveManager {
    private static final String SAVE_FILE = "game_save.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveGame(SaveData saveData) {
        try {
            String jsonData = gson.toJson(saveData);
            Files.write(Paths.get(SAVE_FILE), jsonData.getBytes());
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    public static SaveData loadGame() {
        try {
            if (Files.exists(Paths.get(SAVE_FILE))) {
                String jsonData = new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
                return gson.fromJson(jsonData, SaveData.class);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement: " + e.getMessage());
        }
        return null;
    }

    public static boolean hasSaveFile() {
        return Files.exists(Paths.get(SAVE_FILE));
    }

    public static void deleteSave() {
        try {
            Files.deleteIfExists(Paths.get(SAVE_FILE));
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de la sauvegarde: " + e.getMessage());
        }
    }
} 