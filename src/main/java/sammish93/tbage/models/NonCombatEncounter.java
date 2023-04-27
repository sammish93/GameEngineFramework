package sammish93.tbage.models;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.tools.EncounterController;
import sammish93.tbage.tools.StringParser;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import static sammish93.tbage.GameEngine.scanner;

public class NonCombatEncounter extends Encounter {

    private TreeMap<String, Ally> allies;


    private NonCombatEncounter(String name, String imagePath, TreeMap<String, Feat> featChecks,
                               TreeMap<String, Feat> featRewards,  TreeMap<String, Ally> allies,
                               TreeMap<String, String> navigationOptions, TreeMap<String, Prop> props)
            throws InvalidValueException {
        super(name, imagePath, featChecks, featRewards, navigationOptions, props);

        this.allies = Objects.requireNonNullElseGet(allies, TreeMap::new);

    }

    /**
     *
     * @return Returns an instantiated NonCombatEncounter object with a default UUID set as a name.
     * @throws InvalidValueException
     */
    public static NonCombatEncounter create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new NonCombatEncounter(randomlyGeneratedId.toString(), null, null,
                null, null, null, null);
    }

    /**
     *
     * @param name A string that represents a NonCombatEncounter object's name.
     * @return Returns an instantiated NonCombatEncounter object.
     * @throws InvalidValueException
     */
    public static NonCombatEncounter create(String name) throws InvalidValueException {
        return new NonCombatEncounter(name, null, null, null, null,
                null, null);
    }

    /**
     *
     * @param name A string that represents a NonCombatEncounter object's name.
     * @param imagePath
     * @param featChecks An existing TreeMap that includes feats to be checked on traversing to an encounter.
     * @param featRewards An existing TreeMap that includes feats to be rewarded on defeating an encounter.
     * @param allies An existing TreeMap that includes allies to be included in an encounter.
     * @param navigationOptions An existing TreeMap that includes navigation options (nouns) valid in said
     *                          encounter.
     * @param props An existing TreeMap that includes props to be included in an encounter.
     * @return Returns an instantiated NonCombatEncounter object.
     * @throws InvalidValueException
     */
    public static NonCombatEncounter create(String name, String imagePath, TreeMap<String, Feat> featChecks,
                                            TreeMap<String, Feat> featRewards,  TreeMap<String, Ally> allies,
                                            TreeMap<String, String> navigationOptions, TreeMap<String, Prop> props)
            throws InvalidValueException {
        return new NonCombatEncounter(name, imagePath, featChecks, featRewards, allies, navigationOptions, props);
    }


    /**
     *
     * @return Returns a TreeMap of all allies in the encounter.
     */
    public TreeMap<String, Ally> getAllies() {
        return allies;
    }

    /**
     *
     * @param allies Sets allies in an encounter to an existing TreeMap.
     */
    public void setAllies(TreeMap<String, Ally> allies) {
        this.allies = allies;
    }

    /**
     *
     * @param allyName A string representing an instantiated Ally object.
     * @return Returns an Ally object if said ally exists in the current encounter.
     */
    public Ally getAllyFromAllies(String allyName) {
        return allies.get(allyName);
    }

    /**
     * This class is used to add an ally to an encounter. Unlike the Enemy sibling class, an Ally cannot be
     * added twice to a single encounter.
     * @param ally An existing Ally object.
     * @see CombatEncounter#addEnemyToEnemies(Enemy)
     */
    public void addAllyToAllies(Ally ally) {
        allies.put(ally.getName(), ally);
    }

    /**
     * Removes a single ally from an encounter.
     * @param ally An instantiated Ally object.
     */
    public void removeAllyFromAllies(Ally ally) {
        allies.remove(ally.getName());
    }

    /**
     * Removes a single ally from an encounter.
     * @param allyName A string representing an instantiated Ally object.
     */
    public void removeAllyFromAllies(String allyName) {
        allies.remove(allyName);
    }

    private void nonCombatEncounterIntroduction(GameEngine gameEngine) {
        if (!getIntroductoryMessage().isEmpty()) {
            gameEngine.printMessage(getIntroductoryMessage());
        }

        onInitiation(gameEngine);
    }

    private void printAllies(GameEngine gameEngine) {
        int allyIteration = 1;
        var player = gameEngine.getPlayer();

        gameEngine.printMessage("Your current health is " + (int)player.getCurrentHealth() + "/" +
                (int)player.getMaxHealth());
        for (Map.Entry<String, Ally> entry : allies.entrySet()) {
            var ally = entry.getValue();
            gameEngine.printMessage("Ally " + allyIteration++ + ": " + ally.getName());
        }
    }

    /**
     * This method is used by the game's chosen interface.
     */
    @Override
    public String run(GameEngine gameEngine) throws InventoryFullException, InvalidValueException {
        String input = "";
        TreeMap<String, String> inputMap = new TreeMap<>();
        setBacktracking(isDefeated());

        if (isBacktracking()) {
            gameEngine.printMessage("You return to encounter '" + getName() + "'. Enter a navigational " +
                    "command or 'progress' to return to where you came from.");
        }

        if (!isIntroductionPrinted()) {
            nonCombatEncounterIntroduction(gameEngine);

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
                        if (isDefeated()) {
                            return "defeated";
                        } else {
                            gameEngine.printMessage("You haven't defeated this encounter yet!");
                        }
                    } else if (value.equalsIgnoreCase("status")) {
                        printAllies(gameEngine);
                    } else if (value.equalsIgnoreCase("inventory")) {
                        printInventory(gameEngine);
                    } else if (value.equalsIgnoreCase("attack")) {
                        gameEngine.printMessage("There are no enemies to attack!");
                    } else if (value.equalsIgnoreCase("interact")) {
                        if (!getAllies().isEmpty()) {
                            Ally ally = ((Ally) EncounterController.chooseNpc(gameEngine, this));
                            if (ally != null) {
                                ally.onInteraction(gameEngine);
                            }
                        } else {
                            gameEngine.printMessage("There is currently no one to interact with.");
                        }
                    } else if (value.equalsIgnoreCase("investigate")) {
                        if (!getProps().isEmpty()) {
                            Prop propToBeUsed = EncounterController.chooseProp(gameEngine, this);
                            if (propToBeUsed != null) {
                                propToBeUsed.onUse(gameEngine);
                            }
                        } else {
                            gameEngine.printMessage("There is currently nothing to investigate");
                        }
                    } else if (value.equalsIgnoreCase("use")) {
                        if (!gameEngine.getPlayer().getInventory().isEmpty()) {
                            EncounterController.useItem(gameEngine);
                        } else {
                            gameEngine.printMessage("You have no items in your inventory.");
                        }

                        if (!gameEngine.getPlayer().isAlive()) {
                            gameEngine.printMessage("You have died!");
                        }
                    } else if (input.equalsIgnoreCase("skip")) {
                        setDefeated(true);
                    }
                } else if (inputMap.get("verb") != null && inputMap.get("noun") != null) {
                    String verb = inputMap.get("verb");
                    String noun = inputMap.get("noun");

                    if (noun.equalsIgnoreCase("defeated")) {
                        gameEngine.printMessage("You haven't defeated this encounter yet!");
                    } else if (getNavigationOptions().containsKey(noun) && !getNavigationalVerbs()
                            .contains(verb)) {
                        gameEngine.printMessage("Try another means of traversal.");
                    } else if (getNavigationOptions().containsKey(noun) && getNavigationalVerbs()
                            .contains(verb)) {
                        setDefeated(true);
                        EncounterController.getFeatRewards(gameEngine, this);
                        return noun;
                    } else {
                        gameEngine.printMessage("Try something else.");
                    }
                }
            }


            if (isDefeated() && !isBacktracking()) {
                EncounterController.getFeatRewards(gameEngine, this);

                String answer;

                gameEngine.printMessage("Would you like to progress to the next encounter?");
                answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                    break;
                } else {
                    gameEngine.printMessage("Enter a navigational command or 'progress' to traverse to " +
                            "another encounter.");
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

        sb.append("Encounter Type: Non-Combat, ");
        sb.append(super.toString());

        if (!getAllies().isEmpty()) {
            sb.append("\nAlly Table: ");
            for (Map.Entry<String, Ally> allySet : getAllies().entrySet()) {
                Ally ally = allySet.getValue();
                sb.append("\n\t" + ally.toString());
            }
        }

        return sb.toString();
    }
}
