package no.hiof.samuelcd.tbage.models.encounters;

import java.util.LinkedHashMap;

public class RandomEncounters extends Encounters{

    LinkedHashMap<String, Encounter> encounterPool;
    LinkedHashMap<String, Double> encounterProbability;
    int nrOfEncounters;


    private RandomEncounters(int nrOfEncounters) {
        this.encounterPool = new LinkedHashMap<>();
        this.encounterProbability = new LinkedHashMap<>();
        this.nrOfEncounters = nrOfEncounters;
    }

    public static RandomEncounters create() {
        return new RandomEncounters(10);
    }

    public static RandomEncounters create(int nrOfEncounters) {
        return new RandomEncounters(nrOfEncounters);
    }

    private void randomiseEncounters(int nrOfEncounters) {
        // Takes n number of encounters from the given encounterPool and places them into a LinkedHashMap.
    }

    public void addEncounter(Encounter encounter, double probability) {
        encounterPool.put(encounter.getName(), encounter);
        encounterProbability.put(encounter.getName(), probability);
    }

    public void removeEncounter(String encounterName) {
        encounterPool.remove(encounterName);
        encounterProbability.remove(encounterName);
    }

    public void removeEncounter(Encounter encounter) {
        encounterPool.remove(encounter.getName());
        encounterProbability.remove(encounter.getName());
    }

    public LinkedHashMap<String, Encounter> getEncounterPool() {
        return encounterPool;
    }

    public void setEncounterPool(LinkedHashMap<String, Encounter> encounterPool) {
        this.encounterPool = encounterPool;
    }

    public LinkedHashMap<String, Double> getEncounterProbabilities() {
        return encounterProbability;
    }

    public void setEncounterProbabilities(LinkedHashMap<String, Double> encounterProbability) {
        this.encounterProbability = encounterProbability;
    }

    public Double getEncounterProbability(String encounterName) {
        return encounterProbability.get(encounterName);
    }

    public Double getEncounterProbability(Encounter encounter) {
        return encounterProbability.get(encounter.getName());
    }

    public void setEncounterProbability(String encounterName, double probability) {
        encounterProbability.remove(encounterName);
        encounterProbability.put(encounterName, probability);
    }

    public void setEncounterProbability(Encounter encounter, double probability) {
        encounterProbability.remove(encounter.getName());
        encounterProbability.put(encounter.getName(), probability);
    }

    public int getNrOfEncounters() {
        return nrOfEncounters;
    }

    public void setNrOfEncounters(int nrOfEncounters) {
        this.nrOfEncounters = nrOfEncounters;
    }
}
