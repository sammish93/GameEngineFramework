package no.hiof.samuelcd.tbage;

import no.hiof.samuelcd.tbage.enums.EncounterPattern;

import java.io.*;

public class GameSettings implements Serializable {

    private EncounterPattern encounterPattern = EncounterPattern.RANDOM;
    private int encounterModifier = 0;
    private int encounterLength = 10;
    private int fontSize = 12;
    private String message = "I will run in a terminal window until user types 'exit'.";
    private String buttonMessage = "I will run in a Swing window until user presses ESC.";


    private GameSettings() {

    }

    public static GameSettings create() {
        return new GameSettings();
    }

    // Getters and setters.
    public EncounterPattern getEncounterPattern() {
        return encounterPattern;
    }

    protected void setEncounterPatternToFixed() {
        this.encounterPattern = EncounterPattern.FIXED;
    }
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

    public int getEncounterLength() {
        return encounterLength;
    }

    public void setEncounterLength(int encounterLength) {
        this.encounterLength = encounterLength;
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

    public void save(String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);
    }

    public static GameSettings load(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (GameSettings) objectInputStream.readObject();
    }
}
