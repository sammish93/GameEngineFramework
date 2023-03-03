package no.hiof.samuelcd.tbage;

import no.hiof.samuelcd.tbage.enums.GamePlatform;
import no.hiof.samuelcd.tbage.gui.Swing;
import no.hiof.samuelcd.tbage.gui.Terminal;
import no.hiof.samuelcd.tbage.models.encounters.EncounterPool;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;

public class GameEngine {

    private GamePlatform platform = GamePlatform.TERMINAL;
    private GameSettings gameSettings;
    private Encounters encounters;

    public GameEngine(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public GameEngine() {
        this.gameSettings = new GameSettings();
    }

    public void run() {
        if (platform.equals(GamePlatform.TERMINAL)) {
            new Terminal(gameSettings, encounters);
        } else if (platform.equals(GamePlatform.SWING)) {
            new Swing(gameSettings, encounters);
        }
    }

    public void setPlatformToSwing() {
        platform = GamePlatform.SWING;
    }

    public void setPlatformToTerminal() {
        platform = GamePlatform.TERMINAL;
    }

    public GamePlatform getPlatform() {
        return platform;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }
}
