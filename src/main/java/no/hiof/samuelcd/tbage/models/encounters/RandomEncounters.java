package no.hiof.samuelcd.tbage.models.encounters;

import java.util.LinkedHashMap;

public class RandomEncounters extends Encounters{

    LinkedHashMap<String, Encounter> encounters;
    private RandomEncounters(int nrOfEncounters, EncounterPool encounterPool) {
        encounters = new LinkedHashMap<>(nrOfEncounters);
        if (encounterPool != null) {
            randomiseEncounters(nrOfEncounters, encounterPool);
        } else {
            // Do some default behaviour here.
            randomiseEncounters(nrOfEncounters, null);
        }
    }

    public static RandomEncounters create() {
        return new RandomEncounters(10, null);
    }

    public static RandomEncounters create(int nrOfEncounters, EncounterPool encounterPool) {
        return new RandomEncounters(nrOfEncounters, encounterPool);
    }

    private void randomiseEncounters(int nrOfEncounters, EncounterPool encounterPool) {
        // Takes n number of encounters from the given encounterPool and places them into a LinkedHashMap.
    }

}
