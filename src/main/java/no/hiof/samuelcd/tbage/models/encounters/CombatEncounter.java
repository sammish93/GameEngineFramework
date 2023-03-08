package no.hiof.samuelcd.tbage.models.encounters;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class CombatEncounter extends Encounter {

    private int enemyCount;
    private TreeMap<String, Enemy> enemies;
    private int turnCount = 1;


    private CombatEncounter(String name, double weightedProbability, String imagePath) {
        super(name, weightedProbability, imagePath);
    }

    public CombatEncounter create() {
        return new CombatEncounter(null, 0.5, null);
    }

    public CombatEncounter create(String name) {
        return new CombatEncounter(name, 0.5, null);
    }

    public CombatEncounter create(String name, double weightedProbability) {
        return new CombatEncounter(name, weightedProbability, null);
    }

    public void onTurn() {
        // Behaviour for each turn in the encounter.
    }

    public TreeMap<String, Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(TreeMap<String, Enemy> enemies) {
        this.enemies = enemies;
    }

    public Enemy getEnemyFromEnemies(String enemyName) {
        return enemies.get(enemyName);
    }

    public void addEnemyToEnemies(Enemy enemy) {
        enemies.put(enemy.getName(), enemy);
    }

    public void removeEnemyFromEnemies(Enemy enemy) {
        enemies.remove(enemy.getName());
    }

    public void removeEnemyFromEnemies(String enemyName) {
        enemies.remove(enemyName);
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
        CombatEncounter combatEncounter = om.readValue(file, CombatEncounter.class);

        // Specific deserialisation here.
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
