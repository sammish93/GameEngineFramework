package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.player.Player;
import no.hiof.samuelcd.tbage.tools.EncounterTraversalController;

public abstract class GameInterface {

    GameSettings gameSettings;
    Player player;
    Encounters encounters;
    GameEngine gameEngine;
    EncounterTraversalController encounterTraversalController;

    GameInterface(GameEngine gameEngine) {
        gameSettings = gameEngine.getGameSettings();
        player = gameEngine.getPlayer();
        encounters = gameEngine.getEncounters();
        this.gameEngine = gameEngine;

        encounterTraversalController = new EncounterTraversalController(encounters);
    }

    public EncounterTraversalController getEncounterController() {
        return encounterTraversalController;
    }
}
