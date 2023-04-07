package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.interfaces.Closeable;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.encounters.RandomEncounters;
import no.hiof.samuelcd.tbage.tools.EncounterTraversalController;

import static no.hiof.samuelcd.tbage.GameEngine.scanner;

public class Terminal extends GameInterface implements Closeable<String> {


    boolean exitBool = false;

    public Terminal(GameEngine gameEngine) {
        super(gameEngine);

        run();
    }

    public void close(String exitString) {
        if (exitString.equalsIgnoreCase("exit")) {
            exitBool = true;
        }
    }

    private void run() {
        var controller = getEncounterController();
        gameEngine.printMessage("I will run in a terminal window until user types 'exit'.");

        if (!controller.checkEncounterPaths(gameEngine)) {
            close("exit");
        }

        if (encounters == null ||
                (encounters instanceof FixedEncounters) && ((FixedEncounters)encounters).getEncounters().isEmpty() ||
                (encounters instanceof RandomEncounters) && ((RandomEncounters) encounters).getEncounterOrder().isEmpty()) {
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
