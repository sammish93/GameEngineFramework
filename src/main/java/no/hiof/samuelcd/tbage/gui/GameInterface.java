package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.player.Player;
import no.hiof.samuelcd.tbage.tools.EncounterTraversalController;

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
