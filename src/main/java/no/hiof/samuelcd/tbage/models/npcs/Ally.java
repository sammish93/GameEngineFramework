package no.hiof.samuelcd.tbage.models.npcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.models.abilities.Ability;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class Ally extends NonPlayableCharacter {

    private Ally(String name, TreeMap<String, Ability> abilities) {
        super(name, abilities);
    }

    public static Ally create() {
        return new Ally(null, null);
    }

    public static Ally create(String name, TreeMap<String, Ability> abilities) {
        return new Ally(name, abilities);
    }

    @Override
    public void processAbilities() {
        // Iterates through abilityPool to find which events (onEncounterStart and onEncounterFinish) are TRUE.
    }

    @Override
    public void processItems() {
        // Determines if items are available from a specific Ally.
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
        Ally ally = om.readValue(file, Ally.class);

        // Specific deserialisation here.
    }

    @Override
    public String toString() {
        return super.toString();
    }
}