package no.hiof.samuelcd.tbage.models.encounters;

import java.util.LinkedHashMap;

public class RandomEncounters extends Encounters{

    LinkedHashMap<String, Encounter> encounters;
    public RandomEncounters(int nrOfEncounters, EncounterPool encounterPool) {
        encounters = new LinkedHashMap<>(nrOfEncounters);
        randomiseEncounters(nrOfEncounters, encounterPool);
    }

    private void randomiseEncounters(int nrOfEncounters, EncounterPool encounterPool) {
        // Takes n number of encounters from the given encounterPool and places them into a LinkedHashMap.
    }

}
