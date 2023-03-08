package no.hiof.samuelcd.tbage.models.encounters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CombatEncounter extends Encounter {

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
        CombatEncounter combatEncounter = om.readValue(file, CombatEncounter.class);

        // Specific deserialisation here.
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
