package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Closeable;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.player.Player;
import no.hiof.samuelcd.tbage.tools.GameController;

import java.util.Scanner;

public class Terminal extends GameInterface implements Closeable<String> {


    boolean exitBool = false;
    Scanner scanner = new Scanner(System.in);

    public Terminal(GameEngine gameEngine) {
        this.gameSettings = gameEngine.getGameSettings();
        this.player = gameEngine.getPlayer();
        this.encounters = gameEngine.getEncounters();
        this.gameEngine = gameEngine;

        run();
    }

    public void close(String exitString) {
        if (exitString.equalsIgnoreCase("exit")) {
            exitBool = true;
        }
    }

    private void run() {
        var controller = new GameController(encounters);
        System.out.println("I will run in a terminal window until user types 'exit'.");

        // Closes current Terminal process on user entering 'exit'.
        while (!exitBool) {
            String output;

            if (controller.getCurrentEncounter() != null) {
                output = controller.getCurrentEncounter().run();
            } else {
                System.out.println("Game has finished. Please type 'exit'.");
                output = scanner.nextLine();
            }

            close(output);
            if (output.equalsIgnoreCase("defeated")) {
                controller.progressToNextEncounter(output);
            }
        }

        System.out.println("Game is exiting...");
    }
}
