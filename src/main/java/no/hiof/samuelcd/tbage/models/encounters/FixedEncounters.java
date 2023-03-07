package no.hiof.samuelcd.tbage.models.encounters;

import java.util.LinkedHashMap;

public class FixedEncounters extends Encounters {

    LinkedHashMap<String, EncounterVertex> encounters;
    private FixedEncounters() {
        encounters = new LinkedHashMap<>();
    }

    public static FixedEncounters create() {
        return new FixedEncounters();
    }

    private void linkEncounters() {
        // Takes all vertexes and edges and links them together into a single structure.
    }

    public void addInitialEncounter(String encounter) {
        // Adds an initial encounter vertex.
    }

    public void addEncounter(String encounterFrom, String encounterTo, String event) {
        // Encounter x progresses to encounter y if event is triggered.
    }

}
