import no.hiof.samuelcd.tbage.*;

public class Main {
    public static void main(String[] args) {


        // Running the game in a terminal.
        var game = GameEngine.create();
        game.run();

        // Running the game in Swing.
        var game2 = GameEngine.create();
        game2.setPlatformToSwing();
        game2.run();

        var settings = new GameSettings();
        settings.setButtonMessage("this swing window is overridden");
        settings.setMessage("this terminal window is overridden");

        // Running the game in a terminal.
        var game3 = GameEngine.create(settings);
        game3.run();

        // Running the game in Swing.
        var game4 = GameEngine.create(settings);
        game4.setPlatformToSwing();
        game4.run();


    }
}