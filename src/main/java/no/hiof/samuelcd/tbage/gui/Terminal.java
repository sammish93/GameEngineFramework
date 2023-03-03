package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.models.encounters.EncounterPool;

import java.util.Scanner;

public class Terminal {

    private static GameSettings gameSettings;
    private static EncounterPool encounterPool;
    boolean exitBool = false;

    public Terminal(GameSettings gameSettings, EncounterPool encounterPool) {
        Terminal.gameSettings = gameSettings;
        Terminal.encounterPool = encounterPool;
        System.out.println(Terminal.gameSettings.getMessage());

        // Closes current Terminal process on user entering 'exit'.
        Scanner scanner = new Scanner(System.in);

        while (!exitBool) {
            String word = scanner.nextLine();
            if (word.equalsIgnoreCase("exit")) {
                exitBool = true;
            }
        }
    }

    public static void main(String[] args) {
        new Terminal(gameSettings, encounterPool);
    }
}
