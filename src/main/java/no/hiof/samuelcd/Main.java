package no.hiof.samuelcd;

public class Main {
    public static void main(String[] args) {

        // Running the game in a terminal.
        var game = new GameEngine("TERMINAL");
        game.run();

        // Running the game in Swing.
        var game2 = new GameEngine("SWING");
        game2.run();

    }
}