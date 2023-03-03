package no.hiof.samuelcd;

public class Main {
    public static void main(String[] args) {
        var game = new GameEngine("TERMINAL");
        game.run();


        var game2 = new GameEngine("SWING");
        game2.run();

    }
}