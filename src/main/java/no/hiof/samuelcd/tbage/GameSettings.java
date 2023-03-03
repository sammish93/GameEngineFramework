package no.hiof.samuelcd.tbage;

import no.hiof.samuelcd.tbage.enums.EncounterPattern;

public class GameSettings {

    private EncounterPattern encounterPattern= EncounterPattern.RANDOM;
    private String message = "I will run in a terminal window until user types 'exit'.";
    private String buttonMessage = "I will run in a Swing window until user presses ESC.";

    public GameSettings() {

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
}
