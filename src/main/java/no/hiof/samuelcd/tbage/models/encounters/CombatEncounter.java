package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.tools.EncounterController;
import no.hiof.samuelcd.tbage.tools.StringParser;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static java.lang.String.format;
import static no.hiof.samuelcd.tbage.GameEngine.scanner;

public class CombatEncounter extends Encounter {

    private int enemyCount;
    private TreeMap<String, Enemy> enemies;
    private TreeMap<String, Integer> duplicateEnemiesInEnemies;
    private int turnCount = 1;


    private CombatEncounter(String name, String imagePath, TreeMap<String, Feat> featChecks, TreeMap<String, Feat> featRewards, TreeMap<String, String> navigationOptions) {
        super(name, imagePath, featChecks, featRewards, navigationOptions);

        enemies = new TreeMap<>();
        duplicateEnemiesInEnemies = new TreeMap<>();
    }

    public static CombatEncounter create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new CombatEncounter(randomlyGeneratedId.toString(),  null, null, null, null);
    }

    public static CombatEncounter create(String name) {
        return new CombatEncounter(name,null, null, null, null);
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
        String input = "";
        TreeMap<String, String> inputMap = new TreeMap<>();
        int turnNumber = 1;
        setBacktracking(isDefeated());

        if (isBacktracking()) {
            gameEngine.printMessage("You return to encounter '" + getName() + "'. Enter a navigational command or 'progress' to return to where you came from.");
        }

        if (!isIntroductionPrinted()) {
            combatEncounterIntroduction(gameEngine);
            printEnemies(gameEngine);

            setIntroductionPrinted(true);
        }

        while (!isDefeated() || isBacktracking()) {

            input = scanner.nextLine();
            inputMap = StringParser.read(gameEngine, input);

            if (!gameEngine.getPlayer().isAlive()) {
                while (true) {
                    gameEngine.printMessage("Game has finished. Please type 'exit'.");

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
                    } else if (value.equalsIgnoreCase("options") || value.equalsIgnoreCase("help")) {
                        printOptions(gameEngine);
                    } else if (value.equalsIgnoreCase("back")) {
                        gameEngine.printMessage("Invalid command. Did you want to navigate to the previous enconter? Try a directional command like 'go south'.");
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
                    } else if (value.equalsIgnoreCase("investigate")) {
                        gameEngine.printMessage("There is currently nothing to investigate.");
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
                            EncounterController.useItem(gameEngine, this);

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

                    } else if (allEnemiesDead() || input.equalsIgnoreCase("skip")) {
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
                String answer;

                if (getNavigationOptions().get("defeated") != null) {
                    gameEngine.printMessage("Would you like to progress to the next encounter?");
                    answer = scanner.nextLine();

                    if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                        break;
                    } else {
                        gameEngine.printMessage("Enter a navigational command or 'progress' to traverse to another encounter.");
                        setBacktracking(true);
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

    private void combatEncounterIntroduction(GameEngine gameEngine) {
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
