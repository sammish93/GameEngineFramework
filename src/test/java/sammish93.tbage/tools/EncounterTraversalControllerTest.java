package sammish93.tbage.tools;

import org.junit.jupiter.api.Test;
import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.models.CombatEncounter;
import sammish93.tbage.models.FixedEncounters;
import sammish93.tbage.models.NonCombatEncounter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EncounterTraversalControllerTest {

    GameEngine gameEngine = mock(GameEngine.class);

    @Test
    void assertsEncountersWithOnlyValidPathsReturnsTrue() throws InvalidValueException {

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
    void assertsEncountersWithAnInvalidPathReturnsFalse() throws InvalidValueException {

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

        assertEquals(null, EncounterTraversalController.getCurrentEncounter());
        assertNotEquals(nCoEncounter2, EncounterTraversalController.getCurrentEncounter());
    }

}