package sammish93.tbage.tools;

import org.junit.jupiter.api.Test;
import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.models.CombatEncounter;
import sammish93.tbage.models.FixedEncounters;
import sammish93.tbage.models.NonCombatEncounter;
import sammish93.tbage.models.RandomEncounters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EncounterTraversalControllerTest {

    GameEngine gameEngine = mock(GameEngine.class);

    @Test
    void assertsEncountersWithOnlyValidPathsReturnsTrue() throws InvalidValueException, InterruptedException {

        var nCoEncounter = NonCombatEncounter.create("ENCOUNTER 1");
        var nCoEncounter2 = NonCombatEncounter.create("ENCOUNTER 2");
        var encounter = CombatEncounter.create("ENCOUNTER 3");

        nCoEncounter2.removeDefaultNavigationalVerbs();
        nCoEncounter2.addNavigationalVerb("open");


        var encounters = FixedEncounters.create();

        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter, nCoEncounter2, "defeated");
        encounters.addEncounter(nCoEncounter2, encounter, "hatch");


        EncounterTraversalController etc = new EncounterTraversalController(encounters);

        assertTrue(etc.checkEncounterPaths(gameEngine));
    }

    @Test
    void assertsEncountersWithAnInvalidPathReturnsFalse() throws InvalidValueException, InterruptedException {

        var nCoEncounter = NonCombatEncounter.create("ENCOUNTER 1");
        var nCoEncounter2 = NonCombatEncounter.create("ENCOUNTER 2");
        var encounter = CombatEncounter.create("ENCOUNTER 3");

        nCoEncounter2.removeDefaultNavigationalVerbs();
        nCoEncounter2.addNavigationalVerb("open");

        nCoEncounter2.addNavigationalNoun("nounThatLeadsToNowhere");


        var encounters = FixedEncounters.create();

        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter, nCoEncounter2, "defeated");
        encounters.addEncounter(nCoEncounter2, encounter, "hatch");


        EncounterTraversalController etc = new EncounterTraversalController(encounters);

        assertTrue(etc.checkEncounterPaths(gameEngine));
    }

    @Test
    void assertsFirstEncounterAddedIsCurrentEncounter() throws InvalidValueException {

        var nCoEncounter = NonCombatEncounter.create("ENCOUNTER 1");
        var nCoEncounter2 = NonCombatEncounter.create("ENCOUNTER 2");

        var encounters = FixedEncounters.create();

        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter, nCoEncounter2, "defeated");

        EncounterTraversalController etc = new EncounterTraversalController(encounters);

        assertEquals(nCoEncounter, EncounterTraversalController.getCurrentEncounter());
    }

    @Test
    void assertsEncounterProgressesOnSuccessfulPrompt() throws InvalidValueException {

        var nCoEncounter = NonCombatEncounter.create("ENCOUNTER 1");
        var nCoEncounter2 = NonCombatEncounter.create("ENCOUNTER 2");

        var encounters = FixedEncounters.create();

        String eventPrompt = "north";
        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter, nCoEncounter2, eventPrompt);

        EncounterTraversalController etc = new EncounterTraversalController(encounters);

        assertEquals(nCoEncounter, EncounterTraversalController.getCurrentEncounter());

        etc.progressToNextEncounter(eventPrompt);

        assertEquals(nCoEncounter2, EncounterTraversalController.getCurrentEncounter());
    }

    @Test
    void assertsEncounterDoesNotProgressOnUnsuccessfulPrompt() throws InvalidValueException {

        var nCoEncounter = NonCombatEncounter.create("ENCOUNTER 1");
        var nCoEncounter2 = NonCombatEncounter.create("ENCOUNTER 2");

        var encounters = FixedEncounters.create();

        String eventPrompt = "north";
        String incorrectEventPrompt = "south";

        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter, nCoEncounter2, eventPrompt);

        EncounterTraversalController etc = new EncounterTraversalController(encounters);

        assertEquals(nCoEncounter, EncounterTraversalController.getCurrentEncounter());

        etc.progressToNextEncounter(incorrectEventPrompt);

        assertNull(EncounterTraversalController.getCurrentEncounter());
        assertNotEquals(nCoEncounter2, EncounterTraversalController.getCurrentEncounter());
    }

    @Test
    void assertsRandomEncounterProgressesNaturally() throws InvalidValueException {

        var nCoEncounter = NonCombatEncounter.create("ENCOUNTER 1");
        var nCoEncounter2 = NonCombatEncounter.create("ENCOUNTER 2");

        var encounters = RandomEncounters.create();

        encounters.addEncounter(nCoEncounter, 1);
        encounters.addEncounter(nCoEncounter2, 0.5);

        EncounterTraversalController etc = new EncounterTraversalController(encounters);


        if (nCoEncounter2 == EncounterTraversalController.getCurrentEncounter()) {
            etc.progressToNextEncounter("defeated");
            assertEquals(nCoEncounter, EncounterTraversalController.getCurrentEncounter());
        } else if (nCoEncounter == EncounterTraversalController.getCurrentEncounter()) {
            etc.progressToNextEncounter("defeated");
            assertEquals(nCoEncounter2, EncounterTraversalController.getCurrentEncounter());
        }
    }

    @Test
    void assertsRandomEncounterProgressesOnPrompt() throws InvalidValueException {

        var nCoEncounter = NonCombatEncounter.create("ENCOUNTER 1");
        var nCoEncounter2 = NonCombatEncounter.create("ENCOUNTER 2");
        nCoEncounter.removeDefaultNavigationalVerbs();
        nCoEncounter2.removeDefaultNavigationalVerbs();
        String prompt = "north";
        nCoEncounter.addNavigationalNoun(prompt);
        nCoEncounter2.addNavigationalNoun(prompt);

        var encounters = RandomEncounters.create();

        encounters.addEncounter(nCoEncounter, 1);
        encounters.addEncounter(nCoEncounter2, 0.5);

        EncounterTraversalController etc = new EncounterTraversalController(encounters);

        if (nCoEncounter2 == EncounterTraversalController.getCurrentEncounter()) {
            etc.progressToNextEncounter(prompt);
            assertEquals(nCoEncounter, EncounterTraversalController.getCurrentEncounter());
        } else if (nCoEncounter == EncounterTraversalController.getCurrentEncounter()) {
            etc.progressToNextEncounter(prompt);
            assertEquals(nCoEncounter2, EncounterTraversalController.getCurrentEncounter());
        }
    }

    @Test
    void assertsRandomEncountersSelectsCorrectAmountOfEncounters() throws InvalidValueException {

        var encounter = CombatEncounter.create();
        var encounter2 = CombatEncounter.create();
        var encounter3 = CombatEncounter.create();
        var encounter4 = CombatEncounter.create();
        var encounter5 = CombatEncounter.create();

        int size = 3;

        var encounters = RandomEncounters.create(size);

        encounters.addEncounter(encounter);
        encounters.addEncounter(encounter2);
        encounters.addEncounter(encounter3);
        encounters.addEncounter(encounter4);
        encounters.addEncounter(encounter5);

        assertEquals(size, encounters.getEncounterOrder().size());
    }

    @Test
    void assertsRandomEncountersSelectsCorrectAmountOfEncountersWithLimitedPool() throws InvalidValueException {

        var encounter = CombatEncounter.create();
        var encounter2 = CombatEncounter.create();
        var encounter3 = CombatEncounter.create();
        var encounter4 = CombatEncounter.create();
        var encounter5 = CombatEncounter.create();

        var encounters = RandomEncounters.create(100);

        encounters.addEncounter(encounter);
        encounters.addEncounter(encounter2);
        encounters.addEncounter(encounter3);
        encounters.addEncounter(encounter4);
        encounters.addEncounter(encounter5);

        assertEquals(5, encounters.getEncounterOrder().size());
    }

    @Test
    void assertsRandomEncountersProgressLinearly() throws InvalidValueException {

        var encounter = CombatEncounter.create();
        var encounter2 = CombatEncounter.create();
        var encounter3 = CombatEncounter.create();
        var encounter4 = CombatEncounter.create();
        var encounter5 = CombatEncounter.create();

        var encounters = RandomEncounters.create(3);

        encounters.addEncounter(encounter);
        encounters.addEncounter(encounter2);
        encounters.addEncounter(encounter3);
        encounters.addEncounter(encounter4);
        encounters.addEncounter(encounter5);

        String encounterNameRetrieved = encounters.getEncounterOrder().peek();

        EncounterTraversalController etc = new EncounterTraversalController(encounters);

        assertEquals(encounterNameRetrieved, EncounterTraversalController.getCurrentEncounter().getName());

        String anotherEncounterNameRetrieved = encounters.getEncounterOrder().peek();

        etc.progressToNextEncounter("defeated");

        assertEquals(anotherEncounterNameRetrieved, EncounterTraversalController.getCurrentEncounter().getName());
    }

}