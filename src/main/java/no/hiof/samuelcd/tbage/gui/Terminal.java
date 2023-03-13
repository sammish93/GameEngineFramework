package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Closeable;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.player.Player;

import java.util.Scanner;

public class Terminal extends GameInterface implements Closeable<String> {


    boolean exitBool = false;
    Scanner scanner = new Scanner(System.in);

    public Terminal(GameSettings gameSettings, Player player, Encounters encounters) {
        this.gameSettings = gameSettings;
        this.player = player;
        this.encounters = encounters;

        System.out.println(this.gameSettings.getMessage());

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
}
