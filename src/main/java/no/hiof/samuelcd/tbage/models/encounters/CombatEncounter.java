package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.tools.CombatTurn;

import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static no.hiof.samuelcd.tbage.GameEngine.scanner;

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
    public String run(GameEngine gameEngine) {
        String word = "";
        int turnNumber = 1;

        gameEngine.printMessage("Starting encounter '" + getName());

        combatEncounterIntroduction(gameEngine);
        printEnemies(gameEngine);

        while (!isDefeated()) {

            word = scanner.nextLine();

            // Remember to handle calls to display inventory, enemy health, item use, ability use etc.
            if (word.equalsIgnoreCase("exit")) {
                return "exit";
            } else if (word.equalsIgnoreCase("defeated")) {
                gameEngine.printMessage("You haven't defeated this encounter yet!");
            } else if (getNavigationOptions().containsKey(word)) {
                return word;
            } else if (word.equalsIgnoreCase("status")) {
                printEnemies(gameEngine);
            } else if (word.equalsIgnoreCase("attack")) {
                CombatTurn.turn(gameEngine, this, turnNumber++);
            } else if (allEnemiesDead()) {
                setDefeated(true);
            }
        }

        return "defeated";
    }

    private void combatEncounterIntroduction(GameEngine gameEngine) {
        int enemyCount = enemies.size();

        if (enemyCount > 1) {
            gameEngine.printMessage("You face " + enemyCount + " enemies!");
        } else if (enemyCount == 1) {
            gameEngine.printMessage("You face an enemy!");
        }
    }

    private void printEnemies(GameEngine gameEngine) {
        int enemyIteration = 1;
        var player = gameEngine.getPlayer();

        gameEngine.printMessage("Your current health is " + (int)player.getCurrentHealth() + "/" + (int)player.getMaxHealth());
        for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
            var enemy = entry.getValue();
            gameEngine.printMessage("Enemy " + enemyIteration++ + ": " + enemy.getName() + ", Status: " + enemy.getEnemyHealthStatus());
        }
    }

    private boolean allEnemiesDead() {
        for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
            var enemy = entry.getValue();
            if (!enemy.getEnemyHealthStatus().equalsIgnoreCase("dead")) {
                return false;
            }
        }
        return true;
    }

}
