package sammish93.tbage.models;


import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.tools.EncounterController;

import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

/**
 * A class intended to be used in a CombatEncounter. Enemies can take part in combat with the player, cast
 * abilities, and drop currency and loot on being defeated.
 */
public class Enemy extends NonPlayableCharacter {

    private double maxHealth;
    private double currentHealth;
    private double[] damage = new double[2];
    private double currencyReceivedOnDeath;
    private String enemyType;
    private boolean isMelee;
    private double meleeChancePerTurn;


    private Enemy(String name, int maxHealth, int minDamage, int maxDamage, double currencyReceivedOnDeath,
                  TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType,
                  double meleeChancePerTurn) throws InvalidValueException {
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

        if (meleeChancePerTurn > 0 && meleeChancePerTurn <= 1) {
            this.meleeChancePerTurn = meleeChancePerTurn;
            this.isMelee = true;
        } else if (meleeChancePerTurn == 0) {
            this.isMelee = false;
        } else {
            throw new InvalidValueException("Value " + meleeChancePerTurn + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0 and less than or equal to 1");
        }
    }


    /**
     *
     * @return Returns an initialised Enemy object with a default UUID value as a name, maximum and current
     * health as 10, minimum damage as 1, maximum damage as 2, currency received on death as 0, and a melee
     * chance of 100%.
     * @throws InvalidValueException Thrown when a non-positive value is set for maximum health, or a negative
     * value is set for minimum damage, maximum damage, or currency received on death, or a decimal value less
     * than 0 or greater than 1 is set for melee chance per turn.
     */
    public static Enemy create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Enemy(randomlyGeneratedId.toString(), 10, 1, 2,
                0.0, null, null, null, 1);
    }

    /**
     *
     * @param name A string that is used as an identifier for this enemy.
     * @return Returns an initialised Enemy object with a default maximum and current
     * health as 10, minimum damage as 1, maximum damage as 2, currency received on death as 0, and a melee
     * chance of 100%.
     * @throws InvalidValueException Thrown when a non-positive value is set for maximum health, or a negative
     * value is set for minimum damage, maximum damage, or currency received on death, or a decimal value less
     * than 0 or greater than 1 is set for melee chance per turn.
     */
    public static Enemy create(String name) throws InvalidValueException {
        return new Enemy(name, 10, 1, 3, 0.0, null,
                null, null, 1);
    }

    /**
     *
     * @param name A string representing the name of an Enemy object.
     * @param health A positive integer value representing the maximum and current health of an enemy.
     * @param minDamage A non-negative integer value that determines the minimum damage that an enemy can inflict
     *                  upon attacking a Player.
     * @param maxDamage A non-negative integer value that determines the maximum damage that an enemy can inflict
     *                  upon attacking a Player.
     * @param goldReceivedOnDeath A non-negative decimal that determines how much gold an Enemy drops upon being
     *                            defeated.
     * @param abilities An existing TreeMap representing the abilities that an Enemy can use during combat.
     * @param items An existing TreeMap representing the items that an Enemy can drop upon being defeated. Note
     *              that these items aren't guaranteed to drop.
     * @param enemyType A string value that has no pre-programmed use in the framework, but can be used to group
     *                  enemies, and optionally be used within the generic Useable interface.
     *                  Example:
     *                  Enemies with an enemyType of 'undead' might take more damage from an item's onUse() method.
     * @param meleeChancePerTurn A decimal value greater than or equal to 0, or less than or equal to 1. An enemy
     *                           that has a value of 1 is guaranteed to use a melee attack each turn, while an
     *                           enemy with a value of 0.5 and existing abilities in their ability pool has a 50%
     *                           chance of attacking that turn, or a 50% chance of using an ability.
     * @return Returns an initialised Enemy object.
     * @throws InvalidValueException Thrown when a non-positive value is set for maximum health, or a negative
     * value is set for minimum damage, maximum damage, or currency received on death, or a decimal value less
     * than 0 or greater than 1 is set for melee chance per turn.
     */
    public static Enemy create(String name, int health, int minDamage, int maxDamage, double goldReceivedOnDeath,
                               TreeMap<String, Ability> abilities, TreeMap<String, Item> items, String enemyType,
                               boolean isMelee, double meleeChancePerTurn) throws InvalidValueException {
        return new Enemy(name, health, minDamage, maxDamage, goldReceivedOnDeath, abilities, items, enemyType,
                meleeChancePerTurn);
    }


    /**
     *
     * @return Returns a decimal value representing the maximum health of an enemy.
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the maximum health of an enemy.
     * @param maxHealth A positive integer value.
     * @throws InvalidValueException Thrown when a value less than or equal to 0 is provided.
     */
    public void setMaxHealth(int maxHealth) throws InvalidValueException {
        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + maxHealth + " is invalid. Enter a decimal " +
                    "value greater than 0");
        }
    }

    /**
     *
     * @return Returns a decimal value representing an enemy's current health.
     */
    public double getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current health of an enemy.
     * @param currentHealth A positive integer value.
     * @throws InvalidValueException Thrown when a value less than or equal to 0 is provided.
     */
    public void setCurrentHealth(int currentHealth) throws InvalidValueException {
        if (maxHealth >= 0) {
            this.currentHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + currentHealth + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    /**
     *
     * @return Returns a decimal value representing the minimum amount of damage an enemy can inflict to a
     * player during combat.
     */
    public double getMinDamage() {
        return damage[0];
    }

    /**
     * Sets the minimum amount of damage an enemy can inflict to a player during combat.
     * @param minDamage An integer value greater than or equal to 0.
     */
    public void setMinDamage(int minDamage) throws InvalidValueException {
        if (minDamage >= 0) {
            damage[0] = minDamage;
        } else {
            throw new InvalidValueException("Value " + minDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    /**
     *
     * @return Returns a decimal value representing the maximum amount of damage an enemy can inflict to a
     * player during combat.
     */
    public double getMaxDamage() {
        return damage[1];
    }

    /**
     * Sets the maximum amount of damage an enemy can inflict to a player during combat.
     * @param maxDamage An integer value greater than or equal to 0.
     */
    public void setMaxDamage(int maxDamage) throws InvalidValueException {
        if (maxDamage >= 0) {
            damage[1] = maxDamage;
        } else {
            throw new InvalidValueException("Value " + maxDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    /**
     *
     * @return Returns a non-negative decimal representing how much currency the enemy drops on defeat.
     */
    public double getCurrencyReceivedOnDeath() {
        return currencyReceivedOnDeath;
    }

    /**
     *
     * @param currencyReceivedOnDeath Sets a non-negative decimal representing how much currency the enemy
     *                                drops on defeat.
     * @throws InvalidValueException Thrown if a negative value is provided.
     */
    public void setCurrencyReceivedOnDeath(double currencyReceivedOnDeath) throws InvalidValueException {
        if (currencyReceivedOnDeath >= 0) {
            this.currencyReceivedOnDeath = currencyReceivedOnDeath;
        } else {
            throw new InvalidValueException("Value " + currencyReceivedOnDeath + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    /**
     *
     * @return  Returns a string value representing the type of enemy.
     */
    public String getEnemyType() {
        return enemyType;
    }

    /**
     *
     * @param enemyType Sets a string value representing the type of enemy that has no pre-programmed use in the
     *                  framework, but can be used to group enemies, and optionally be used within the generic
     *                  Useable interface.
     *                  Example:
     *                  Enemies with an enemyType of 'undead' might take more damage from an item's onUse() method.
     *
     */
    public void setEnemyType(String enemyType) {
        this.enemyType = enemyType;
    }

    /**
     *
     * @return Returns true if the enemy has a chance to attack via melee per turn.
     */
    public boolean isMelee() {
        return isMelee;
    }

    /**
     *
     * @param melee Sets a boolean value to determine if an enemy will attack via melee each turn.
     */
    public void setMelee(boolean melee) {
        isMelee = melee;
    }

    /**
     *
     * @return Returns a decimal value representing the chance a melee attack will be chosen per turn.
     */
    public double getMeleeChancePerTurn() {
        return meleeChancePerTurn;
    }

    /**
     *
     * @param meleeChancePerTurn Sets the probability that a melee attack will be chosen per turn. If the value is
     *                           1 then an enemy is guaranteed to attack via melee. If it is 0.5 then there's a
     *                           50% chance it will attack via melee, providing there exists abilities in the ability
     *                           pool. If it is a positive value less than or equal to 1 and there are no abilities
     *                           in the ability pool then the enemy is guaranteed to attack via melee.
     * @throws InvalidValueException Thrown in the event that a value is provided that is less than 0 or greater
     * than 1.
     */
    public void setMeleeChancePerTurn(double meleeChancePerTurn) throws InvalidValueException {
        if (meleeChancePerTurn > 0 && meleeChancePerTurn <= 1) {
            this.meleeChancePerTurn = meleeChancePerTurn;
            this.isMelee = true;
        } else if (meleeChancePerTurn == 0) {
            this.isMelee = false;
        } else {
            throw new InvalidValueException("Value " + meleeChancePerTurn + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0 and less than or equal to 1");
        }
    }

    /**
     * This method is intended to be used by the EncounterController class during runtime.
     * @return Returns whether an enemy will attack via melee on a specific turn.
     * @see EncounterController
     */
    public boolean isMeleeAttackThisTurn() {
        var random = new Random();

        int randomChance = 100 - ((int) (meleeChancePerTurn * 100));
        if (randomChance != 0) {
            int randomInt = random.nextInt(100 - ((int) meleeChancePerTurn * 100)) +
                    ((int) meleeChancePerTurn * 100);

            if (randomInt < ((int) (meleeChancePerTurn * 100))) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method is intended to return non-exact representations of an enemy's current health.
     * @return Returns a string that vaguely describes the health status of an enemy. An enemy progresses from
     * 'Fully Health' to 'Slightly Injured' to 'Injured' to 'Close to Death' to 'Dead' depending on their
     * current health compared to max health.
     */
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

    /**
     * A method intended to reduce the amount of code a developer has to write when subtracting health from an
     * enemys's current health total.
     * @param i A positive integer value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
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

    /**
     * A method intended to reduce the amount of code a developer has to write when adding health to an
     * enemys's current health total.
     * @param i A positive integer value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
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

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
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
