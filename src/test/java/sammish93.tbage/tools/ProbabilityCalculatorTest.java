package sammish93.tbage.tools;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import sammish93.tbage.exceptions.InvalidValueException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class ProbabilityCalculatorTest {

    @Test
    void assertsValueIsAddedToPool() {
        String itemAdded = "example";

        var calculator = new ProbabilityCalculator<String>();

        calculator.add(0.5, itemAdded);

        String itemRetrieved = calculator.next();

        assertEquals(itemAdded, itemRetrieved);
    }

    @Test
    void assertsValuesAreAddedToPool() {
        String itemAdded = "example";
        String anotherItemAdded = "exampleTwo";

        var calculator = new ProbabilityCalculator<String>();

        calculator.add(0.5, itemAdded);
        calculator.add(0.5, anotherItemAdded);

        List<String> retrievedItems = new ArrayList<String>();
        retrievedItems.add(calculator.nextThenRemoveReturnedItemFromPool());
        retrievedItems.add(calculator.next());

        assertTrue(retrievedItems.contains(itemAdded));
        assertTrue(retrievedItems.contains(anotherItemAdded));
    }

    @Test
    void assertsGuaranteedValuesAreAlwaysChosen() {
        String itemAdded = "example";
        String anotherItemAdded = "exampleTwo";
        String thirdItemAdded = "exampleThree";

        var calculator = new ProbabilityCalculator<String>();

        calculator.add(1, itemAdded);
        calculator.add(1, anotherItemAdded);
        calculator.add(0.5, thirdItemAdded);

        List<String> retrievedItems = new ArrayList<String>();
        retrievedItems.add(calculator.nextThenRemoveReturnedItemFromPool());
        retrievedItems.add(calculator.nextThenRemoveReturnedItemFromPool());

        assertTrue(retrievedItems.contains(itemAdded));
        assertTrue(retrievedItems.contains(anotherItemAdded));
        assertEquals(thirdItemAdded, calculator.next());
    }

    @Test
    void assertsGuaranteedValueIsAlwaysChosen() {
        String itemAdded = "example";
        String anotherItemAdded = "exampleTwo";

        var calculator = new ProbabilityCalculator<String>();

        calculator.add(0.9, itemAdded);
        calculator.add(1, anotherItemAdded);

        for (int i = 0; i < 100; i++) {
            assertEquals(anotherItemAdded, calculator.next());
        }
    }

    @Test
    void assertsGuaranteedValueIsRemovedCorrectly() {
        String itemAdded = "example";
        String anotherItemAdded = "exampleTwo";

        var calculator = new ProbabilityCalculator<String>();

        calculator.add(0.9, itemAdded);
        calculator.add(1, anotherItemAdded);

        assertEquals(anotherItemAdded, calculator.nextThenRemoveReturnedItemFromPool());
        assertEquals(itemAdded, calculator.nextThenRemoveReturnedItemFromPool());
    }

    @Test
    void assertsPoolIsEmptyWhenAllValuesAreRemoved() {
        String itemAdded = "example";

        var calculator = new ProbabilityCalculator<String>();

        calculator.add(0.5, itemAdded);

        assertEquals(itemAdded, calculator.nextThenRemoveReturnedItemFromPool());
        assertThrows(NullPointerException.class, calculator::next);
    }

    @Test
    void assertsValueWithZeroPercentProbabilityIsNeverAddedToPool() {
        String itemAdded = "example";

        var calculator = new ProbabilityCalculator<String>();

        calculator.add(0, itemAdded);

        assertThrows(NullPointerException.class, calculator::next);
    }

    @Test
    void assertsDamageCalculatorReturnsValueBetweenMinimumAndMaximumDamageParameters() throws InvalidValueException {
        int minDamage = 5;
        int maxDamage = 10;

        ArrayList<Integer> validValues = new ArrayList<>();

        for (int i = minDamage; i <= maxDamage; i++) {
            validValues.add(i);
        }

        var random = new Random();

        try (MockedStatic<ProbabilityCalculator> calcuator = mockStatic(ProbabilityCalculator.class)) {
            calcuator.when(() -> ProbabilityCalculator.damageCalculator(minDamage, maxDamage))
                    .thenReturn(random.nextInt(maxDamage - minDamage) + minDamage);

        }

        ArrayList<Integer> thing = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int generatedDamage = ProbabilityCalculator.damageCalculator(minDamage, maxDamage);
            assertTrue(validValues.contains(generatedDamage));
        }
    }

    @Test
    void assertsDamageCalculatorReturnsUpperAndLowerParameters() throws InvalidValueException {
        int minDamage = 5;
        int maxDamage = 10;

        ArrayList<Integer> validValues = new ArrayList<>();

        for (int i = minDamage; i <= maxDamage; i++) {
            validValues.add(i);
        }

        var random = new Random();

        try (MockedStatic<ProbabilityCalculator> calcuator = mockStatic(ProbabilityCalculator.class)) {
            calcuator.when(() -> ProbabilityCalculator.damageCalculator(minDamage, maxDamage))
                    .thenReturn(random.nextInt(maxDamage - minDamage) + minDamage);

        }

        ArrayList<Integer> thing = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int generatedDamage = ProbabilityCalculator.damageCalculator(minDamage, maxDamage);
            assertTrue(validValues.contains(generatedDamage));
            thing.add(generatedDamage);
        }

        assertTrue(thing.contains(minDamage));
        assertTrue(thing.contains(maxDamage));
    }
}