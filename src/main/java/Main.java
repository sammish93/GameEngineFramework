import no.hiof.samuelcd.tbage.*;
import no.hiof.samuelcd.tbage.models.encounters.CombatEncounter;
import no.hiof.samuelcd.tbage.models.feats.Feat;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        var encounter = CombatEncounter.create("ENCOUNTER_EXAMPLE");
        var feat = Feat.create("FEAT_EXAMPLE");
        encounter.addFeatToFeatChecks(feat);

        System.out.println(encounter.getName() + " has the feat: " + encounter.getFeatChecks());

        String path = "src/test.ser";
        encounter.save(path);

        var encounter2 = CombatEncounter.load(path);
        encounter2.onInitiation();
        System.out.println(encounter2.getFeatFromFeatChecks(feat.getName()).getName());

        // Running the game in a terminal.
        var game = GameEngine.create();
        game.run();

        // Running the game in Swing.
        var game2 = GameEngine.create();
        game2.setPlatformToSwing();
        game2.run();

        var settings = GameSettings.create();
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