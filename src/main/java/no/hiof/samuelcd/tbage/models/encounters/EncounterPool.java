package no.hiof.samuelcd.tbage.models.encounters;

import java.util.TreeMap;

public class EncounterPool {

    TreeMap<String, Encounter> encounterMap;
    public EncounterPool() {
        // Default encounter list.
        encounterMap = new TreeMap<>();
    }

    public EncounterPool(TreeMap<String, Encounter> encounterMap) {
        this.encounterMap = encounterMap;
    }

    public void addEncounter(Encounter encounter) {
        // Should implement behaviour if encounter already exists.
        encounterMap.put(encounter.getEncounterName(), encounter);
    }

    public void removeEncounter(String encounterName) {
        encounterMap.remove(encounterName);
    }

    public Encounter getEncounter(String encounterName) {
        return encounterMap.get(encounterName);
    }

}
