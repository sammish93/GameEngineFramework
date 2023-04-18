package no.hiof.samuelcd.tbage.tools;

import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// Used a modified version of code found here https://stackoverflow.com/a/6409791 on 31/03/2023 at 12:07.

/**
 * A class intended to be used as a means to calculate both weighted and non-weighted probabilities, and to
 * return a generic object based on the objects, together with their probabilities, that have been supplied.
 * @param <T> A generic type that can be set to any desired object.
 */
public class ProbabilityCalculator<T> {

    private final NavigableMap<Double, T> mapOfNonGuaranteedValues = new TreeMap<Double, T>();
    private final NavigableMap<Double, T> mapOfGuaranteedValues = new TreeMap<Double, T>();
    private final Random random;
    private double totalNonGuaranteedValues = 0;
    private double totalGuaranteedValues = 0;
    private boolean isGuaranteedValuePresent;

    /**
     * Creates a new instance of this class, together with a new instance of the Random class.
     * @see Random
     */
    public ProbabilityCalculator() {
        this(new Random());
        isGuaranteedValuePresent = false;
    }

    /**
     * Creates a new instance of this class.
     * @param random An instance of the Random class can be optionally supplied.
     */
    public ProbabilityCalculator(Random random) {
        this.random = random;
        isGuaranteedValuePresent = false;
    }

    /**
     * A method intended to supply the class with an additional item, together with its probability of
     * being chosen.
     * Example:
     * probabilityCalculator.add(0.5, "exampleValue");
     * @param weight A decimal value (float) that is greater than 0 and less than or equal to 1.
     *               A value of 0.5 represents a 50% chance for the item to be chosen, while a value of 1
     *               represents a guaranteed chance (100%).
     *
     *               Keep in mind that these values are weighted against
     *               other values. If only two values with 50% chances were given, then both would have an
     *               equal chance of being chosen, whereas if one was 50% and the other was 100% then the 100%
     *               chance would always be chosen first.
     *
     *               See the next() and nextThenRemoveReturnedItemFromPool() methods for more information on what
     *               happens when a result is chosen.
     * @param item An object of the same generic type given when this class was first instantiated.
     * @return Returns an instance of this class.
     * @see ProbabilityCalculator#next()
     * @see ProbabilityCalculator#nextThenRemoveReturnedItemFromPool()
     */
    public ProbabilityCalculator<T> add(double weight, T item) {
        if (weight <= 0){

            return this;
        } else if (weight >= 1) {
            isGuaranteedValuePresent = true;

            totalGuaranteedValues += weight;
            mapOfGuaranteedValues.put(totalGuaranteedValues, item);
        } else {
            totalNonGuaranteedValues += weight;
            mapOfNonGuaranteedValues.put(totalNonGuaranteedValues, item);
        }

        return this;
    }

    /**
     *
     * @return Returns a chosen object of generic type based on the weighted probability chance of it being chosen.
     * If an object with a guaranteed chance to be chosen (100%) is present, then this would be returned.
     * If several objects with a guaranteed chance are present, then each one has an equal chance to be returned.
     * If no objects with a guaranteed chance are present, then the weighted probability algorithm chooses one
     * based on their combined probability.
     *
     * See the nextThenRemoveReturnedItemFromPool() method for an application where the object is then removed from the pool of
     * objects that can be chosen from, never to be chosen again.
     *
     * @see ProbabilityCalculator#nextThenRemoveReturnedItemFromPool()
     */
    public T next() {
        if (isGuaranteedValuePresent) {
            double value = random.nextDouble() * totalGuaranteedValues;
            return mapOfGuaranteedValues.higherEntry(value).getValue();
        } else {
            double value = random.nextDouble() * totalNonGuaranteedValues;
            return mapOfNonGuaranteedValues.higherEntry(value).getValue();
        }
    }

    /**
     * This class is intended to be used by the RandomEncounters class, and is similar to the next() method, apart
     * from the key difference that once an object has been chosen then it is removed, and not able to be chosen
     * on future iterations.
     * @return Returns a chosen object of generic type based on the weighted probability chance of it being chosen.
     * If an object with a guaranteed chance to be chosen (100%) is present, then this would be returned.
     * If several objects with a guaranteed chance are present, then each one has an equal chance to be returned.
     * If no objects with a guaranteed chance are present, then the weighted probability algorithm chooses one
     * based on their combined probability.
     *
     * NOTE: Once chosen and returned, the object is then removed from the pool of objects able to be chosen, and
     * cannot be chosen again.
     * @see no.hiof.samuelcd.tbage.models.encounters.RandomEncounters
     * @see ProbabilityCalculator#next()
     */
    public T nextThenRemoveReturnedItemFromPool() {
        if (mapOfGuaranteedValues.isEmpty()) {
            isGuaranteedValuePresent = false;
        }

        if (isGuaranteedValuePresent) {
            double value = random.nextDouble() * totalGuaranteedValues;
            Map.Entry<Double, T> entry = mapOfGuaranteedValues.higherEntry(value);
            if (entry != null) {
                mapOfGuaranteedValues.remove(entry.getKey(), entry.getValue());
                return entry.getValue();
            }
            return null;

        } else {
            double value = random.nextDouble() * totalNonGuaranteedValues;
            Map.Entry<Double, T> entry = mapOfNonGuaranteedValues.higherEntry(value);
            if (entry != null) {
                mapOfNonGuaranteedValues.remove(entry.getKey(), entry.getValue());
                return entry.getValue();
            }
            return null;
        }
    }

    /**
     * A static method intended to be used to calculate which items are dropped when an enemy is defeated.
     * @param chanceUpToOneHundred An integer value from 0 to 100.
     * @return If the random number generated from 0 to 100
     * is equal to or greater than the integer provided then the value 'true' is returned, and the item appears
     * as a drop in the game, otherwise 'false' is returned.
     * @throws InvalidValueException Is thrown when an invalid integer is provided as a parameter. If the integer
     * is less than 0, or greater than 100, then this exception is thrown.
     * @see Random
     */
    protected static boolean isDropped(int chanceUpToOneHundred) throws InvalidValueException {
        Random random = new Random();
        int chance = random.nextInt(100);

        if (chanceUpToOneHundred < 0 || chanceUpToOneHundred > 100) {
            throw new InvalidValueException("Value " + chanceUpToOneHundred + " is invalid. Enter an integer " +
                    "value greater than or equal to 0 and less than or equal to 100.");
        }
        return chance <= chanceUpToOneHundred;
    }

    /**
     * A static method intended to be used to calculate a melee weapon attack based on the minimum and maximum
     * damage of the attacking source.
     * @param minDamage An integer value greater than or equal to 0.
     * @param maxDamage An integer value greater than or equal to 0.
     * @return Returns an integer based on a random value selected between the two other values provided as
     * parameter values (minimum and maximum damage).
     * @throws InvalidValueException Is thrown in the event of the minimum damage supplied being greater than
     * the maximum damage. This is because Random can throw its own exceptions when supplied with minus values.
     */
    public static int damageCalculator(int minDamage, int maxDamage) throws InvalidValueException {

        var random = new Random();

        if (minDamage > maxDamage) {
            throw new InvalidValueException("Value " + minDamage + " is invalid. Enter an integer " +
                    "value greater than or equal to " + maxDamage);
        }

        if (maxDamage - minDamage == 0) {
            return minDamage;
        }

        return random.nextInt(maxDamage - minDamage) + minDamage;
    }
}
