package sammish93.tbage.tools;

import sammish93.tbage.GameEngine;
import sammish93.tbage.enums.GamePlatform;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.interfaces.Useable;
import sammish93.tbage.models.Ability;
import sammish93.tbage.models.CombatEncounter;
import sammish93.tbage.models.Encounter;
import sammish93.tbage.models.NonCombatEncounter;
import sammish93.tbage.models.Feat;
import sammish93.tbage.models.Item;
import sammish93.tbage.models.Ally;
import sammish93.tbage.models.Enemy;
import sammish93.tbage.models.NonPlayableCharacter;
import sammish93.tbage.models.Player;
import sammish93.tbage.models.Prop;

import java.util.Map;
import java.util.TreeMap;

import static sammish93.tbage.GameEngine.scanner;

/**
 * A class intended to be used to control how the framework responds to certain player behaviours, interactions,
 * and input.
 */
public class EncounterController {
    // This class is used to determine behaviour for a single combat turn.
    private Player player;
    private Encounter encounter;
    private GameEngine gameEngine;
    private static String input = "";

    /**
     *
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param encounter Required to determine exactly which Encounter object is to be interacted with.
     */
    public EncounterController(GameEngine gameEngine, Encounter encounter) {
        this.gameEngine = gameEngine;
        this.player = gameEngine.getPlayer();
        this.encounter = encounter;
    }

    /**
     * A method designed to handle how a CombatEncounter behaves, and to simulate turn-based combat.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param encounter Required to determine exactly which Encounter object is to be interacted with.
     * @param turnNumber Required to display the current turn number of the turn-based combat.
     *                   Has no bearing on anything else.
     * @throws InventoryFullException Thrown in the event that an enemy drops an item, but the player cannot
     * receive the item because their inventory is full.
     * @throws InvalidValueException Thrown in the event of an invalid value being provided. See specific methods
     * for more information on what constitutes a valid value.
     * @see CombatEncounter
     */
    public static void turn(GameEngine gameEngine, Encounter encounter, int turnNumber)
            throws InventoryFullException, InvalidValueException, InterruptedException {
        var player = gameEngine.getPlayer();
        boolean isEnemyChosen = false;
        Enemy enemyChosen = null;
        int enemyCount = ((CombatEncounter)encounter).getEnemies().size();


        String inputComparison = input;
        var enemiesWithIndex = getEnemiesWithIndex(encounter);

        gameEngine.printMessage("Turn " + turnNumber);
        gameEngine.printMessage("Choose a target to attack:");
        for (Map.Entry<Integer, String> entry : enemiesWithIndex.entrySet()) {
            gameEngine.printMessage("\t" + entry.getKey() + ". " + entry.getValue());
        }

        while (!isEnemyChosen) {

            if (gameEngine.getPlatform() == GamePlatform.SWING) {
                Thread.sleep(100);
                if (input.equals(inputComparison)) {
                    continue;
                }
            } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                input = scanner.nextLine();
            }

            String output = EncounterController.input;

            inputComparison = EncounterController.input = "";

            int outputInt = 0;

            if (output.equalsIgnoreCase("back")) {
                gameEngine.printMessage("You are no longer attacking.");
                break;
            }

            try {
                outputInt = Integer.parseInt(output);
            } catch (Exception ex) {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                        "number from 1 to " + enemyCount + ".");
            }

            if (enemiesWithIndex.containsKey(outputInt)) {
                var enemyName = enemiesWithIndex.get(outputInt);
                enemyChosen = ((CombatEncounter) encounter).getEnemyFromEnemies(enemyName);
                isEnemyChosen = true;
            } else {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                        "number from 1 to " + enemyCount + ".");
            }
        }

        if (isEnemyChosen) {
            playerTurn(gameEngine, player, enemyChosen);

            gameEngine.printMessage("Would you like to use an item?");
            boolean isAnswered = false;

            while(!isAnswered) {
                if (gameEngine.getPlatform() == GamePlatform.SWING) {
                    Thread.sleep(100);
                    if (EncounterController.input.equals(inputComparison)) {
                        continue;
                    }
                } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                    EncounterController.input = scanner.nextLine();
                }

                String output = EncounterController.input;
                isAnswered = true;

                inputComparison = EncounterController.input = "";

                if (output.equalsIgnoreCase("y") || output.equalsIgnoreCase("yes")) {
                    EncounterController.useItem(gameEngine);
                } else if (output.equalsIgnoreCase("n") || output.equalsIgnoreCase("no")) {
                    break;
                } else {
                    gameEngine.printMessage("I'm sorry, I didn't understand.");
                    gameEngine.printMessage("Would you like to use an item?");
                }
            }

            enemyTurn(gameEngine, (CombatEncounter) encounter, player);
            gameEngine.printMessage("Your current health is " + (int) player.getCurrentHealth() + "/" +
                    (int) player.getMaxHealth());
        }
    }

    /**
     * A method intended to handle behaviour when a player chooses to use an item. Further behaviour can be
     * given via a generic Useable interface being provided.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @throws InventoryFullException Thrown in the event that an item removed from a player's inventory could
     * result in an inventory being full.
     * @throws InvalidValueException Thrown in the event of an invalid value being provided. See specific methods
     * for more information on what constitutes a valid value.
     * @see Item#setOnUseBehaviour(Useable)
     */
    public static void useItem(GameEngine gameEngine) throws InventoryFullException, InvalidValueException, InterruptedException {
        var player = gameEngine.getPlayer();
        boolean isItemChosen = false;
        Item itemChosen = null;
        int itemCount = player.getInventory().size();

        String inputComparison = input;
        var itemsWithIndex = getInventoryItemsWithIndex(gameEngine);

        gameEngine.printMessage("Choose an item to use:");
        for (Map.Entry<Integer, String> entry : itemsWithIndex.entrySet()) {
            gameEngine.printMessage("\t" + entry.getKey() + ". " + entry.getValue());
        }

        while (!isItemChosen) {
            if (gameEngine.getPlatform() == GamePlatform.SWING) {
                Thread.sleep(100);
                if (input.equals(inputComparison)) {
                    continue;
                }
            } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                input = scanner.nextLine();
            }

            String output = EncounterController.input;

            inputComparison = EncounterController.input = "";

            int outputInt = 0;

            if (output.equalsIgnoreCase("back")) {
                gameEngine.printMessage("You are no longer about to use an item.");
                break;
            }

            try {
                outputInt = Integer.parseInt(output);
            } catch (Exception ex) {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number " +
                        "from 1 to " + itemCount + ".");
                output = scanner.nextLine();
            }

            if (itemsWithIndex.containsKey(outputInt)) {
                var itemName = itemsWithIndex.get(outputInt);
                itemChosen = player.getItemFromInventory(itemName);
                isItemChosen = true;
            } else {
                gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a number " +
                        "from 1 to " + itemCount + ".");
            }
        }

        if (isItemChosen) {
            int numberOfUsesRemaining = itemChosen.getNumberOfUses();
            itemChosen.onUse(gameEngine);

            if (numberOfUsesRemaining != 0) {
                if (itemChosen.getNumberOfUses() != 0) {
                    gameEngine.printMessage("You now have " + numberOfUsesRemaining + " use(s) remaining " +
                            "of the item '" + itemChosen.getName() + "'");
                } else {
                    gameEngine.printMessage("You have depleted " + itemChosen.getName() + "'s uses.");
                    gameEngine.getPlayer().removeItemFromInventory(itemChosen);
                }

            }
        }
    }

    /**
     * A method intended to be used to choose a specific NonPlayableCharacter object. This can also be used by a
     * generic Useable interface provided to, for example, an Item object.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param encounter Required to determine exactly which Encounter object is to be interacted with.
     * @return Returns a NonPlayableCharacter object after a player has selected one via input prompts in the
     * game interface.
     * @see Item#setOnUseBehaviour(Useable)
     * @see NonPlayableCharacter
     */
    public static NonPlayableCharacter chooseNpc(GameEngine gameEngine, Encounter encounter) throws InterruptedException {
        boolean isTargetChosen = false;
        NonPlayableCharacter targetChosen = null;
        String inputComparison = input;

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
                    if (gameEngine.getPlatform() == GamePlatform.SWING) {
                        Thread.sleep(100);
                        if (input.equals(inputComparison)) {
                            continue;
                        }
                    } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                        input = scanner.nextLine();
                    }

                    String output = EncounterController.input;

                    inputComparison = EncounterController.input = "";

                    int outputInt = 0;

                    if (output.equalsIgnoreCase("back")) {
                        gameEngine.printMessage("You are no longer about to use this item on an enemy.");
                        break;
                    }

                    try {
                        outputInt = Integer.parseInt(output);
                    } catch (Exception ex) {
                        gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                                "number from 1 to " + enemyCount + ".");
                        output = scanner.nextLine();
                    }

                    if (enemiesWithIndex.containsKey(outputInt)) {
                        var enemyName = enemiesWithIndex.get(outputInt);
                        targetChosen = ((CombatEncounter) encounter).getEnemyFromEnemies(enemyName);
                        isTargetChosen = true;
                    } else {
                        gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                                "number from 1 to " + enemyCount + ".");
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
                if (gameEngine.getPlatform() == GamePlatform.SWING) {
                    Thread.sleep(100);
                    if (input.equals(inputComparison)) {
                        continue;
                    }
                } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                    input = scanner.nextLine();
                }

                String output = EncounterController.input;

                inputComparison = EncounterController.input = "";
                int outputInt = 0;

                if (output.equalsIgnoreCase("back")) {
                    gameEngine.printMessage("You are no longer about to interact.");
                    break;
                }

                try {
                    outputInt = Integer.parseInt(output);
                } catch (Exception ex) {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                            "number from 1 to " + allyCount + ".");
                    output = scanner.nextLine();
                }

                if (alliesWithIndex.containsKey(outputInt)) {
                    var allyName = alliesWithIndex.get(outputInt);
                    targetChosen = ((NonCombatEncounter) encounter).getAllyFromAllies(allyName);
                    isTargetChosen = true;
                } else {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                            "number from 1 to " + allyCount + ".");
                }
            }

            if (isTargetChosen) {
                return targetChosen;
            }
        }

        return null;
    }

    /**
     * A method intended to be used to choose a specific Prop object.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param encounter Required to determine exactly which Encounter object is to be interacted with.
     * @return Returns a NonPlayableCharacter object after a player has selected one via input prompts in the
     * game interface.
     * @see Prop
     */
    public static Prop chooseProp(GameEngine gameEngine, Encounter encounter) throws InterruptedException {
        boolean isTargetChosen = false;
        Prop targetChosen = null;
        String inputComparison = input;


        int propCount = encounter.getProps().size();
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
                if (gameEngine.getPlatform() == GamePlatform.SWING) {
                    Thread.sleep(100);
                    if (input.equals(inputComparison)) {
                        continue;
                    }
                } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                    input = scanner.nextLine();
                }

                String output = EncounterController.input;

                inputComparison = EncounterController.input = "";

                int outputInt = 0;

                if (output.equalsIgnoreCase("back")) {
                    gameEngine.printMessage("You are no longer about to interact with an object.");
                    break;
                }

                try {
                    outputInt = Integer.parseInt(output);
                } catch (Exception ex) {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                            "number from 1 to " + propCount + ".");
                    output = scanner.nextLine();
                }

                if (propsWithIndex.containsKey(outputInt)) {
                    var propName = propsWithIndex.get(outputInt);
                    targetChosen = encounter.getPropFromProps(propName);
                    isTargetChosen = true;
                } else {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                            "number from 1 to " + propCount + ".");
                }
            }
        }

        if (isTargetChosen) {
            return targetChosen;
        }

        return null;
    }

    /**
     * A method intended to be used to choose a specific Item object when interacting (trading) with an Ally.
     * Once an item is chosen, its cost is deducted from the player's currency amount, and it is added to a
     * player's inventory. In the case of the inventory being full, or the player not having enough gold, a
     * message is shown in the game interface.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param ally Required to determine exactly which Ally object is to be interacted with.
     * @return Returns an Item object after a player has selected one via input prompts in the game interface.
     */
    public static Item chooseItem(GameEngine gameEngine, Ally ally) throws InterruptedException {
        boolean isTargetChosen = false;
        Item targetChosen = null;
        String inputComparison = input;

        var itemsWithIndex = getAllyItemsWithIndex(ally);
        int itemCount = itemsWithIndex.size();

        if (itemCount == 0) {
            gameEngine.printMessage("There are currently no items to purchase.");
            return null;
        } else {
            gameEngine.printMessage("Choose an item to purchase (" + (int)gameEngine.getPlayer()
                    .getCurrencyAmount()
                    + " gold):");

            for (Map.Entry<Integer, String> entry : itemsWithIndex.entrySet()) {
                Item itemFromTable = ally.getItemFromItemTable(entry.getValue());
                if (gameEngine.getGameSettings().isOutputSeparatedByNewLine()) {
                    gameEngine.printMessageFormatted("\t%s %-12s %s\n", entry.getKey() + ".", "Gold: " +
                            itemFromTable.getValue(), entry.getValue() + "\n");
                } else {
                    gameEngine.printMessageFormatted("\t%s %-12s %s\n", entry.getKey() + ".", "Gold: " +
                            itemFromTable.getValue(), entry.getValue());
                }
            }

            while (!isTargetChosen) {
                if (gameEngine.getPlatform() == GamePlatform.SWING) {
                    Thread.sleep(100);
                    if (input.equals(inputComparison)) {
                        continue;
                    }
                } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                    input = scanner.nextLine();
                }

                String output = EncounterController.input;

                inputComparison = EncounterController.input = "";

                int outputInt = 0;

                if (output.equalsIgnoreCase("back")) {
                    gameEngine.printMessage("You are no longer trading.");
                    break;
                }

                try {
                    outputInt = Integer.parseInt(output);
                } catch (Exception ex) {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                            "number from 1 to " + itemCount + ".");
                    output = scanner.nextLine();
                }

                if (itemsWithIndex.containsKey(outputInt)) {
                    var itemName = itemsWithIndex.get(outputInt);
                    targetChosen = ally.getItemFromItemTable(itemName);
                    boolean isAnswered = false;

                    double currentGold = gameEngine.getPlayer().getCurrencyAmount();
                    int itemValue = targetChosen.getValue();
                    if (itemValue > currentGold) {
                        gameEngine.printMessage("You do not have enough gold to purchase this item.");
                    } else {
                        gameEngine.printMessage("Would you like to purchase this item for " +
                                targetChosen.getValue() + " gold?");
                        while (!isAnswered) {

                            inputComparison = EncounterController.input = "";

                            if (gameEngine.getPlatform() == GamePlatform.SWING) {
                                Thread.sleep(100);
                            if (input.equals(inputComparison)) {
                                continue;
                            }
                            } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                                input = scanner.nextLine();
                            }

                            output = EncounterController.input;
                            isAnswered = true;

                            inputComparison = EncounterController.input = "";

                            if (output.equalsIgnoreCase("yes") || output.equalsIgnoreCase("y")) {
                            isTargetChosen = true;
                            } else {
                                gameEngine.printMessage("You choose to not purchase this item.");

                                gameEngine.printMessage("Choose an item to purchase (" +
                                        (int)gameEngine.getPlayer()
                                        .getCurrencyAmount()
                                        + " gold):");

                                for (Map.Entry<Integer, String> entry : itemsWithIndex.entrySet()) {
                                    Item itemFromTable = ally.getItemFromItemTable(entry.getValue());
                                    gameEngine.printMessageFormatted("\t%s %-12s %s\n", entry.getKey() +
                                            ".", "Gold: " + itemFromTable.getValue(), entry.getValue());
                                }
                            }
                        }
                    }
                } else {
                    gameEngine.printMessage("'" + output + "' is not a valid choice. Please enter a " +
                            "number from 1 to " + itemCount + ".");
                }
            }

            if (isTargetChosen) {
                return targetChosen;
            }
        }

        return null;
    }

    private static void enemyTurn(GameEngine gameEngine, CombatEncounter encounter, Player player)
            throws InvalidValueException, InterruptedException {
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
                        int enemyDamage = ProbabilityCalculator.damageCalculator((int)enemy.getMinDamage(),
                                (int)enemy.getMaxDamage());
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

    private static void playerTurn(GameEngine gameEngine, Player player, Enemy enemyChosen)
            throws InvalidValueException, InterruptedException {
        int playerDamage = ProbabilityCalculator.damageCalculator((int) player.getMinDamage(),
                (int) player.getMaxDamage());
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

        for (Map.Entry<String, Prop> entry : encounter.getProps().entrySet()) {
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

    /**
     * A class intended to calculate which items are dropped from a specific encounter. The items in question
     * depend on the Encounter in question, and the items (together with drop percentage) that each Enemy
     * object has. Once the items have been dropped on the encounter being defeated then they will be added to
     * a player's inventory.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param encounter Required to determine exactly which Encounter object is to be interacted with.
     * @throws InvalidValueException Thrown in the event of an invalid value being provided. See specific methods
     * for more information on what constitutes a valid value.
     */
    public static void getEncounterDrops(GameEngine gameEngine, Encounter encounter) throws InvalidValueException, InterruptedException {

        Player player = gameEngine.getPlayer();

        double totalCurrencyReceived = 0;

        if (encounter instanceof CombatEncounter) {
            for (Map.Entry<String, Enemy> enemyEntry : ((CombatEncounter) encounter).getEnemies().entrySet()) {
                Enemy enemy = enemyEntry.getValue();

                totalCurrencyReceived += enemy.getCurrencyReceivedOnDeath();

                if (!enemy.getNpcItemTable().isEmpty()) {

                    for (Map.Entry<String, Item> itemEntry : enemy.getNpcItemTable().entrySet()) {
                        Item item = itemEntry.getValue();

                        if ((item.getDropChance() * 100) == 0 ||
                                ProbabilityCalculator.isDropped((int)(item.getDropChance() * 100))) {

                            try {
                                player.addItemToInventory(item);
                                gameEngine.printMessage("You received " + item.getName() + " from " +
                                        enemy.getName() + ".");
                            } catch (InventoryFullException ex) {
                                gameEngine.printMessage(ex.getMessage());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
            if (totalCurrencyReceived > 0) {
                gameEngine.printMessage("You received " + (int)totalCurrencyReceived + " gold.");
                player.addToCurrencyAmount((int)totalCurrencyReceived);
            }

            getFeatRewards(gameEngine, encounter);
        }
    }

    public static void getFeatRewards(GameEngine gameEngine, Encounter encounter) throws InterruptedException {
        var player = gameEngine.getPlayer();

        if (!encounter.getFeatRewards().isEmpty()) {
            for (Map.Entry<String, Feat> featEntry: encounter.getFeatRewards().entrySet()) {
                Feat feat = featEntry.getValue();
                player.addFeatToFeats(feat);
                if (!feat.isSecret()) {
                    gameEngine.printMessage("You received the feat '" + feat.getName() + "'.");
                }
            }
        }
    }

    public static String getInput() {
        return input;
    }

    public static void setInput(String input) {
        EncounterController.input = input;
    }
}
