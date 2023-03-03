package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.models.encounters.EncounterPool;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;

import java.util.Scanner;

public class Terminal {

    private static GameSettings gameSettings;
    private static Encounters encounters;
    boolean exitBool = false;

    public Terminal(GameSettings gameSettings, Encounters encounters) {
        Terminal.gameSettings = gameSettings;
        Terminal.encounters = encounters;
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
        new Terminal(gameSettings, encounters);
    }
}
