package no.hiof.samuelcd.tbage.tools;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.encounters.CombatEncounter;
import no.hiof.samuelcd.tbage.models.encounters.Encounter;
import no.hiof.samuelcd.tbage.models.encounters.NonCombatEncounter;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.npcs.Ally;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.models.npcs.NonPlayableCharacter;
import no.hiof.samuelcd.tbage.models.player.Player;
import no.hiof.samuelcd.tbage.models.props.Prop;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static no.hiof.samuelcd.tbage.GameEngine.scanner;

public class EncounterController {
    // This class is used to determine behaviour for a single combat turn.
    private Player player;
    private Encounter encounter;
    private GameEngine gameEngine;

    public EncounterController(GameEngine gameEngine, Encounter encounter) {
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

            if (output.equalsIgnoreCase("back")) {
                gameEngine.printMessage("You are no longer attacking.");
                break;
            }

            try {
                outputInt = Integer.parseInt(output);
            } catch (Exception ex) {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + enemyCount + ".");
            }

            if (enemiesWithIndex.containsKey(outputInt)) {
                var enemyName = enemiesWithIndex.get(outputInt);
                enemyChosen = ((CombatEncounter) encounter).getEnemyFromEnemies(enemyName);
                isEnemyChosen = true;
            } else {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + enemyCount + ".");
            }
        }

        if (isEnemyChosen) {
            playerTurn(gameEngine, player, enemyChosen);

            String answer;

            while(true) {
                gameEngine.printMessage("Would you like to use an item?");

                answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                    EncounterController.useItem(gameEngine, encounter);
                } else if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
                    break;
                } else {
                    gameEngine.printMessage("I'm sorry, I didn't understand.");
                }
            }

            enemyTurn(gameEngine, (CombatEncounter) encounter, player);
            gameEngine.printMessage("Your current health is " + (int) player.getCurrentHealth() + "/" + (int) player.getMaxHealth());
        }
    }

    public static void useItem(GameEngine gameEngine, Encounter encounter) {
        var player = gameEngine.getPlayer();
        boolean isItemChosen = false;
        Item itemChosen = null;
        int itemCount = player.getInventory().size();

        String output = "";
        var itemsWithIndex = getInventoryItemsWithIndex(gameEngine);

        gameEngine.printMessage("Choose an item to use:");
        for (Map.Entry<Integer, String> entry : itemsWithIndex.entrySet()) {
            gameEngine.printMessage("\t" + entry.getKey() + ". " + entry.getValue());
        }

        while (!isItemChosen) {
            output = scanner.nextLine();
            int outputInt = 0;

            if (output.equalsIgnoreCase("back")) {
                gameEngine.printMessage("You are no longer about to use an item.");
                break;
            }

            try {
                outputInt = Integer.parseInt(output);
            } catch (Exception ex) {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + itemCount + ".");
                output = scanner.nextLine();
            }

            if (itemsWithIndex.containsKey(outputInt)) {
                var itemName = itemsWithIndex.get(outputInt);
                itemChosen = player.getItemFromInventory(itemName);
                isItemChosen = true;
            } else {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + itemCount + ".");
            }
        }

        if (isItemChosen) {
            int numberOfUsesRemaining = itemChosen.getNumberOfUses();
            itemChosen.onUse(gameEngine);

            if (numberOfUsesRemaining != 0) {
                itemChosen.setNumberOfUses(numberOfUsesRemaining-- - 1);
                if (itemChosen.getNumberOfUses() != 0) {
                    gameEngine.printMessage("You now have " + numberOfUsesRemaining + " use(s) remaining of the item '" + itemChosen.getName() + "'");
                } else {
                    gameEngine.printMessage("You have depleted " + itemChosen.getName() + "'s uses.");
                    gameEngine.getPlayer().removeItemFromInventory(itemChosen);
                }

            }
        }
    }

    public static NonPlayableCharacter chooseNpc(GameEngine gameEngine, Encounter encounter) {
        boolean isTargetChosen = false;
        NonPlayableCharacter targetChosen = null;
        String output = "";

        if (encounter instanceof CombatEncounter) {
            int enemyCount = ((CombatEncounter)encounter).getEnemies().size();
            var enemiesWithIndex = getEnemiesWithIndex(encounter);

            if (enemyCount == 0) {
                gameEngine.printMessage("There are currently no targets to use the item on.");
                return null;
            } else {
                gameEngine.printMessage("Choose a target to use this item on:");

                for (Map.Entry<Integer, String> entry : enemiesWithIndex.entrySet()) {
                    gameEngine.printMessage("\t" + entry.getKey() + ". " + entry.getValue());
                }

                while (!isTargetChosen) {
                    output = scanner.nextLine();
                    int outputInt = 0;

                    if (output.equalsIgnoreCase("back")) {
                        gameEngine.printMessage("You are no longer about to use this item on an enemy.");
                        break;
                    }

                    try {
                        outputInt = Integer.parseInt(output);
                    } catch (Exception ex) {
                        gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + enemyCount + ".");
                        output = scanner.nextLine();
                    }

                    if (enemiesWithIndex.containsKey(outputInt)) {
                        var enemyName = enemiesWithIndex.get(outputInt);
                        targetChosen = ((CombatEncounter) encounter).getEnemyFromEnemies(enemyName);
                        isTargetChosen = true;
                    } else {
                        gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + enemyCount + ".");
                    }
                }
            }

            if (isTargetChosen) {
                return targetChosen;
            }
        } else if (encounter instanceof NonCombatEncounter) {
            int allyCount = ((NonCombatEncounter)encounter).getAllies().size();
            var alliesWithIndex = getAlliesWithIndex(encounter);

            for (Map.Entry<Integer, String> entry : alliesWithIndex.entrySet()) {
                gameEngine.printMessage("\t" + entry.getKey() + ". " + entry.getValue());
            }

            while (!isTargetChosen) {
                output = scanner.nextLine();
                int outputInt = 0;

                if (output.equalsIgnoreCase("back")) {
                    gameEngine.printMessage("You are no longer about to interact.");
                    break;
                }

                try {
                    outputInt = Integer.parseInt(output);
                } catch (Exception ex) {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + allyCount + ".");
                    output = scanner.nextLine();
                }

                if (alliesWithIndex.containsKey(outputInt)) {
                    var allyName = alliesWithIndex.get(outputInt);
                    targetChosen = ((NonCombatEncounter) encounter).getAllyFromAllies(allyName);
                    isTargetChosen = true;
                } else {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + allyCount + ".");
                }
            }

            if (isTargetChosen) {
                return targetChosen;
            }
        }

        return null;
    }

    public static Prop chooseProp(GameEngine gameEngine, Encounter encounter) {
        boolean isTargetChosen = false;
        Prop targetChosen = null;
        String output = "";

        if (encounter instanceof NonCombatEncounter) {
            int propCount = ((NonCombatEncounter)encounter).getProps().size();
            var propsWithIndex = getPropsWithIndex(encounter);

            if (propCount == 0) {
                gameEngine.printMessage("There are currently no objects to interact with.");
                return null;
            } else {
                gameEngine.printMessage("Choose an object to interact with:");

                for (Map.Entry<Integer, String> entry : propsWithIndex.entrySet()) {
                    gameEngine.printMessage("\t" + entry.getKey() + ". " + entry.getValue());
                }

                while (!isTargetChosen) {
                    output = scanner.nextLine();
                    int outputInt = 0;

                    if (output.equalsIgnoreCase("back")) {
                        gameEngine.printMessage("You are no longer about to interact with an object.");
                        break;
                    }

                    try {
                        outputInt = Integer.parseInt(output);
                    } catch (Exception ex) {
                        gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + propCount + ".");
                        output = scanner.nextLine();
                    }

                    if (propsWithIndex.containsKey(outputInt)) {
                        var propName = propsWithIndex.get(outputInt);
                        targetChosen = ((NonCombatEncounter) encounter).getPropFromProps(propName);
                        isTargetChosen = true;
                    } else {
                        gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + propCount + ".");
                    }
                }
            }

            if (isTargetChosen) {
                return targetChosen;
            }
        }

        return null;
    }

    public static Item chooseItem(GameEngine gameEngine, Ally ally) {
        boolean isTargetChosen = false;
        Item targetChosen = null;
        String output = "";

        var itemsWithIndex = getAllyItemsWithIndex(ally);
        int itemCount = itemsWithIndex.size();

        if (itemCount == 0) {
            gameEngine.printMessage("There are currently no items to purchase.");
            return null;
        } else {
            gameEngine.printMessage("Choose an item to purchase (" + (int)gameEngine.getPlayer().getCurrencyAmount()
                    + " gold):");

            for (Map.Entry<Integer, String> entry : itemsWithIndex.entrySet()) {
                Item itemFromTable = ally.getItemFromItemTable(entry.getValue());
                gameEngine.printMessageFormatted("\t%s %-12s %s\n", entry.getKey() + ".", "Gold: " + itemFromTable.getValue(), entry.getValue());
            }

            while (!isTargetChosen) {
                output = scanner.nextLine();
                int outputInt = 0;

                if (output.equalsIgnoreCase("back")) {
                    gameEngine.printMessage("You are no longer trading.");
                    break;
                }

                try {
                    outputInt = Integer.parseInt(output);
                } catch (Exception ex) {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + itemCount + ".");
                    output = scanner.nextLine();
                }

                if (itemsWithIndex.containsKey(outputInt)) {
                    var itemName = itemsWithIndex.get(outputInt);
                    targetChosen = ally.getItemFromItemTable(itemName);

                    double currentGold = gameEngine.getPlayer().getCurrencyAmount();
                    int itemValue = targetChosen.getValue();
                    if (itemValue > currentGold) {
                        gameEngine.printMessage("You do not have enough gold to purchase this item.");
                    } else {
                        gameEngine.printMessage("Would you like to purchase this item for " + targetChosen.getValue() + " gold?");

                        String answer;

                        answer = scanner.nextLine();

                        if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                            isTargetChosen = true;
                        }
                    }
                } else {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number from 1 to " + itemCount + ".");
                }
            }

            if (isTargetChosen) {
                return targetChosen;
            }
        }

        return null;
    }

    private static void enemyTurn(GameEngine gameEngine, CombatEncounter encounter, Player player) {
        for (Map.Entry<String, Enemy> entry : encounter.getEnemies().entrySet()) {
            var enemy = entry.getValue();

            if (!enemy.getEnemyHealthStatus().equalsIgnoreCase("dead")) {

                Ability ability = null;

                if (!enemy.getNpcAbilityPool().isEmpty()) {
                    ProbabilityCalculator<String> wpc = new ProbabilityCalculator<>();

                    for (Map.Entry<String, Ability> abilityEntry : enemy.getNpcAbilityPool().entrySet()) {
                        ability = abilityEntry.getValue();
                        wpc.add(ability.getAbilityProbabilityPerTurn(), ability.getName());
                    }

                    ability = enemy.getAbilityFromAbilityPool(wpc.next());
                }
                if (enemy.isMelee()) {
                    if (enemy.isMeleeAttackThisTurn()) {
                        int enemyDamage = damageCalculator((int)enemy.getMinDamage(), (int)enemy.getMaxDamage());
                        player.subtractFromCurrentHealth(enemyDamage);
                        gameEngine.printMessage(enemy.getName() + " did " + enemyDamage + " damage to you.");

                    } else if (ability != null && ability.getOnUseBehaviour() != null) {
                        ability.onUse(gameEngine);
                    }
                } else if (ability != null && ability.getOnUseBehaviour() != null) {
                    ability.onUse(gameEngine);
                } else {
                    gameEngine.printMessage(enemy.getName() + " does nothing this turn.");
                }
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

    private static TreeMap<Integer, String> getAlliesWithIndex(Encounter encounter) {
        int allyIteration = 1;
        TreeMap<Integer, String> alliesWithIndex = new TreeMap<>();

        for (Map.Entry<String, Ally> entry : ((NonCombatEncounter)encounter).getAllies().entrySet()) {
            var ally = entry.getValue();

            alliesWithIndex.put(allyIteration++, ally.getName());

        }

        return alliesWithIndex;
    }

    private static TreeMap<Integer, String> getPropsWithIndex(Encounter encounter) {
        int propIteration = 1;
        TreeMap<Integer, String> propsWithIndex = new TreeMap<>();

        for (Map.Entry<String, Prop> entry : ((NonCombatEncounter)encounter).getProps().entrySet()) {
            var prop = entry.getValue();
            if (!prop.isUsed()) {
                propsWithIndex.put(propIteration++, prop.getName());
            }
        }

        return propsWithIndex;
    }

    private static TreeMap<Integer, String> getInventoryItemsWithIndex(GameEngine gameEngine) {
        int itemIteration = 1;
        TreeMap<Integer, String> itemsWithIndex = new TreeMap<>();

        for (Map.Entry<String, Item> entry : gameEngine.getPlayer().getInventory().entrySet()) {
            var item = entry.getValue();
            if (item.getOnUseBehaviour() != null) {
                itemsWithIndex.put(itemIteration++, item.getName());
            }
        }

        return itemsWithIndex;
    }

    private static TreeMap<Integer, String> getAllyItemsWithIndex(Ally ally) {
        int itemIteration = 1;
        TreeMap<Integer, String> itemsWithIndex = new TreeMap<>();

        for (Map.Entry<String, Item> entry : ally.getNpcItemTable().entrySet()) {
            var item = entry.getValue();
            if (item.getValue() != 0) {
                itemsWithIndex.put(itemIteration++, item.getName());
            }
        }

        return itemsWithIndex;
    }

    private static int damageCalculator(int minDamage, int maxDamage) {

        var random = new Random();

        return random.nextInt(maxDamage - minDamage) + minDamage;
    }

    public static void getEncounterDrops(GameEngine gameEngine, Encounter encounter) {

        var random = new Random();

        Player player = gameEngine.getPlayer();

        if (encounter instanceof CombatEncounter) {
            for (Map.Entry<String, Enemy> enemyEntry : ((CombatEncounter) encounter).getEnemies().entrySet()) {
                Enemy enemy = enemyEntry.getValue();

                if (!enemy.getNpcItemTable().isEmpty()) {

                    for (Map.Entry<String, Item> itemEntry : enemy.getNpcItemTable().entrySet()) {
                        Item item = itemEntry.getValue();

                        if ((item.getDropChance() * 100) == 0 ||
                                ProbabilityCalculator.isDropped(random, (int)(item.getDropChance() * 100))) {

                            gameEngine.printMessage("You received " + item.getName() + " from " + enemy.getName());
                            player.addItemToInventory(item);
                        }
                    }
                }
            }
        }
    }
}
