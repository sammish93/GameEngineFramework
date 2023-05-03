package sammish93.tbage;

import sammish93.tbage.enums.EncounterPattern;

import java.io.*;
import java.nio.file.InvalidPathException;

/**
 * A class intended to hold preferences and settings stipulating how the game renders.
 */
public class GameSettings implements Serializable {

    private EncounterPattern encounterPattern = EncounterPattern.RANDOM;
    private int encounterModifier = 0;
    private int fontSize = 12;
    private int windowWidth;
    private int windowHeight;
    private String windowTitle;
    private String message = "I will run in a terminal window until user types 'exit'.";
    private String buttonMessage = "I will run in a Swing window until user presses ESC.";


    private GameSettings() {
        windowHeight = 600;
        windowWidth = 800;
        windowTitle = "TBAGE - Text-Based Adventure Game Engine";
    }

    /**
     *
     * @return Returns a new instance of GameSettings.
     */
    public static GameSettings create() {
        return new GameSettings();
    }

    // Getters and setters.
    public EncounterPattern getEncounterPattern() {
        return encounterPattern;
    }

    /**
     * Used by the GameEngine class to control how encounter traversal is handled.
     */
    protected void setEncounterPatternToFixed() {
        this.encounterPattern = EncounterPattern.FIXED;
    }

    /**
     * Used by the GameEngine class to control how encounter traversal is handled.
     */
    protected void setEncounterPatternToRandom() {
        this.encounterPattern = EncounterPattern.RANDOM;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getButtonMessage() {
        return buttonMessage;
    }

    public void setButtonMessage(String buttonMessage) {
        this.buttonMessage = buttonMessage;
    }

    public int getEncounterModifier() {
        return encounterModifier;
    }

    public void setEncounterModifier(int encounterModifier) {
        this.encounterModifier = encounterModifier;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     *
     * @return Returns an int array with 2 values. The value at index 0 is the width, and the value at index 1
     * is the height.
     */
    public int[] getWindowResolution() {
        int[] resolution = new int[2];
        resolution[0] = windowWidth;
        resolution[1] = windowHeight;
        return resolution;
    }

    /**
     *
     * @param windowWidth A positive integer used to provide the Java Swing interface with a width (in pixels).
     * @param windowHeight A positive integer used to provide the Java Swing interface with a height (in pixels).
     */
    public void setWindowResolution(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    /**
     *
     * @return Returns a string of the title of the game. The title will appear on the top of the window if the
     * game is run in a Java Swing window.
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     *
     * @param windowTitle Sets a string for the title of the game. The title will appear on the top of the window
     *                    if the game is run in a Java Swing window.
     */
    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             gameSettings.save("src/fileName");
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
     *             gameSettings.save("src/fileName", "sav");
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
     *             var gameSettings = GameSettings.save("src/fileNameWithSavExtension.sav");
     *             or
     *             var gameSettings = GameSettings.save("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static GameSettings load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (GameSettings) objectInputStream.readObject();
    }
}
