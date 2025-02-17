package com.projet.projet;

public class SaveData {
    private String playerClass;
    private int currentWave;
    private int currentHealth;
    private int demonKillCount;
    private long playTime;
    private boolean hasPotion1;
    private boolean hasPotion2;

    public SaveData() {}

    public SaveData(String playerClass, int currentWave, int currentHealth, int demonKillCount, long playTime, boolean hasPotion1, boolean hasPotion2) {
        this.playerClass = playerClass;
        this.currentWave = currentWave;
        this.currentHealth = currentHealth;
        this.demonKillCount = demonKillCount;
        this.playTime = playTime;
        this.hasPotion1 = hasPotion1;
        this.hasPotion2 = hasPotion2;
    }

    // Getters et Setters
    public String getPlayerClass() { return playerClass; }
    public void setPlayerClass(String playerClass) { this.playerClass = playerClass; }
    
    public int getCurrentWave() { return currentWave; }
    public void setCurrentWave(int currentWave) { this.currentWave = currentWave; }
    
    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }
    
    public int getDemonKillCount() { return demonKillCount; }
    public void setDemonKillCount(int demonKillCount) { this.demonKillCount = demonKillCount; }
    
    public long getPlayTime() { return playTime; }
    public void setPlayTime(long playTime) { this.playTime = playTime; }
    
    public boolean isHasPotion1() { return hasPotion1; }
    public void setHasPotion1(boolean hasPotion1) { this.hasPotion1 = hasPotion1; }
    
    public boolean isHasPotion2() { return hasPotion2; }
    public void setHasPotion2(boolean hasPotion2) { this.hasPotion2 = hasPotion2; }
} 