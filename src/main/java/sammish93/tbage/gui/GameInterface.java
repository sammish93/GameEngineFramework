package sammish93.tbage.gui;

import sammish93.tbage.GameEngine;
import sammish93.tbage.GameSettings;
import sammish93.tbage.models.Encounters;
import sammish93.tbage.models.Player;
import sammish93.tbage.tools.EncounterTraversalController;

/**
 * An abstract class intended to be used to render the game in a desired interface.
 */
public abstract class GameInterface {

    private GameSettings gameSettings;
    private Player player;
    private Encounters encounters;
    private GameEngine gameEngine;
    private EncounterTraversalController encounterTraversalController;

    GameInterface(GameEngine gameEngine) {
        gameSettings = gameEngine.getGameSettings();
        player = gameEngine.getPlayer();
        encounters = gameEngine.getEncounters();
        this.gameEngine = gameEngine;

        encounterTraversalController = new EncounterTraversalController(encounters);
    }


    protected GameSettings getGameSettings() {
        return gameSettings;
    }

    protected Player getPlayer() {
        return player;
    }

    protected Encounters getEncounters() {
        return encounters;
    }

    protected GameEngine getGameEngine() {
        return gameEngine;
    }

    protected EncounterTraversalController getEncounterController() {
        return encounterTraversalController;
    }
}
