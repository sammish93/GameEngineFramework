package sammish93.tbage.gui;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.interfaces.Closeable;
import sammish93.tbage.models.FixedEncounters;
import sammish93.tbage.models.RandomEncounters;
import sammish93.tbage.tools.EncounterTraversalController;

import static sammish93.tbage.GameEngine.scanner;

/**
 * An class intended to be used to render the game in a terminal window.
 */
public class Terminal extends GameInterface implements Closeable<String> {

    private boolean exitBool = false;

    /**
     *
     * @param gameEngine A specific GameEngine instance is required to be able to retrieve other dependencies
     *                   such as Player, Encounter, GameSettings, etc.
     */
    public Terminal(GameEngine gameEngine) throws InventoryFullException, InvalidValueException {
        super(gameEngine);

        run();
    }

    /**
     * Closes the terminal window.
     * @param exitString When 'exit' is passed as a command during runtime the terminal window closes.
     */
    public void close(String exitString) {
        if (exitString.equalsIgnoreCase("exit")) {
            exitBool = true;
        }
    }

    private void run() throws InventoryFullException, InvalidValueException {
        var controller = getEncounterController();
        var gameEngine = getGameEngine();
        var encounters = getEncounters();

        gameEngine.printMessage("I will run in a terminal window until user types 'exit'.");

        if (!controller.checkEncounterPaths(gameEngine)) {
            close("exit");
        }

        if (encounters == null ||
                (encounters instanceof FixedEncounters) && ((FixedEncounters)encounters)
                        .getEncounters().isEmpty() ||
                (encounters instanceof RandomEncounters) && ((RandomEncounters) encounters)
                        .getEncounterOrder().isEmpty()) {
            gameEngine.printMessage("There are no encounters present.");
        }

        // Closes current Terminal process on user entering 'exit'.
        while (!exitBool) {
            String output;

            if (EncounterTraversalController.getCurrentEncounter() != null) {
                output = EncounterTraversalController.getCurrentEncounter().run(gameEngine);
            } else {
                gameEngine.printMessage("Game has finished. Please type 'exit'.");
                output = scanner.nextLine();
            }

            close(output);

            if (controller.getCurrentEncounter() != null) {
                controller.progressToNextEncounter(output);
            }

        }

        gameEngine.printMessage("Game is exiting...");
    }
}
