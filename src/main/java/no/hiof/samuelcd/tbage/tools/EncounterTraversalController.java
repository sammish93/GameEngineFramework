package no.hiof.samuelcd.tbage.tools;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.models.encounters.Encounter;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.encounters.RandomEncounters;

import java.util.Set;

public class EncounterTraversalController {

    private static Encounter currentEncounter;
    private Encounters encounters;


    public EncounterTraversalController(Encounters encounters) {
        this.encounters = encounters;

        if (encounters instanceof FixedEncounters) {
            currentEncounter = ((FixedEncounters) encounters).getInitialEncounter();
        } else if (encounters instanceof RandomEncounters) {
            currentEncounter = ((RandomEncounters) encounters).getNextEncounter();
        }
    }

    public static Encounter getCurrentEncounter() {
        return currentEncounter;
    }

    public void progressToNextEncounter(String prompt) {
        if (encounters instanceof FixedEncounters) {
            String encounterName = currentEncounter.getEncounterFromPrompt(prompt);
            currentEncounter = ((FixedEncounters) encounters).getEncounter(encounterName);
        } else if (encounters instanceof RandomEncounters) {
            currentEncounter = ((RandomEncounters) encounters).getNextEncounter();
        }
    }

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
