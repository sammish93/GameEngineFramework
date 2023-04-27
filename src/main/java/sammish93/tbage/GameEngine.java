package sammish93.tbage;

import sammish93.tbage.enums.GamePlatform;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.gui.GameInterface;
import sammish93.tbage.gui.Swing;
import sammish93.tbage.gui.Terminal;
import sammish93.tbage.models.Encounters;
import sammish93.tbage.models.FixedEncounters;
import sammish93.tbage.models.RandomEncounters;
import sammish93.tbage.models.Player;
import sammish93.tbage.tools.StringParser;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * A class intended to hold all required elements of an executable game. The GameEngine class is comprised of three
 * main dependencies - GameSettings, Player, and Encounters.
 * GameSettings:
 *   - Handles user preferences relating to the game, as well as the interface.
 * Player:
 *   - Handles stats and inventory system. Inventory can hold multiple Item objects.
 * Encounters:
 *   - Either fixed or random. Holds several Encounter classes. Encounter classes are either Combat or NonCombat,
 *   and can hold multiple NonPlayerCharacter objects (can be either Ally or Enemy), which in turn hold their own
 *   Item objects, can use multiple Ability objects, and have their own stats and behaviours when interacted with.
 */

public class GameEngine implements Serializable {

    private GamePlatform platform = GamePlatform.TERMINAL;
    private GameSettings gameSettings;
    private Player player;
    private Encounters encounters;
    /**
     * A singleton Scanner object that is used to parse text entered via user input at runtime.
     */
    public static Scanner scanner  = new Scanner(System.in);


    private GameEngine(GameSettings gameSettings, Player player, Encounters encounters)
            throws InvalidValueException {
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

    /**
     *
     * @return Returns a new instance of a GameEngine class with default GameSettings, Player, and Encounters
     * objects being supplied.
     * @throws InvalidValueException
     */
    public static GameEngine create() throws InvalidValueException {
        return new GameEngine(null, null, null);
    }

    /**
     *
     * @param gameSettings The instance returned contains the supplied GameSettings instance provided as a parameter.
     * @return Returns a new instance of a GameEngine class with default Player, and Encounters objects
     * being supplied.
     * @throws InvalidValueException
     */
    public static GameEngine create(GameSettings gameSettings) throws InvalidValueException {
        return new GameEngine(gameSettings, null, null);
    }

    /**
     *
     * @param gameSettings The instance returned contains the supplied GameSettings instance provided as a parameter.
     * @param encounters The instance returned contains the supplied Encounters instance provided as a parameter.
     *                   The Encounters object can be either an instance of RandomEncounters or FixedEncounters.
     * @return Returns a new instance of a GameEngine class with a default Player object being supplied.
     * @throws InvalidValueException
     */
    public static GameEngine create(GameSettings gameSettings, Encounters encounters) throws InvalidValueException {
        return new GameEngine(gameSettings, null, encounters);
    }

    /**
     *
     * @param gameSettings The instance returned contains the supplied GameSettings instance provided as a parameter.
     * @param player The instance returned contains the supplied Player instance provided as a parameter.
     * @return Returns a new instance of a GameEngine class with a default Encounters object being supplied.
     * @throws InvalidValueException
     */
    public static GameEngine create(GameSettings gameSettings, Player player) throws InvalidValueException {
        return new GameEngine(gameSettings, player, null);
    }

    /**
     *
     * @param player The instance returned contains the supplied Player instance provided as a parameter.
     * @return Returns a new instance of a GameEngine class with default GameSettings and Encounters objects
     * being supplied.
     * @throws InvalidValueException
     */
    public static GameEngine create(Player player) throws InvalidValueException {
        return new GameEngine(null, player, null);
    }

    /**
     *
     * @param encounters The instance returned contains the supplied Encounters instance provided as a parameter.
     *                   The Encounters object can be either an instance of RandomEncounters or FixedEncounters.
     * @return Returns a new instance of a GameEngine class with default GameSettings and Player objects
     * being supplied.
     * @throws InvalidValueException
     */
    public static GameEngine create(Encounters encounters) throws InvalidValueException {
        return new GameEngine(null, null, encounters);
    }

    /**
     *
     * @param player The instance returned contains the supplied Player instance provided as a parameter.
     * @param encounters The instance returned contains the supplied Encounters instance provided as a parameter.
     *                   The Encounters object can be either an instance of RandomEncounters or FixedEncounters.
     * @return Returns a new instance of a GameEngine class with a default GameSettings object being supplied.
     * @throws InvalidValueException
     */
    public static GameEngine create(Player player, Encounters encounters) throws InvalidValueException {
        return new GameEngine(null, player, encounters);
    }

    /**
     *
     * @param gameSettings The instance returned contains the supplied GameSettings instance provided as a parameter.
     * @param player The instance returned contains the supplied Player instance provided as a parameter.
     * @param encounters The instance returned contains the supplied Encounters instance provided as a parameter.
     *                   The Encounters object can be either an instance of RandomEncounters or FixedEncounters.
     * @return Returns a new instance of a GameEngine class.
     * @throws InvalidValueException
     */
    public static GameEngine create(GameSettings gameSettings, Player player, Encounters encounters)
            throws InvalidValueException {
        return new GameEngine(gameSettings, player, encounters);
    }


    /**
     *
     * @return Returns a new instance of either a Terminal or Swing object, which in turn calls their own run()
     * method which executes and creates an interface that a player can then interact with.
     * @throws InventoryFullException Can be thrown when the player purchases a new item by interacting and
     * trading with an ally, or by gaining an item from defeating an enemy.
     */
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

    /**
     * Sets the interface to a Java Swing window.
     */
    public void setPlatformToSwing() {
        platform = GamePlatform.SWING;
    }

    /**
     * Sets the interface to a terminal window.
     */
    public void setPlatformToTerminal() {
        platform = GamePlatform.TERMINAL;
    }

    /**
     *
     * @return Returns a GamePlatform of enum type.
     */
    public GamePlatform getPlatform() {
        return platform;
    }

    /**
     *
     * @return Returns the current instance of GameSettings that is used by this GameEngine instance.
     */
    public GameSettings getGameSettings() {
        return gameSettings;
    }

    /**
     *
     * @param gameSettings Sets a new instance of GameSettings to be used by this GameEngine instance.
     */
    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    /**
     *
     * @return Returns the current instance of Player that is used by this GameEngine instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @param player Sets a new instance of Player to be used by this GameEngine instance.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *
     * @return Returns the current instance of Encounters that is used by this GameEngine instance.
     */
    public Encounters getEncounters() {
        return encounters;
    }

    /**
     *
     * @param encounters Sets a new instance of Encounters to be used by this GameEngine instance.
     */
    public void setEncounters(Encounters encounters) {
        this.encounters = encounters;
    }

    /**
     * Prints a formatted String to the game interface.
     * Example:
     *   gameEngine.printMessageFormatted("%-15s %s\n", "Help",
     *                                    "Prints a list of commands that the player can enter.");
     *   gameEngine.printMessageFormatted("%-15s %s\n", "Inventory",
     *                                    "Lists the items and gold a player currently has in their inventory.");
     *   will print a message that looks like this:
     *   Help           Prints a list of commands that the player can enter.
     *   Inventory      Lists the items and gold a player currently has in their inventory.
     *
     * @see PrintStream#println(String)
     * @param string The String to be printed.
     */
    public void printMessage(String string) {
        System.out.println(string);
    }

    /**
     * Prints a formatted String to the game interface.
     * Example:
     *   gameEngine.printMessageFormatted("%-15s %s\n", "Help",
     *                                    "Prints a list of commands that the player can enter.");
     *   gameEngine.printMessageFormatted("%-15s %s\n", "Inventory",
     *                                    "Lists the items and gold a player currently has in their inventory.");
     *   will print a message that looks like this:
     *   Help           Prints a list of commands that the player can enter.
     *   Inventory      Lists the items and gold a player currently has in their inventory.
     *
     * @see PrintStream#printf(String, Object...)
     * @param string A format string as described in Format string syntax.
     * @param args Arguments referenced by the format specifiers in the format string. If there are more arguments
     *             than format specifiers, the extra arguments are ignored. The number of arguments is variable
     *             and may be zero. The maximum number of arguments is limited by the maximum dimension of a Java
     *             array as defined by The Java Virtual Machine Specification. The behaviour on a null argument
     *             depends on the conversion.
     */
    public void printMessageFormatted(String string, Object ... args) {
        System.out.printf(string, args);
    }

    /**
     * Populates the game engine with the default commands usable during runtime.
     * Some commands are useable by the player, such as 'help', and 'attack', whereas some are used to control
     * traversal and encounter behaviours such as 'playerdeath', and 'defeated'.
     * Default nouns are also populated relating to the player. This is not currently in use during V1.0 of the
     * framework.
     * @throws InvalidValueException
     */
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
        // defaultCommands.add("skip");

        ArrayList<String> defaultNouns = new ArrayList<String>();
        defaultNouns.add("player");
        defaultNouns.add("me");
        defaultNouns.add("myself");
        defaultNouns.add("self");


        StringParser.setCommands(defaultCommands);
    }

    /**
     * Clears the library of commands, verbs, and nouns. Should be used with caution, especially as it removes
     * commands such as 'exit' and 'defeated' that are crucial to the running of the game.
     * It may be more prudent to remove individual commands, nouns, or verbs using the static
     * StringParser.removeCommand() method.
     *
     * @see StringParser#removeCommand(String)
     * @see StringParser#removeNoun(String)
     * @see StringParser#removeVerb(String)
     * @throws InvalidValueException
     */
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
