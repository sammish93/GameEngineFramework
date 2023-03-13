import no.hiof.samuelcd.tbage.GameEngine;

public class Main {
    public static void main(String[] args) {
        var game = GameEngine.create();
        game.setPlatformToSwing();
        game.run();
    }
}
