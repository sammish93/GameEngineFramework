package no.hiof.samuelcd.tbage.models.npcs;


import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

public class Enemy extends NonPlayableCharacter {

    private double maxHealth;
    private double currentHealth;
    private double[] damage = new double[2];
    private double currencyReceivedOnDeath;
    private String enemyType;
    private boolean isMelee;
    private double meleeChancePerTurn;


    private Enemy(String name, int maxHealth, int minDamage, int maxDamage, double currencyReceivedOnDeath, TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType, boolean isMelee, double meleeChancePerTurn) throws InvalidValueException {
        super(name, abilities, items);

        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
            this.currentHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + maxHealth + " is invalid. Enter a decimal " +
                    "value greater than 0");
        }

        if (minDamage >= 0) {
            damage[0] = minDamage;
        } else {
            throw new InvalidValueException("Value " + minDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }

        if (maxDamage >= 0) {
            damage[1] = maxDamage;
        } else {
            throw new InvalidValueException("Value " + maxDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }

        if (currencyReceivedOnDeath >= 0) {
            this.currencyReceivedOnDeath = currencyReceivedOnDeath;
        } else {
            throw new InvalidValueException("Value " + currencyReceivedOnDeath + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }

        this.enemyType = enemyType;
        this.isMelee = isMelee;

        if (meleeChancePerTurn > 0 && meleeChancePerTurn <= 1) {
            this.meleeChancePerTurn = meleeChancePerTurn;
        } else if (meleeChancePerTurn == 0) {
            this.isMelee = false;
        } else {
            throw new InvalidValueException("Value " + meleeChancePerTurn + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0 and less than or equal to 1");
        }
    }

    public static Enemy create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Enemy(randomlyGeneratedId.toString(), 10, 1, 2, 0.0, null, null, null, true, 1);
    }

    public static Enemy create(String name) throws InvalidValueException {
        return new Enemy(name, 10, 1, 3, 0.0, null, null, null, true, 1);
    }

    public static Enemy create(String name, int health, int minDamage, int maxDamage, double goldReceivedOnDeath, TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType, boolean isMelee, double meleeChancePerTurn) throws InvalidValueException {
        return new Enemy(name, health, minDamage, maxDamage, goldReceivedOnDeath, abilities, items, enemyType, isMelee, meleeChancePerTurn);
    }


    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) throws InvalidValueException {
        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + maxHealth + " is invalid. Enter a decimal " +
                    "value greater than 0");
        }
    }

    public double getMinDamage() {
        return damage[0];
    }

    public void setMinDamage(int minDamage) throws InvalidValueException {
        if (minDamage >= 0) {
            damage[0] = minDamage;
        } else {
            throw new InvalidValueException("Value " + minDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    public double getMaxDamage() {
        return damage[1];
    }

    public void setMaxDamage(int maxDamage) throws InvalidValueException {
        if (maxDamage >= 0) {
            damage[1] = maxDamage;
        } else {
            throw new InvalidValueException("Value " + maxDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) throws InvalidValueException {
        if (maxHealth >= 0) {
            this.currentHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + currentHealth + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    public double getCurrencyReceivedOnDeath() {
        return currencyReceivedOnDeath;
    }

    public void setCurrencyReceivedOnDeath(double currencyReceivedOnDeath) throws InvalidValueException {
        if (currencyReceivedOnDeath >= 0) {
            this.currencyReceivedOnDeath = currencyReceivedOnDeath;
        } else {
            throw new InvalidValueException("Value " + currencyReceivedOnDeath + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
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

    public void setMeleeChancePerTurn(double meleeChancePerTurn) throws InvalidValueException {
        if (meleeChancePerTurn > 0 && meleeChancePerTurn <= 1) {
            this.meleeChancePerTurn = meleeChancePerTurn;
        } else if (meleeChancePerTurn == 0) {
            this.isMelee = false;
        } else {
            throw new InvalidValueException("Value " + meleeChancePerTurn + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0 and less than or equal to 1");
        }
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

        return "Unknown";
    }

    public void subtractFromCurrentHealth(int i) throws InvalidValueException {
        if (i >= 0) {
            currentHealth -= i;
        } else {
            throw new InvalidValueException("Value " + i + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }

        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public void addToCurrentHealth(int i) throws InvalidValueException {
        if (i >= 0) {
            currentHealth += i;
        } else {
            throw new InvalidValueException("Value " + i + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public String toString() {
        double i = meleeChancePerTurn * 100;

        StringBuilder sb = new StringBuilder();

        sb.append("Enemy Name: '" + getName() + "', " +
                "Enemy Type: " + enemyType + ", " +
                "Is Melee: " + isMelee + ", " +
                "Melee Chance Per Turn: " + (int) i + "%, " +
                "Currency Dropped On Death: " + (int) currencyReceivedOnDeath + ", " +
                "Current Health: " + (int) currentHealth + ", " +
                "Maximum Health: " + (int) maxHealth + ", " +
                "Minimum Damage: " + (int) damage[0] + ", " +
                "Maximum Damage: " + (int) damage[1]);

        printItemTableAndAbilityPool(sb);

        return sb.toString();
    }
}
