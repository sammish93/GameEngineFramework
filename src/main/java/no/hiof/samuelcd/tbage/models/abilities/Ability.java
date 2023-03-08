package no.hiof.samuelcd.tbage.models.abilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.File;
import java.io.IOException;

public class Ability implements Useable, JsonExternalisable {

    private String name;
    private boolean onEncounterStart;
    private boolean onEncounterFinish;
    private double abilityProbabilityPerTurn;


    private Ability(String name, boolean onEncounterStart, boolean onEncounterFinish, double abilityProbabilityPerTurn) {
        this.name = name;
        this.onEncounterStart = onEncounterStart;
        this.onEncounterFinish = onEncounterFinish;
        this.abilityProbabilityPerTurn = abilityProbabilityPerTurn;
    }

    public static Ability create() {
        return new Ability(null, false, false, 0);
    }

    public static Ability create(String name) {
        return new Ability(name, false, false, 0.2);
    }

    public static Ability create(String name, boolean onEncounterStart, boolean onEncounterFinish, double abilityProbabilityPerTurn) {
        return new Ability(name, onEncounterStart, onEncounterFinish, abilityProbabilityPerTurn);
    }

    public void onUse() {
        // Ability behaviour.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnEncounterStart() {
        return onEncounterStart;
    }

    public void setOnEncounterStart(boolean onEncounterStart) {
        this.onEncounterStart = onEncounterStart;
    }

    public boolean isOnEncounterFinish() {
        return onEncounterFinish;
    }

    public void setOnEncounterFinish(boolean onEncounterFinish) {
        this.onEncounterFinish = onEncounterFinish;
    }

    public double getAbilityProbabilityPerTurn() {
        return abilityProbabilityPerTurn;
    }

    public void setAbilityProbabilityPerTurn(double abilityProbabilityPerTurn) {
        this.abilityProbabilityPerTurn = abilityProbabilityPerTurn;
    }

    @Override
    public void writeToJson(File file) throws IOException {
        ObjectMapper om = new ObjectMapper();
        boolean fileExists = file.exists();

        if (!fileExists) {
            fileExists = file.createNewFile();
        }

        if (fileExists) {
            om.writeValue(file, this);
        }

        // Jackson auto-closes the stream and mapper.
    }

    @Override
    public void readFromJson(File file) throws IOException{
        ObjectMapper om = new ObjectMapper();
        Ability ability = om.readValue(file, Ability.class);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
