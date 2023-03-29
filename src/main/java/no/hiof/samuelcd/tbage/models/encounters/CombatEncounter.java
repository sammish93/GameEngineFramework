package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;

import java.util.Scanner;
import java.util.TreeMap;

public class CombatEncounter extends Encounter {

    private int enemyCount;
    private TreeMap<String, Enemy> enemies;
    private int turnCount = 1;


    private CombatEncounter(String name, double weightedProbability, String imagePath, TreeMap<String, Feat> featChecks, TreeMap<String, Feat> featRewards, TreeMap<String, String> navigationOptions) {
        super(name, weightedProbability, imagePath, featChecks, featRewards, navigationOptions);

        enemies = new TreeMap<>();
    }

    public static CombatEncounter create() {
        return new CombatEncounter(null, 0.5, null, null, null, null);
    }

    public static CombatEncounter create(String name) {
        return new CombatEncounter(name, 0.5, null, null, null, null);
    }

    public static CombatEncounter create(String name, double weightedProbability) {
        return new CombatEncounter(name, weightedProbability, null, null, null, null);
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
    public String toString() {
        return super.toString();
    }

    @Override
    public String run() {
        System.out.println("Starting encounter '" + getName() + "'.");
        Scanner scanner = new Scanner(System.in);

        String word = scanner.nextLine();
        if (word.equalsIgnoreCase("exit")) {
            return word;
        } else {
            return "defeated";
        }
    }

}
