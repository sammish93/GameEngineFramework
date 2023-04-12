package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.exceptions.InventoryFullException;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.npcs.Ally;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.models.props.Prop;
import no.hiof.samuelcd.tbage.tools.EncounterController;
import no.hiof.samuelcd.tbage.tools.StringParser;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import static no.hiof.samuelcd.tbage.GameEngine.scanner;

public class NonCombatEncounter extends Encounter {

    private int allyCount;
    private TreeMap<String, Ally> allies;


    private NonCombatEncounter(String name, String imagePath, TreeMap<String, Feat> featChecks, TreeMap<String, Feat> featRewards,  TreeMap<String, Ally> allies, TreeMap<String, String> navigationOptions, TreeMap<String, Prop> props) {
        super(name, imagePath, featChecks, featRewards, navigationOptions, props);

        this.allies = Objects.requireNonNullElseGet(allies, TreeMap::new);

    }

    public static NonCombatEncounter create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new NonCombatEncounter(randomlyGeneratedId.toString(), null, null, null, null, null, null);
    }

    public static NonCombatEncounter create(String name) {
        return new NonCombatEncounter(name, null, null, null, null, null, null);
    }

    public TreeMap<String, Ally> getAllies() {
        return allies;
    }

    public void setAllies(TreeMap<String, Ally> allies) {
        this.allies = allies;
    }

    public Ally getAllyFromAllies(String allyName) {
        return allies.get(allyName);
    }

    public void addAllyToAllies(Ally ally) {
        allies.put(ally.getName(), ally);
    }

    public void removeAllyFromAllies(Ally ally) {
        allies.remove(ally.getName());
    }

    public void removeAllyFromAllies(String allyName) {
        allies.remove(allyName);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String run(GameEngine gameEngine) throws InventoryFullException {
        String input = "";
        TreeMap<String, String> inputMap = new TreeMap<>();
        setBacktracking(isDefeated());

        if (isBacktracking()) {
            gameEngine.printMessage("You return to encounter '" + getName() + "'. Enter a navigational command or 'progress' to return to where you came from.");
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
                    } else if (value.equalsIgnoreCase("options") || value.equalsIgnoreCase("help")) {
                        printOptions(gameEngine);
                    } else if (value.equalsIgnoreCase("back")) {
                        gameEngine.printMessage("Invalid command. Did you want to navigate to the previous enconter? Try a directional command like 'go south'.");
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
                            EncounterController.useItem(gameEngine, this);
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
                    } else if (getNavigationOptions().containsKey(noun) && !getNavigationalVerbs().contains(verb)) {
                        gameEngine.printMessage("Try another means of traversal.");
                    } else if (getNavigationOptions().containsKey(noun) && getNavigationalVerbs().contains(verb)) {
                        setDefeated(true);
                        return noun;
                    } else {
                        gameEngine.printMessage("Try something else.");
                    }
                }
            }


            if (isDefeated() && !isBacktracking()) {
                String answer;

                gameEngine.printMessage("Would you like to progress to the next encounter?");
                answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                    break;
                } else {
                    gameEngine.printMessage("Enter a navigational command or 'progress' to traverse to another encounter.");
                    setBacktracking(true);
                }
            }
        }

        return "defeated";
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

        gameEngine.printMessage("Your current health is " + (int)player.getCurrentHealth() + "/" + (int)player.getMaxHealth());
        for (Map.Entry<String, Ally> entry : allies.entrySet()) {
            var ally = entry.getValue();
            gameEngine.printMessage("Ally " + allyIteration++ + ": " + ally.getName());
        }
    }
}
