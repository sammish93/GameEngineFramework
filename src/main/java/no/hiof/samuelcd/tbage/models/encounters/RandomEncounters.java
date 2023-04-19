package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.tools.ProbabilityCalculator;

import java.util.*;

/**
 * A class that includes an encounter pool and  an integer that states how many encounters are to be chosen
 * from said encounter pool to be included in a single game's permutation.
 */
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

    /**
     *
     * @return Returns an instantiated RandomEncounters object with a default number of encounters present in a
     * single game's permutation as 10.
     * @throws InvalidValueException
     */
    public static RandomEncounters create() throws InvalidValueException {
        return new RandomEncounters(10);
    }

    /**
     *
     * @param nrOfEncounters Sets the number of encounters present in a single game's permutation to a positive
     *                       integer value.
     *                       NOTE: If the value is greater than the amount of Encounter objects in the pool then
     *                       the amount of encounters in a single permutation will be equal to the amount of
     *                       encounters in a pool.
     * @return Returns an instantiated RandomEncounters object.
     * @throws InvalidValueException Thrown if a value less than or equal to 0 is provided.
     */
    public static RandomEncounters create(int nrOfEncounters) throws InvalidValueException {
        return new RandomEncounters(nrOfEncounters);
    }


    /**
     * Adds an Encounter to the pool with a default weighted probability chance of 50%.
     * @param encounter An instantiated Encounter object.
     */
    public void addEncounter(Encounter encounter) {
        encounterPool.put(encounter.getName(), encounter);
        encounterProbability.put(encounter.getName(), 0.5);
    }

    /**
     * Adds an Encounter to the pool.
     * @param encounter An instantiated Encounter object.
     * @param probability A double representing a weighted probability chance of an encounter being selected to
     *                    appear in a single permutation of a game. A value of 0.5 represents a 50% chance,
     *                    whereas a value as 1 represents a 100% chance.
     * @throws InvalidValueException Thrown if a value provided is less than or equal to 0, or greater than 1.
     */
    public void addEncounter(Encounter encounter, double probability) throws InvalidValueException {
        encounterPool.put(encounter.getName(), encounter);
        encounterProbability.put(encounter.getName(), probability);
        if (probability <= 1.00 && probability > 0) {
            encounterProbability.put(encounter.getName(), probability);
        } else {
            throw new InvalidValueException("Value " + probability + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }

    /**
     * Removes a single instantiated Encounter object from the encounter pool.
     * @param encounterName A string representing an instantiated Encounter object.
     */
    public void removeEncounter(String encounterName) {
        encounterPool.remove(encounterName);
        encounterProbability.remove(encounterName);
    }

    /**
     * Removes a single instantiated Encounter object from the encounter pool.
     * @param encounter An instantiated Encounter object.
     */
    public void removeEncounter(Encounter encounter) {
        encounterPool.remove(encounter.getName());
        encounterProbability.remove(encounter.getName());
    }

    /**
     *
     * @return Returns a LinkedHashMap of all Encounter objects currently in the encounter pool.
     */
    public LinkedHashMap<String, Encounter> getEncounterPool() {
        return encounterPool;
    }

    /**
     *
     * @param encounterPool Sets an encounter pool to an existing LinkedHashMap containing encounters.
     */
    public void setEncounterPool(LinkedHashMap<String, Encounter> encounterPool) {
        this.encounterPool = encounterPool;
    }

    /**
     *
     * @return Returns a LinkedHashMap of strings representing an Encounter object, along with their weighted
     * probabilities.
     */
    public LinkedHashMap<String, Double> getEncounterProbabilities() {
        return encounterProbability;
    }

    /**
     *
     * @param encounterProbability Sets all encounter probabilities in the pool to an existing LinkedHashMap
     *                             containing strings representing Encounter objects, along with their
     *                             weighted probabilities.
     */
    public void setEncounterProbabilities(LinkedHashMap<String, Double> encounterProbability) {
        this.encounterProbability = encounterProbability;
    }

    /**
     *
     * @param encounterName A string representing an instantiated Encounter object.
     * @return Returns a positive decimal representing the weighted probability chance of an encounter being
     * selected to appear in a single game's permutation.
     */
    public Double getEncounterProbability(String encounterName) {
        return encounterProbability.get(encounterName);
    }

    /**
     *
     * @param encounter An instantiated Encounter object.
     * @return Returns a positive decimal representing the weighted probability chance of an encounter being
     * selected to appear in a single game's permutation.
     */
    public Double getEncounterProbability(Encounter encounter) {
        return encounterProbability.get(encounter.getName());
    }

    /**
     *
     * @param encounterName A string representing an instantiated Encounter object.
     * @param probability A double representing a weighted probability chance of an encounter being selected to
     *                    appear in a single permutation of a game. A value of 0.5 represents a 50% chance,
     *                    whereas a value as 1 represents a 100% chance.
     * @throws InvalidValueException Thrown if a value provided is less than or equal to 0, or greater than 1.
     */
    public void setEncounterProbability(String encounterName, double probability) throws InvalidValueException {
        if (probability <= 1.00 && probability > 0) {
            encounterProbability.remove(encounterName);
            encounterProbability.put(encounterName, probability);
        } else {
            throw new InvalidValueException("Value " + probability + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }

    /**
     *
     * @param encounter An instantiated Encounter object.
     * @param probability A double representing a weighted probability chance of an encounter being selected to
     *                    appear in a single permutation of a game. A value of 0.5 represents a 50% chance,
     *                    whereas a value as 1 represents a 100% chance.
     * @throws InvalidValueException Thrown if a value provided is less than or equal to 0, or greater than 1.
     */
    public void setEncounterProbability(Encounter encounter, double probability) throws InvalidValueException {
        if (probability <= 1.00 && probability > 0) {
            encounterProbability.remove(encounter.getName());
            encounterProbability.put(encounter.getName(), probability);
        } else {
            throw new InvalidValueException("Value " + probability + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }

    /**
     *
     * @return Returns the number of encounters to be selected for a single game's permutation.
     */
    public int getNumberOfEncounters() {
        return nrOfEncounters;
    }

    /**
     *
     * @param nrOfEncounters A positive integer to determine the amount of encounters to be selected for a
     *                       single game's permutation
     * @throws InvalidValueException Thrown if a value is less than or equal to 0.
     */
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
            String chosenEncounter = probabilityCalculator.nextThenRemoveReturnedItemFromPool();

            if (chosenEncounter == null) {
                break;
            }

            encounterOrderBeforeShuffle.add(chosenEncounter);
        }

        Collections.shuffle(encounterOrderBeforeShuffle);

        encounterOrder.addAll(encounterOrderBeforeShuffle);
    }

    /**
     *
     * @return Returns a queue of strings representing instantiated Encounter objects. The queue models the order
     * of traversal for a single game permutation.
     */
    public Queue<String> getEncounterOrder() {
        if (encounterOrder.isEmpty()) {
            randomiseEncounters();
            return encounterOrder;
        }
        return encounterOrder;
    }

    /**
     *
     * @return Returns an instantiated Encounter object of the next encounter once the current encounter has been
     * defeated.
     */
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

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
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
