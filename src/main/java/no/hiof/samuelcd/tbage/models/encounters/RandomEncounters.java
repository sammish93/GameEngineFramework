package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.tools.ProbabilityCalculator;

import java.util.*;

public class RandomEncounters extends Encounters{

    private LinkedHashMap<String, Encounter> encounterPool;
    private LinkedHashMap<String, Double> encounterProbability;
    private Queue<String> encounterOrder;
    private int nrOfEncounters;
    private boolean isCompleted;
    private int encountersRemaining;


    private RandomEncounters(int nrOfEncounters) throws InvalidValueException {
        encounterPool = new LinkedHashMap<>();
        encounterProbability = new LinkedHashMap<>();

        if (nrOfEncounters > 0) {
            this.nrOfEncounters = nrOfEncounters;
        } else {
            throw new InvalidValueException("Value " + nrOfEncounters + " is invalid. Enter an integer " +
                    "value greater than 0.");
        }

        encounterOrder = new LinkedList<>();
        isCompleted = false;
        encountersRemaining = nrOfEncounters;
    }

    public static RandomEncounters create() throws InvalidValueException {
        return new RandomEncounters(10);
    }

    public static RandomEncounters create(int nrOfEncounters) throws InvalidValueException {
        return new RandomEncounters(nrOfEncounters);
    }


    public void addEncounter(Encounter encounter) {
        encounterPool.put(encounter.getName(), encounter);
        encounterProbability.put(encounter.getName(), 0.5);
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

    public int getNumberOfEncounters() {
        return nrOfEncounters;
    }

    public void setNumberOfEncounters(int nrOfEncounters) throws InvalidValueException {
        if (nrOfEncounters > 0) {
            this.nrOfEncounters = nrOfEncounters;
        } else {
            throw new InvalidValueException("Value " + nrOfEncounters + " is invalid. Enter an integer " +
                    "value greater than 0.");
        }
    }

    private void randomiseEncounters() {
        ProbabilityCalculator<String> probabilityCalculator = new ProbabilityCalculator<>();
        ArrayList<String> encounterOrderBeforeShuffle = new ArrayList<>();

        for (Map.Entry<String, Double> encounterProbabilityEntry : encounterProbability.entrySet()) {
            String encounterName = encounterProbabilityEntry.getKey();
            double probability = encounterProbabilityEntry.getValue();
            probabilityCalculator.add(probability, encounterName);
        }

        for (int i = 0; i < nrOfEncounters; i++) {
            String chosenEncounter = probabilityCalculator.nextRandomEncounter();

            if (chosenEncounter == null) {
                break;
            }

            encounterOrderBeforeShuffle.add(chosenEncounter);
        }

        Collections.shuffle(encounterOrderBeforeShuffle);

        encounterOrder.addAll(encounterOrderBeforeShuffle);
    }

    public Queue<String> getEncounterOrder() {
        if (encounterOrder.isEmpty()) {
            randomiseEncounters();
            return encounterOrder;
        }
        return encounterOrder;
    }

    public Encounter getNextEncounter() {
        if (!isCompleted && encounterOrder.isEmpty()) {
            randomiseEncounters();
            encountersRemaining = encounterOrder.size();
        }

        if (isCompleted) {
            return null;
        } else {
            encountersRemaining--;
            if (encountersRemaining == 0) {
                isCompleted = true;
            }
            return encounterPool.get(encounterOrder.poll());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Random Encounters In Order:");

        for (String encounterName : getEncounterOrder()) {
            sb.append("\n" + encounterPool.get(encounterName).toString());
        }

        return sb.toString();
    }
}
