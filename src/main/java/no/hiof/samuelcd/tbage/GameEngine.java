package no.hiof.samuelcd.tbage;

import no.hiof.samuelcd.tbage.enums.GamePlatform;
import no.hiof.samuelcd.tbage.gui.Swing;
import no.hiof.samuelcd.tbage.gui.Terminal;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.encounters.RandomEncounters;

import java.util.Objects;

public class GameEngine {

    private GamePlatform platform = GamePlatform.TERMINAL;
    private GameSettings gameSettings;
    private Encounters encounters;

    private GameEngine(GameSettings gameSettings, Encounters encounters) {
        this.gameSettings = Objects.requireNonNullElseGet(gameSettings, GameSettings::create);

        if (encounters instanceof FixedEncounters || encounters instanceof RandomEncounters) {
            this.encounters = encounters;
        } else {
            // Temporary - will have a default pool of encounters.
            this.encounters = null;
        }
    }

    public static GameEngine create() {
        return new GameEngine(null, null);
    }

    public static GameEngine create(GameSettings gameSettings) {
        return new GameEngine(gameSettings, null);
    }

    public static GameEngine create(GameSettings gameSettings, Encounters encounters) {
        return new GameEngine(gameSettings, encounters);
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
