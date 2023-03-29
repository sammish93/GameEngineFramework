package no.hiof.samuelcd.tbage.models.npcs;


import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.util.TreeMap;

public class Enemy extends NonPlayableCharacter {

    private double maxHealth;
    private double currentHealth;
    private double[] damage = new double[2];
    private String enemyType;


    private Enemy(String name, int maxHealth, int minDamage, int maxDamage, TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType) {
        super(name, abilities, items);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage[0] = minDamage;
        this.damage[1] = maxDamage;
        this.enemyType = enemyType;
    }

    public static Enemy create() {
        return new Enemy(null, 10, 1, 2, null, null, null);
    }

    public static Enemy create(String name) {
        return new Enemy(name, 10, 1, 3, null, null, null);
    }

    public static Enemy create(String name, int health, int minDamage, int maxDamage, TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType) {
        return new Enemy(name, health, minDamage, maxDamage, abilities, items, enemyType);
    }

    @Override
    public void processAbilities() {
        // Iterates through abilityPool to find which events (onEncounterStart and onEncounterFinish) are TRUE.
    }

    @Override
    public void processItems() {
        // Determines which items have dropped from a defeated enemy.
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getMinDamage() {
        return damage[0];
    }

    public void setMinDamage(int minDamage) {
        this.damage[0] = minDamage;
    }

    public double getMaxDamage() {
        return damage[1];
    }

    public void setMaxDamage(int maxDamage) {
        this.damage[1] = maxDamage;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(String enemyType) {
        this.enemyType = enemyType;
    }

    public String getEnemyHealthStatus() {
        if (currentHealth == 0) {
            return "Dead";
        } else if (currentHealth == maxHealth) {
            return "Full Health";
        } else if (currentHealth / maxHealth > 0.5) {
            return "Slightly Injured";
        } else if ((currentHealth / maxHealth) <= 0.5 && (currentHealth / maxHealth) > 0.2) {
            return "Injured";
        } else if ((currentHealth / maxHealth) <= 0.2) {
            return "Close to Death";
        }

        return "unknown";
    }

    public void subtractFromCurrentHealth(int i) {
        currentHealth -= i;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public void addToCurrentHealth(int i) {
        currentHealth += i;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
