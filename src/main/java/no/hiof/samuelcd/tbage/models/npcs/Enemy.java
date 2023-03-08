package no.hiof.samuelcd.tbage.models.npcs;


import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class Enemy extends NonPlayableCharacter {

    private int maxHealth;
    private int currentHealth;
    private int[] damage = new int[2];
    private String enemyType;


    private Enemy(String name, int maxHealth, int minDamage, int maxDamage, TreeMap<String, Ability> abilities, String enemyType) {
        super(name, abilities);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage[0] = minDamage;
        this.damage[1] = maxDamage;
        this.enemyType = enemyType;
    }

    public static Enemy create() {
        return new Enemy(null, 0, 0, 0, null, null);
    }

    public static Enemy create(String name, int health, int minDamage, int maxDamage, TreeMap<String, Ability> abilities, String enemyType) {
        return new Enemy(name, health, minDamage, maxDamage, abilities, enemyType);
    }

    @Override
    public void processAbilities() {
        // Iterates through abilityPool to find which events (onEncounterStart and onEncounterFinish) are TRUE.
    }

    @Override
    public void processItems() {
        // Determines which items have dropped from a defeated enemy.
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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

    public int getCurrentHealth() {
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

    @Override
    public void writeToJson(File file) throws IOException {
        ObjectMapper om = new ObjectMapper();
        boolean fileExists = file.exists();

        if (!fileExists) {
            fileExists = file.createNewFile();
        }

        if (fileExists) {
            om.writeValue(file, this);
        }

        // Jackson auto-closes the stream and mapper.
    }

    @Override
    public void readFromJson(File file) throws IOException{
        ObjectMapper om = new ObjectMapper();
        Enemy enemy = om.readValue(file, Enemy.class);

        // Specific deserialisation here.
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
