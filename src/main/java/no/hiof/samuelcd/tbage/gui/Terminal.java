package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Closeable;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.player.Player;

import java.util.Scanner;

public class Terminal implements Closeable<String> {

    private static GameSettings gameSettings;
    public static Player player;
    private static Encounters encounters;
    boolean exitBool = false;
    Scanner scanner = new Scanner(System.in);

    public Terminal(GameSettings gameSettings, Player player, Encounters encounters) {
        Terminal.gameSettings = gameSettings;
        Terminal.player = player;
        Terminal.encounters = encounters;

        System.out.println(Terminal.gameSettings.getMessage());

        // Closes current Terminal process on user entering 'exit'.

        while (!exitBool) {
            close("exit");
        }

        // Shouldn't be necessary with Java Garbage Collector.
        //scanner.close();
    }

    public void close(String exitString) {
        String word = scanner.nextLine();
        if (word.equalsIgnoreCase(exitString)) {
            exitBool = true;
        }
    }

    public static void main(String[] args) {
        new Terminal(gameSettings, player, encounters);
    }
}
