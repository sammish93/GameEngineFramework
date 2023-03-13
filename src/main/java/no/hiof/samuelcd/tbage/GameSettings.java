package no.hiof.samuelcd.tbage;

import no.hiof.samuelcd.tbage.enums.EncounterPattern;
import no.hiof.samuelcd.tbage.enums.GameDifficulty;
import no.hiof.samuelcd.tbage.enums.GameTheme;
import no.hiof.samuelcd.tbage.models.encounters.Encounter;

import java.io.*;

public class GameSettings implements Serializable {

    private EncounterPattern encounterPattern = EncounterPattern.RANDOM;
    private GameTheme gameTheme = GameTheme.DEFAULT;
    private GameDifficulty gameDifficulty = GameDifficulty.MEDIUM;
    private int encounterLength = 10;
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

    public void setEncounterPatternToFixed() {
        this.encounterPattern = EncounterPattern.FIXED;
    }
    public void setEncounterPatternToRandom() {
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

    public GameTheme getGameTheme() {
        return gameTheme;
    }

    public void setGameThemeToDefault() {
        this.gameTheme = GameTheme.DEFAULT;
    }

    public void setGameThemeToFantasy() {
        this.gameTheme = GameTheme.FANTASY;
    }

    public void setGameThemeToSciFi() {
        this.gameTheme = GameTheme.SCIFI;
    }

    public int getEncounterLength() {
        return encounterLength;
    }

    public void setEncounterLength(int encounterLength) {
        this.encounterLength = encounterLength;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficultyToEasy() {
        this.gameDifficulty = GameDifficulty.EASY;
    }

    public void setGameDifficultyToMedium() {
        this.gameDifficulty = GameDifficulty.MEDIUM;
    }

    public void setGameDifficultyToHard() {
        this.gameDifficulty = GameDifficulty.HARD;
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
