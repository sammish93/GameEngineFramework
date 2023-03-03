package no.hiof.samuelcd.tbage.models.encounters;

import java.util.LinkedHashMap;

public class FixedEncounters extends Encounters {

    LinkedHashMap<String, EncounterVertex> encounters;
    public FixedEncounters() {
        encounters = new LinkedHashMap<>();
    }

    private void linkEncounters() {
        // Takes all vertexes and edges and links them together into a single structure.
    }

    public void addVertex(String encounter) {
    }

    public void addEdge(String encounterFrom, String encounterTo, String event) {
        // Encounter x progresses to encounter y if event is fulfilled.
    }

}
