package no.hiof.samuelcd.tbage.models.encounters;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.models.npcs.Ally;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class NonCombatEncounter extends Encounter {

    private int allyCount;
    private TreeMap<String, Ally> allies;

    private NonCombatEncounter(String name, double weightedProbability, String imagePath) {
        super(name, weightedProbability, imagePath);
    }

    public NonCombatEncounter create() {
        return new NonCombatEncounter(null, 0.5, null);
    }

    public NonCombatEncounter create(String name) {
        return new NonCombatEncounter(name, 0.5, null);
    }

    public NonCombatEncounter create(String name, double weightedProbability) {
        return new NonCombatEncounter(name, weightedProbability, null);
    }

    public TreeMap<String, Ally> getAllies() {
        return allies;
    }

    public void setAllies(TreeMap<String, Ally> allies) {
        this.allies = allies;
    }

    public Ally getAllyFromAllies(String allyName) {
        return allies.get(allyName);
    }

    public void addAllyToAllies(Ally ally) {
        allies.put(ally.getName(), ally);
    }

    public void removeAllyFromAllies(Ally ally) {
        allies.remove(ally.getName());
    }

    public void removeAllyFromAllies(String allyName) {
        allies.remove(allyName);
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
        NonCombatEncounter nonCombatEncounter = om.readValue(file, NonCombatEncounter.class);

        // Specific deserialisation here.
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
