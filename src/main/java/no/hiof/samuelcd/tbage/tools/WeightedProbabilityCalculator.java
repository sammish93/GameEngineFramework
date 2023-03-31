package no.hiof.samuelcd.tbage.tools;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// Used a modified version of code found here https://stackoverflow.com/a/6409791 on 31/03/2023 at 12:07.
// Added behaviour to handle so-called 'guaranteed chance' being given (i.e. 100% chance of happening), while
// returning only a single value.

public class WeightedProbabilityCalculator<T> {
    private final NavigableMap<Double, T> mapOfNonGuaranteedValues = new TreeMap<Double, T>();
    private final NavigableMap<Double, T> mapOfGuaranteedValues = new TreeMap<Double, T>();
    private final Random random;
    private double totalNonGuaranteedValues = 0;
    private double totalGuaranteedValues = 0;
    private boolean isGuaranteedValuePresent;

    public WeightedProbabilityCalculator() {
        this(new Random());
        isGuaranteedValuePresent = false;
    }

    public WeightedProbabilityCalculator(Random random) {
        this.random = random;
        isGuaranteedValuePresent = false;
    }

    public WeightedProbabilityCalculator<T> add(double weight, T result) {
        if (weight <= 0){

            return this;
        } else if (weight >= 1) {
            isGuaranteedValuePresent = true;

            totalGuaranteedValues += weight;
            mapOfGuaranteedValues.put(totalGuaranteedValues, result);
        } else {
            totalNonGuaranteedValues += weight;
            mapOfNonGuaranteedValues.put(totalNonGuaranteedValues, result);
        }

        return this;
    }

    public T next() {
        if (isGuaranteedValuePresent) {
            double value = random.nextDouble() * totalGuaranteedValues;
            return mapOfGuaranteedValues.higherEntry(value).getValue();
        } else {
            double value = random.nextDouble() * totalNonGuaranteedValues;
            return mapOfNonGuaranteedValues.higherEntry(value).getValue();
        }
    }
}
