package no.hiof.samuelcd.tbage.models.encounters;

import java.util.Objects;
import java.util.TreeMap;

public class EncounterPool {

    TreeMap<String, Encounter> encounterMap;

    private EncounterPool(TreeMap<String, Encounter> encounterMap) {
        this.encounterMap = Objects.requireNonNullElseGet(encounterMap, TreeMap::new);
    }

    public static EncounterPool create() {
        return new EncounterPool(null);
    }

    public static EncounterPool create(TreeMap<String, Encounter> encounterMap) {
        return new EncounterPool(encounterMap);
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
