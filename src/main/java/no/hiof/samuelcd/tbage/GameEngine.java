package no.hiof.samuelcd.tbage;

import no.hiof.samuelcd.tbage.enums.GamePlatform;
import no.hiof.samuelcd.tbage.gui.Swing;
import no.hiof.samuelcd.tbage.gui.Terminal;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.encounters.RandomEncounters;
import no.hiof.samuelcd.tbage.models.player.Player;

import java.io.File;
import java.util.Objects;

public class GameEngine {

    private GamePlatform platform = GamePlatform.TERMINAL;
    private GameSettings gameSettings;
    private Player player;
    private Encounters encounters;


    private GameEngine(GameSettings gameSettings, Player player, Encounters encounters) {
        this.gameSettings = Objects.requireNonNullElseGet(gameSettings, GameSettings::create);
        this.player = Objects.requireNonNullElseGet(player, Player::create);

        if (encounters instanceof FixedEncounters || encounters instanceof RandomEncounters) {
            this.encounters = encounters;
        } else {
            // Temporary - will have a default pool of encounters.
            this.encounters = null;
        }
    }

    public static GameEngine create() {
        return new GameEngine(null, null, null);
    }

    public static GameEngine create(GameSettings gameSettings) {
        return new GameEngine(gameSettings, null, null);
    }

    public static GameEngine create(GameSettings gameSettings, Encounters encounters) {
        return new GameEngine(gameSettings, null, encounters);
    }

    public static GameEngine create(GameSettings gameSettings, Player player) {
        return new GameEngine(gameSettings, player, null);
    }
    public static GameEngine create(Player player) {
        return new GameEngine(null, player, null);
    }

    public static GameEngine create(Encounters encounters) {
        return new GameEngine(null, null, encounters);
    }

    public static GameEngine create(Player player, Encounters encounters) {
        return new GameEngine(null, player, encounters);
    }


    public void run() {
        if (platform.equals(GamePlatform.TERMINAL)) {
            new Terminal(gameSettings, player, encounters);
        } else if (platform.equals(GamePlatform.SWING)) {
            new Swing(gameSettings, player, encounters);
        }
    }

    public void exportToFile(File file) {
        // Export game to file.
    }

    public void importFromFile(File file) {
        // Imports a game from file.
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
