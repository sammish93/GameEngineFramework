package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.tools.StringParser;

import java.util.LinkedHashMap;
import java.util.Set;

public class FixedEncounters extends Encounters {

    LinkedHashMap<String, Encounter> encounters;
    private Encounter initialEncounter;

    private FixedEncounters() {
        encounters = new LinkedHashMap<>();
    }

    public static FixedEncounters create() {
        return new FixedEncounters();
    }

    private void linkEncounters() {
        // Takes all vertexes and edges and links them together into a single structure.
    }

    public void addEncounter(Encounter encounter) {
        initialEncounter = encounter;
        encounters.put(encounter.getName(), encounter);
        // Adds an initial encounter vertex.
    }

    public void addEncounter(Encounter encounterFrom, Encounter encounterTo, String event) {
        encounterFrom.setNavigationOption(event, encounterTo.getName());
        encounters.put(encounterTo.getName(), encounterTo);
        StringParser.addNoun(event);
        // Encounter x progresses to encounter y if event is triggered.
    }

    public void addEncounter(Encounter encounterFrom, Encounter encounterTo) {
        String defaultEvent = "defeated";
        encounterFrom.setNavigationOption(defaultEvent, encounterTo.getName());
        encounters.put(encounterTo.getName(), encounterTo);
        StringParser.addNoun(defaultEvent);
        // Encounter x progresses to encounter y if event is triggered.
    }

    public Encounter getInitialEncounter() {
        return initialEncounter;
    }

    public Encounter getEncounter(String encounterName) {
        return encounters.get(encounterName);
    }

    public LinkedHashMap<String, Encounter> getEncounters() {
        return encounters;
    }
}
