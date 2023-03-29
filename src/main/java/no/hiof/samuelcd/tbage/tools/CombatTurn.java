package no.hiof.samuelcd.tbage.tools;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.models.encounters.CombatEncounter;
import no.hiof.samuelcd.tbage.models.encounters.Encounter;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.models.player.Player;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import static no.hiof.samuelcd.tbage.GameEngine.scanner;

public class CombatTurn {
    // This class is used to determine behaviour for a single combat turn.
    private Player player;
    private Encounter encounter;
    private GameEngine gameEngine;

    public CombatTurn(GameEngine gameEngine, Encounter encounter) {
        this.gameEngine = gameEngine;
        this.player = gameEngine.getPlayer();
        this.encounter = encounter;
    }

    public static void turn(GameEngine gameEngine, Encounter encounter, int turnNumber) {
        var player = gameEngine.getPlayer();
        boolean isEnemyChosen = false;
        Enemy enemyChosen = null;
        int enemyCount = ((CombatEncounter)encounter).getEnemies().size();

        String output = "";
        var enemiesWithIndex = getEnemiesWithIndex(encounter);

        gameEngine.printMessage("Turn " + turnNumber);
        gameEngine.printMessage("Choose a target to attack:");
        for (Map.Entry<Integer, String> entry : enemiesWithIndex.entrySet()) {
            gameEngine.printMessage("\t" + entry.getKey() + ". " + entry.getValue());
        }

        while (!isEnemyChosen) {
            output = scanner.nextLine();
            int outputInt = 0;

            try {
                outputInt = Integer.parseInt(output);
            } catch (Exception ex) {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + enemyCount + ".");
                output = scanner.nextLine();
            }

            if (enemiesWithIndex.containsKey(outputInt)) {
                var enemyName = enemiesWithIndex.get(outputInt);
                enemyChosen = ((CombatEncounter) encounter).getEnemyFromEnemies(enemyName);
                isEnemyChosen = true;
            } else {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + enemyCount + ".");
            }
        }

        playerTurn(gameEngine, player, enemyChosen);

        enemyTurn(gameEngine, (CombatEncounter) encounter, player);
    }

    private static void enemyTurn(GameEngine gameEngine, CombatEncounter encounter, Player player) {
        for (Map.Entry<String, Enemy> entry : encounter.getEnemies().entrySet()) {
            var enemy = entry.getValue();
            if (!enemy.getEnemyHealthStatus().equalsIgnoreCase("dead")) {
                int enemyDamage = damageCalculator((int)enemy.getMinDamage(), (int)enemy.getMaxDamage());
                player.subtractFromCurrentHealth(enemyDamage);
                gameEngine.printMessage(enemy.getName() + " did " + enemyDamage + " damage to you.");
                gameEngine.printMessage("Your current health is " + (int) player.getCurrentHealth() + "/" + (int) player.getMaxHealth());
            }
        }
    }

    private static void playerTurn(GameEngine gameEngine, Player player, Enemy enemyChosen) {
        int playerDamage = damageCalculator((int) player.getMinDamage(), (int) player.getMaxDamage());
        enemyChosen.subtractFromCurrentHealth(playerDamage);

        gameEngine.printMessage("You did " + playerDamage + " damage to " + enemyChosen.getName() + ".");
        if (enemyChosen.getEnemyHealthStatus().equalsIgnoreCase("dead")) {
            gameEngine.printMessage(enemyChosen.getName() + " has died!");
        }
    }

    private static TreeMap<Integer, String> getEnemiesWithIndex(Encounter encounter) {
        int enemyIteration = 1;
        TreeMap<Integer, String> enemiesWithIndex = new TreeMap<>();

        for (Map.Entry<String, Enemy> entry : ((CombatEncounter)encounter).getEnemies().entrySet()) {
            var enemy = entry.getValue();
            if (!enemy.getEnemyHealthStatus().equalsIgnoreCase("dead")) {
                enemiesWithIndex.put(enemyIteration++, enemy.getName());
            }
        }

        return enemiesWithIndex;
    }

    private static int damageCalculator(int minDamage, int maxDamage) {

        var random = new Random();

        return random.nextInt(maxDamage - minDamage) + minDamage;
    }
}
