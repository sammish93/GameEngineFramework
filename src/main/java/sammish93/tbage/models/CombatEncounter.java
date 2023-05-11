package sammish93.tbage.models;

import sammish93.tbage.GameEngine;
import sammish93.tbage.enums.GamePlatform;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.tools.EncounterController;
import sammish93.tbage.tools.StringParser;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import static sammish93.tbage.GameEngine.scanner;

public class CombatEncounter extends Encounter {

    private TreeMap<String, Enemy> enemies;
    private TreeMap<String, Integer> duplicateEnemiesInEnemies;


    private CombatEncounter(String name, String imagePath, TreeMap<String, Feat> featChecks,
                            TreeMap<String, Feat> featRewards, TreeMap<String, Enemy> enemies,
                            TreeMap<String, String> navigationOptions, TreeMap<String, Prop> props)
            throws InvalidValueException {
        super(name, imagePath, featChecks, featRewards, navigationOptions, props);

        this.enemies = Objects.requireNonNullElseGet(enemies, TreeMap::new);
        duplicateEnemiesInEnemies = new TreeMap<>();
    }

    /**
     *
     * @return Returns an instantiated CombatEncounter object with a default UUID set as a name.
     * @throws InvalidValueException
     */
    public static CombatEncounter create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new CombatEncounter(randomlyGeneratedId.toString(),  null, null,
                null, null, null, null);
    }

    /**
     *
     * @param name A string that represents a CombatEncounter object's name.
     * @return Returns an instantiated CombatEncounter object.
     * @throws InvalidValueException
     */
    public static CombatEncounter create(String name) throws InvalidValueException {
        return new CombatEncounter(name,null, null, null, null,
                null, null);
    }

    /**
     *
     * @param name A string that represents a CombatEncounter object's name.
     * @param imagePath
     * @param featChecks An existing TreeMap that includes feats to be checked on traversing to an encounter.
     * @param featRewards An existing TreeMap that includes feats to be rewarded on defeating an encounter.
     * @param enemies An existing TreeMap that includes enemies to be included in an encounter.
     * @param navigationOptions An existing TreeMap that includes navigation options (nouns) valid in said
     *                          encounter.
     * @param props An existing TreeMap that includes props to be included in an encounter.
     * @return Returns an instantiated CombatEncounter object.
     * @throws InvalidValueException
     */
    public static CombatEncounter create(String name, String imagePath, TreeMap<String, Feat> featChecks,
                                         TreeMap<String, Feat> featRewards, TreeMap<String, Enemy> enemies,
                                         TreeMap<String, String> navigationOptions, TreeMap<String, Prop> props)
            throws InvalidValueException {
        return new CombatEncounter(name,imagePath, featChecks, featRewards, enemies, navigationOptions, props);
    }


    /**
     *
     * @return Returns a TreeMap of all enemies in the encounter.
     */
    public TreeMap<String, Enemy> getEnemies() {
        return enemies;
    }

    /**
     *
     * @param enemies Sets enemies in an encounter to an existing TreeMap.
     */
    public void setEnemies(TreeMap<String, Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     *
     * @param enemyName A string representing an instantiated Enemy object.
     * @return Returns an Enemy object if said enemy exists in the current encounter.
     */
    public Enemy getEnemyFromEnemies(String enemyName) {
        return enemies.get(enemyName);
    }

    /**
     * This class is used to add an enemy to an encounter. It is also used in the event that duplicate enemies
     * exist in the encounter at the same time, in which an index value is added to the suffix of an item's name.
     * (e.g. 'Skeleton' is an encounter. The developer adds another 'Skeleton' to the same encounter, and the
     * first enemy is renamed to 'Skeleton 1', while the second is renamed to 'Skeleton 2'.
     * @param enemy An existing Enemy object.
     */
    public void addEnemyToEnemies(Enemy enemy) {
        try {
            int iteration = 1;

            if (getEnemyFromEnemies(enemy.getName()) != null) {
                int amountOfDuplicates = 2;

                duplicateEnemiesInEnemies.put(enemy.getName(), amountOfDuplicates);

                Enemy enemyFromEnemies = (Enemy)getEnemyFromEnemies(enemy.getName()).clone();
                enemyFromEnemies.setName(enemy.getName() + " " + iteration++);

                enemies.remove(enemy.getName());
                enemies.put(enemyFromEnemies.getName(), enemyFromEnemies);
            } else if (duplicateEnemiesInEnemies.get(enemy.getName()) != null) {
                iteration = duplicateEnemiesInEnemies.get(enemy.getName());
                duplicateEnemiesInEnemies.remove(enemy.getName());
                duplicateEnemiesInEnemies.put(enemy.getName(), iteration++ + 1);
            }

            if (iteration == 1) {
                enemies.put(enemy.getName(), enemy);
            } else {
                Enemy enemyCloned = (Enemy)enemy.clone();
                enemyCloned.setName(enemyCloned.getName() + " " + iteration);
                enemies.put(enemyCloned.getName(), enemyCloned);
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Removes a single enemy from an encounter.
     * @param enemy An instantiated Enemy object.
     */
    public void removeEnemyFromEnemies(Enemy enemy) {
        enemies.remove(enemy.getName());
    }

    /**
     * Removes a single enemy from an encounter.
     * @param enemyName A string representing an instantiated Enemy object.
     */
    public void removeEnemyFromEnemies(String enemyName) {
        enemies.remove(enemyName);
    }

    private void combatEncounterIntroduction(GameEngine gameEngine) throws InterruptedException {
        if (!getIntroductoryMessage().isEmpty()) {
            gameEngine.printMessage(getIntroductoryMessage());
        }

        onInitiation(gameEngine);

        int enemyCount = enemies.size();

        if (enemyCount > 1) {
            gameEngine.printMessage("You face " + enemyCount + " enemies!");
        } else if (enemyCount == 1) {
            gameEngine.printMessage("You face an enemy!");
        }
    }

    private void printEnemies(GameEngine gameEngine) throws InterruptedException {
        int enemyIteration = 1;
        var player = gameEngine.getPlayer();

        gameEngine.printMessage("Your current health is " + (int)player.getCurrentHealth() + "/" +
                (int)player.getMaxHealth());
        for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
            var enemy = entry.getValue();
            gameEngine.printMessage("Enemy " + enemyIteration++ + ": " + enemy.getName() + ", Status: " +
                    enemy.getEnemyHealthStatus());
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

    /**
     * This method is used by the game's chosen interface.
     */
    @Override
    public String run(GameEngine gameEngine) throws InventoryFullException, InvalidValueException, InterruptedException {
        TreeMap<String, String> inputMap = new TreeMap<>();
        int turnNumber = 1;
        setBacktracking(isDefeated());
        String inputComparison = EncounterController.getInput();

        if (isBacktracking()) {
            gameEngine.printMessage("You return to encounter '" + getName() + "'. Enter a navigational " +
                    "command or 'progress' to return to where you came from.");
        }

        if (!isIntroductionPrinted()) {
            combatEncounterIntroduction(gameEngine);
            printEnemies(gameEngine);

            setIntroductionPrinted(true);
        }

        while (!isDefeated() || isBacktracking()) {

            if (gameEngine.getPlatform() == GamePlatform.SWING) {
                Thread.sleep(100);
                if (EncounterController.getInput().equals(inputComparison)) {
                    continue;
                }
            } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                EncounterController.setInput(scanner.nextLine());
            }

            inputMap = StringParser.read(gameEngine, EncounterController.getInput());

            inputComparison = "";
            EncounterController.setInput("");

            if (!gameEngine.getPlayer().isAlive()) {
                while (true) {
                    if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                        gameEngine.printMessage("Game has finished. Please type 'exit'.");
                    } else {
                        gameEngine.printMessage("Game has finished. Please press the 'Escape' key.");
                    }

                    if (scanner.nextLine().equalsIgnoreCase("exit")) {
                        return "exit";
                    }
                }
            }

            if (!inputMap.isEmpty()) {
                if (inputMap.get("command") != null) {

                    String value = inputMap.get("command");

                    if (value.equalsIgnoreCase("exit")) {
                        return "exit";
                    } else if (value.equalsIgnoreCase("options") ||
                            value.equalsIgnoreCase("help")) {
                        printOptions(gameEngine);
                    } else if (value.equalsIgnoreCase("back")) {
                        gameEngine.printMessage("Invalid command. Did you want to navigate to the " +
                                "previous encounter? Try a directional command like 'go south'.");
                    } else if (value.equalsIgnoreCase("defeated")) {
                        gameEngine.printMessage("You haven't defeated this encounter yet!");
                    } else if (value.equalsIgnoreCase("playerdeath")) {
                        gameEngine.printMessage("You aren't dead!");
                    } else if (value.equalsIgnoreCase("progress")) {
                        if (isDefeated() && getNavigationOptions().get("defeated") != null) {
                            return "defeated";
                        } else if (getNavigationOptions().get("defeated") == null) {
                            gameEngine.printMessage("Try something else.");
                        } else {
                            gameEngine.printMessage("You haven't defeated this encounter yet!");
                        }
                    } else if (value.equalsIgnoreCase("status")) {
                        printEnemies(gameEngine);
                    } else if (value.equalsIgnoreCase("interact")) {
                        gameEngine.printMessage("There is no one to interact with.");
                    } else if (value.equalsIgnoreCase("investigate")) {
                        if (!getProps().isEmpty()) {
                            Prop propToBeUsed = EncounterController.chooseProp(gameEngine, this);
                            if (propToBeUsed != null) {
                                propToBeUsed.onUse(gameEngine);
                            }
                        } else {
                            gameEngine.printMessage("There is currently nothing to investigate");
                        }
                    } else if (value.equalsIgnoreCase("attack")) {
                        if (isDefeated()) {
                            gameEngine.printMessage("There are no enemies living!");
                        } else {

                            EncounterController.turn(gameEngine, this, turnNumber++);

                            if (allEnemiesDead()) {
                                setDefeated(true);
                                EncounterController.getEncounterDrops(gameEngine, this);
                            }

                            if (!gameEngine.getPlayer().isAlive()) {
                                gameEngine.printMessage("You have died!");
                            }
                        }
                    } else if (value.equalsIgnoreCase("inventory")) {
                        printInventory(gameEngine);
                    } else if (value.equalsIgnoreCase("use")) {
                        if (!gameEngine.getPlayer().getInventory().isEmpty()) {
                            EncounterController.useItem(gameEngine);

                            if (allEnemiesDead()) {
                                setDefeated(true);

                                if (!gameEngine.getPlayer().isAlive()) {
                                    gameEngine.printMessage("You have died!");
                                } else {
                                    EncounterController.getEncounterDrops(gameEngine, this);
                                }
                            }
                        } else {
                            gameEngine.printMessage("You have no items in your inventory.");
                        }

                    } else if (allEnemiesDead() || EncounterController.getInput().equalsIgnoreCase("skip")) {
                        setDefeated(true);
                        EncounterController.getEncounterDrops(gameEngine, this);
                    }
                } else if (inputMap.get("verb") != null && inputMap.get("noun") != null) {
                    String verb = inputMap.get("verb");
                    String noun = inputMap.get("noun");

                    if (noun.equalsIgnoreCase("defeated") || !isDefeated()) {
                        gameEngine.printMessage("You haven't defeated this encounter yet!");
                    } else if (getNavigationOptions().containsKey(noun) && !getNavigationalVerbs().contains(verb)) {
                        gameEngine.printMessage("Try another means of traversal.");
                    } else if (getNavigationOptions().containsKey(noun) && getNavigationalVerbs().contains(verb)) {
                        return noun;
                    } else {
                        gameEngine.printMessage("Try something else.");
                    }
                }
            }


            if (isDefeated() && !isBacktracking()) {
                if (getNavigationOptions().get("defeated") != null) {
                    gameEngine.printMessage("Would you like to progress to the next encounter?");
                    boolean isAnswered = false;

                    while (!isAnswered) {
                        if (gameEngine.getPlatform() == GamePlatform.SWING) {
                            Thread.sleep(100);
                            if (EncounterController.getInput().equals(inputComparison)) {
                                continue;
                            }
                        } else if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                            EncounterController.setInput(scanner.nextLine());
                        }

                        String output = EncounterController.getInput();
                        isAnswered = true;

                        inputComparison = "";
                        EncounterController.setInput("");

                        if (output.equalsIgnoreCase("yes") || output.equalsIgnoreCase("y")) {
                            break;
                        } else {
                            gameEngine.printMessage("Enter a navigational command or 'progress' to traverse to " +
                                    "another encounter.");
                            setBacktracking(true);
                        }
                    }


                } else {
                    gameEngine.printMessage(getOnDefeatedMessage());
                    gameEngine.printMessage("Enter a navigational command to traverse to another encounter.");
                    setBacktracking(true);
                }
            }
        }

        return "defeated";
    }

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Encounter Type: Combat, ");
        sb.append(super.toString());

        if (!getEnemies().isEmpty()) {
            sb.append("\nEnemy Table: ");
            for (Map.Entry<String, Enemy> enemySet : getEnemies().entrySet()) {
                Enemy enemy = enemySet.getValue();
                sb.append("\n\t" + enemy.toString());
            }
        }

        return sb.toString();
    }
}
