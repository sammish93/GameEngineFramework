package sammish93.tbage;

import sammish93.tbage.enums.EncounterPattern;
import sammish93.tbage.exceptions.InvalidValueException;

import java.awt.*;
import java.io.*;
import java.nio.file.InvalidPathException;

/**
 * A class intended to hold preferences and settings stipulating how the game renders.
 */
public class GameSettings implements Serializable {

    private EncounterPattern encounterPattern = EncounterPattern.RANDOM;
    private int encounterModifier = 0;
    private Font fontOutput;
    private Font fontInput;
    private Font fontGeneral;
    private boolean isOutputSeparatedByNewLine;
    private boolean isFontAnimated;
    private int fontAnimationSpeed;
    private int windowWidth;
    private int windowHeight;
    private String windowTitle;


    private GameSettings() {
        windowHeight = 600;
        windowWidth = 800;
        windowTitle = "TBAGE - Text-Based Adventure Game Engine";
        fontOutput = new Font("Century", Font.PLAIN, 14);
        fontInput = new Font("Lucida Handwriting Italic", Font.PLAIN, 14);
        fontGeneral = new Font("Century Gothic", Font.BOLD, 14);
        isFontAnimated = true;
        fontAnimationSpeed = 10;
        isOutputSeparatedByNewLine = true;
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

    public int getEncounterModifier() {
        return encounterModifier;
    }

    public void setEncounterModifier(int encounterModifier) {
        this.encounterModifier = encounterModifier;
    }

    /**
     *
     * @return Returns a Font object representing the font size, style, and font face used by the output text area
     * when the game is run using Java Swing.
     */
    public Font getFontOutput() {
        return fontOutput;
    }

    /**
     *
     * @param font Sets a Font object to represent the font size, style, and font face used by the output
     *                   text area when the game is run using Java Swing.
     */
    public void setFontOutput(Font font) {
        this.fontOutput = font;
    }

    /**
     *
     * @return Returns a Font object representing the font size, style, and font face used by the input text field
     * when the game is run using Java Swing.
     */
    public Font getFontInput() {
        return fontInput;
    }

    /**
     *
     * @param font Sets a Font object to represent the font size, style, and font face used by the input text field
     *             ame is run using Java Swing.
     */
    public void setFontInput(Font font) {
        this.fontInput = font;
    }

    /**
     *
     * @return Returns a Font object representing the font size, style, and font face used by the interface
     * buttons and labels when the game is run using Java Swing.
     */
    public Font getFontGeneral() {
        return fontGeneral;
    }

    /**
     *
     * @param font Sets a Font object to represent the font size, style, and font face used by the interface
     *             buttons and labels when the game is run using Java Swing.
     */
    public void setFontGeneral(Font font) {
        this.fontGeneral = font;
    }

    /**
     *
     * @return Returns a boolean value representing if the font is printed to the output (either terminal
     * window or Swing text area) with or without a delay after each character.
     */
    public boolean isFontAnimated() {
        return isFontAnimated;
    }

    /**
     *
     * @param fontAnimated Sets the boolean value to represent if the font is printed to the output (either the
     *                     terminal window or Swing text area) with or without a delay after each character.
     */
    public void setFontAnimated(boolean fontAnimated) {
        isFontAnimated = fontAnimated;
    }

    /**
     *
     * @return Returns a positive integer value representing the pause (in milliseconds) between each character
     * being printed to the output (either the terminal window or Swing text area).
     */
    public int getFontAnimationSpeed() {
        return fontAnimationSpeed;
    }

    /**
     *
     * @param fontAnimationSpeed Sets a positive integer value to represent the pause (in milliseconds) between
     *                           each character being printed to the output (either the terminal window or
     *                           Swing text area).
     *                           NOTE: In the case that a value of 0 is provided then font animation will be
     *                           turned off.
     * @throws InvalidValueException Thrown when a negative integer value is provided.
     */
    public void setFontAnimationSpeed(int fontAnimationSpeed) throws InvalidValueException {
        if (fontAnimationSpeed > 0) {
            this.fontAnimationSpeed = fontAnimationSpeed;
        } else if (fontAnimationSpeed == 0) {
            isFontAnimated = false;
        } else {
            throw new InvalidValueException("Value " + fontAnimationSpeed + " must be a positive integer value");
        }

    }

    /**
     *
     * @return Returns a boolean value representing if each line printed to the output (either the terminal window
     * or Swing text area) is separated by a new line or not.
     */
    public boolean isOutputSeparatedByNewLine() {
        return isOutputSeparatedByNewLine;
    }

    /**
     *
     * @param outputSeparatedByNewLine Sets a boolean value to represent if each line printed to the output (either the terminal window
     *      * or Swing text area) is separated by a new line or not.
     */
    public void setOutputSeparatedByNewLine(boolean outputSeparatedByNewLine) {
        isOutputSeparatedByNewLine = outputSeparatedByNewLine;
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
