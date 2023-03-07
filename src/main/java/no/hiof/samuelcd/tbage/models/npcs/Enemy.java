package no.hiof.samuelcd.tbage.models.npcs;


import no.hiof.samuelcd.tbage.models.abilities.Ability;
import java.util.TreeMap;

public class Enemy extends NonPlayableCharacter {

    private int health;
    private int[] damage = new int[2];


    private Enemy(String name, int health, int minDamage, int maxDamage, TreeMap<String, Ability> abilities) {
        super(name, abilities);

        this.health = health;
        this.damage[0] = minDamage;
        this.damage[1] = maxDamage;
    }

    public static Enemy create() {
        return new Enemy(null, 0, 0, 0, null);
    }

    public static Enemy create(String name, int health, int minDamage, int maxDamage, TreeMap<String, Ability> abilities) {
        return new Enemy(name, health, minDamage, maxDamage, abilities);
    }

    @Override
    public void processAbilities() {
        // Iterates through abilityPool to find which events (onEncounterStart and onEncounterFinish) are TRUE.
    }

    @Override
    public void processItems() {
        // Determines which items have dropped from a defeated enemy.
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMinDamage() {
        return damage[0];
    }

    public void setMinDamage(int minDamage) {
        this.damage[0] = minDamage;
    }

    public int getMaxDamage() {
        return damage[1];
    }

    public void setMaxDamage(int maxDamage) {
        this.damage[1] = maxDamage;
    }
}
