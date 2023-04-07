package no.hiof.samuelcd.tbage.models.npcs;


import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

public class Enemy extends NonPlayableCharacter {

    private double maxHealth;
    private double currentHealth;
    private double[] damage = new double[2];
    private String enemyType;
    private boolean isMelee;
    private double meleeChancePerTurn;


    private Enemy(String name, int maxHealth, int minDamage, int maxDamage, TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType, boolean isMelee, double meleeChancePerTurn) {
        super(name, abilities, items);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage[0] = minDamage;
        this.damage[1] = maxDamage;
        this.enemyType = enemyType;
        this.isMelee = isMelee;
        this.meleeChancePerTurn = meleeChancePerTurn;
    }

    public static Enemy create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Enemy(randomlyGeneratedId.toString(), 10, 1, 2, null, null, null, true, 1);
    }

    public static Enemy create(String name) {
        return new Enemy(name, 10, 1, 3, null, null, null, true, 1);
    }

    public static Enemy create(String name, int health, int minDamage, int maxDamage, TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType, boolean isMelee, double meleeChancePerTurn) {
        return new Enemy(name, health, minDamage, maxDamage, abilities, items, enemyType, isMelee, meleeChancePerTurn);
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

    public boolean isMelee() {
        return isMelee;
    }

    public void setMelee(boolean melee) {
        isMelee = melee;
    }

    public double getMeleeChancePerTurn() {
        return meleeChancePerTurn;
    }

    public boolean isMeleeAttackThisTurn() {
        var random = new Random();

        int randomChance = 100 - ((int) (meleeChancePerTurn * 100));
        if (randomChance != 0) {
            int randomInt = random.nextInt(100 - ((int) meleeChancePerTurn * 100)) + ((int) meleeChancePerTurn * 100);

            if (randomInt < ((int) (meleeChancePerTurn * 100))) {
                return false;
            }
        }

        return true;
    }

    public void setMeleeChancePerTurn(double meleeChancePerTurn) {
        this.meleeChancePerTurn = meleeChancePerTurn;
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
