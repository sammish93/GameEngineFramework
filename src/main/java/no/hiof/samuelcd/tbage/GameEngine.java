package no.hiof.samuelcd.tbage;

import no.hiof.samuelcd.tbage.enums.GamePlatform;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.exceptions.InventoryFullException;
import no.hiof.samuelcd.tbage.gui.GameInterface;
import no.hiof.samuelcd.tbage.gui.Swing;
import no.hiof.samuelcd.tbage.gui.Terminal;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.encounters.RandomEncounters;
import no.hiof.samuelcd.tbage.models.player.Player;
import no.hiof.samuelcd.tbage.tools.StringParser;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class GameEngine implements Serializable {

    private GamePlatform platform = GamePlatform.TERMINAL;
    private GameSettings gameSettings;
    private Player player;
    private Encounters encounters;
    public static Scanner scanner  = new Scanner(System.in);


    private GameEngine(GameSettings gameSettings, Player player, Encounters encounters) throws InvalidValueException {
        this.gameSettings = Objects.requireNonNullElseGet(gameSettings, GameSettings::create);
        if (player != null) {
            this.player = player;
        } else {
            this.player = Player.create();
        }

        if (encounters instanceof FixedEncounters || encounters instanceof RandomEncounters) {
            this.encounters = encounters;
            if (encounters instanceof FixedEncounters) {
                this.gameSettings.setEncounterPatternToFixed();
            } else {
                this.gameSettings.setEncounterPatternToRandom();
            }
        } else {
            // Temporary - will have a default pool of encounters.
            this.encounters = null;
        }

        addDefaultParserParameters();
    }

    public static GameEngine create() throws InvalidValueException {
        return new GameEngine(null, null, null);
    }

    public static GameEngine create(GameSettings gameSettings) throws InvalidValueException {
        return new GameEngine(gameSettings, null, null);
    }

    public static GameEngine create(GameSettings gameSettings, Encounters encounters) throws InvalidValueException {
        return new GameEngine(gameSettings, null, encounters);
    }

    public static GameEngine create(GameSettings gameSettings, Player player) throws InvalidValueException {
        return new GameEngine(gameSettings, player, null);
    }
    public static GameEngine create(Player player) throws InvalidValueException {
        return new GameEngine(null, player, null);
    }

    public static GameEngine create(Encounters encounters) throws InvalidValueException {
        return new GameEngine(null, null, encounters);
    }

    public static GameEngine create(Player player, Encounters encounters) throws InvalidValueException {
        return new GameEngine(null, player, encounters);
    }
    public static GameEngine create(GameSettings gameSettings, Player player, Encounters encounters) throws InvalidValueException {
        return new GameEngine(gameSettings, player, encounters);
    }


    public GameInterface run() throws InventoryFullException {
        try {
            if (platform.equals(GamePlatform.TERMINAL)) {
                return new Terminal(this);
            } else if (platform.equals(GamePlatform.SWING)) {
                return new Swing(this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Encounters getEncounters() {
        return encounters;
    }

    public void setEncounters(Encounters encounters) {
        this.encounters = encounters;
    }

    public void printMessage(String s) {
        System.out.println(s);
    }

    public void printMessageFormatted(String s, Object ... args) {
        System.out.printf(s, args);
    }

    private static void addDefaultParserParameters() throws InvalidValueException {
        ArrayList<String> defaultCommands = new ArrayList<String>();
        defaultCommands.add("exit");
        defaultCommands.add("options");
        defaultCommands.add("help");
        defaultCommands.add("back");
        defaultCommands.add("defeated");
        defaultCommands.add("playerdeath");
        defaultCommands.add("progress");
        defaultCommands.add("status");
        defaultCommands.add("attack");
        defaultCommands.add("investigate");
        defaultCommands.add("interact");
        defaultCommands.add("inventory");
        defaultCommands.add("use");
        // ***** FOR DEBUGGING PURPOSES *****
        defaultCommands.add("skip");

        ArrayList<String> defaultNouns = new ArrayList<String>();
        defaultNouns.add("player");
        defaultNouns.add("me");
        defaultNouns.add("myself");
        defaultNouns.add("self");


        StringParser.setCommands(defaultCommands);
    }

    public void removeAllDefaultParsingParameters() throws InvalidValueException {
        StringParser.setCommands(new ArrayList<>());
        StringParser.setNouns(new ArrayList<>());
        StringParser.setVerbs(new ArrayList<>());
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             gameEngine.save("src/fileName");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws InvalidPathException Arises if the path contains characters other than the English alphabet,
     * underscores, and forward slashes.
     */
    public void save(String path) throws IOException {
        boolean isOnlyAlphaNumericAndUnderscores = path.matches("^[a-zA-Z0-9_/]*$");

        if (isOnlyAlphaNumericAndUnderscores) {
            FileOutputStream fileOutputStream = new FileOutputStream(path + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this);
        } else {
            throw new InvalidPathException(path, "The path isn't recognised as a valid file path.");
        }

    }

    /**
     * Serialises to a local path. The file type extension is decided by the developer.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             gameEngine.save("src/fileName", "sav");
     * @param fileExtension The file type extension the developer wishes to save the file as.
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws InvalidPathException Arises if the path contains characters other than the English alphabet,
     * underscores, and forward slashes.
     */
    public void save(String path, String fileExtension) throws IOException {
        boolean isOnlyAlphaNumericAndUnderscores = path.matches("^[a-zA-Z0-9_/]*$");

        if (isOnlyAlphaNumericAndUnderscores) {
            FileOutputStream fileOutputStream = new FileOutputStream(path + "." + fileExtension);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this);
        } else {
            throw new InvalidPathException(path, "The path isn't recognised as a valid file path.");
        }

    }

    /**
     * Serialises to a local path. The file type extension is only required when the extension is something other
     * than '.ser'.
     * @param path The location that the file is located at. This can either be with or without the file extension.
     *             Examples:
     *             var gameEngine = GameEngine.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var gameEngine = GameEngine.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static GameEngine load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (GameEngine) objectInputStream.readObject();
    }
}
