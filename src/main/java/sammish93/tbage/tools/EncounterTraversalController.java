package sammish93.tbage.tools;

import sammish93.tbage.GameEngine;
import sammish93.tbage.models.Encounter;
import sammish93.tbage.models.Encounters;
import sammish93.tbage.models.FixedEncounters;
import sammish93.tbage.models.RandomEncounters;

import java.util.Set;

/**
 * A class that handles the transition between one encounter to another.
 *
 * NOTE: This class is intended to be used by the chosen game interface, and not to be changed by the developer.
 * The access modifiers are public for the rare occasion that a developer wishes to extend this class and create
 * their own traversal algorithm, or to integrate another interface within the framework.
 */
public class EncounterTraversalController {

    private static Encounter currentEncounter;
    private Encounters encounters;


    /**
     *
     * @param encounters Either an instance of FixedEncounters or RandomEncounters can be supplied here. In either
     *                   case, the initial encounter that the player begins in will be calculated.
     */
    public EncounterTraversalController(Encounters encounters) {
        this.encounters = encounters;

        if (encounters instanceof FixedEncounters) {
            currentEncounter = ((FixedEncounters) encounters).getInitialEncounter();
        } else if (encounters instanceof RandomEncounters) {
            currentEncounter = ((RandomEncounters) encounters).getNextEncounter();
        }
    }

    /**
     *
     * @return Returns an Encounter object of the current encounter.
     * NOTE: During runtime, this will be the encounter that the player currently is it, otherwise it will be
     * the initial encounter that the player will begin in.
     */
    public static Encounter getCurrentEncounter() {
        return currentEncounter;
    }

    /**
     * A method intended to calculate which Encounter object the player will traverse to, based on a prompt.
     * @param prompt A prompt that determines which Encounter object will be set as the current encounter.
     *               This is only used for FixedEncounters where an Encounter may have multiple Encounter objects
     *               that can be traversed from a single Encounter.
     */
    public void progressToNextEncounter(String prompt) {
        if (encounters instanceof FixedEncounters) {
            String encounterName = currentEncounter.getEncounterFromPrompt(prompt);
            currentEncounter = ((FixedEncounters) encounters).getEncounter(encounterName);
        } else if (encounters instanceof RandomEncounters) {
            currentEncounter = ((RandomEncounters) encounters).getNextEncounter();
        }
    }

    /**
     * A method intended to validate that all prompts that are used within the progressToNextEncounter() method
     * are linked to valid Encounter objects.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @return Returns true if all prompts lead to instantiated Encounter objects. Returns false if not.
     * @see EncounterTraversalController#progressToNextEncounter(String)
     */
    public boolean checkEncounterPaths(GameEngine gameEngine) {
        if (encounters instanceof FixedEncounters) {
            Set<String> keys = ((FixedEncounters) encounters).getEncounters().keySet();;

            for (String key : keys) {
                if (((FixedEncounters) encounters).getEncounter(key) == null) {
                    gameEngine.printMessage("The path '" + key + "' cannot find a correlating encounter.");
                    return false;
                }
            }
        }
        return true;
    }
}
