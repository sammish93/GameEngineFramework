package no.hiof.samuelcd.tbage.tools;

import no.hiof.samuelcd.tbage.models.encounters.Encounter;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;

public class GameController {

    private Encounter currentEncounter;
    private Encounters encounters;

    public GameController(Encounters encounters) {
        this.encounters = encounters;

        if (encounters instanceof FixedEncounters) {
            currentEncounter = ((FixedEncounters) encounters).getInitialEncounter();
        }
    }

    public Encounter getCurrentEncounter() {
        return currentEncounter;
    }

    public void progressToNextEncounter(String prompt) {
        if (encounters instanceof FixedEncounters) {
            String encounterName = currentEncounter.getEncounterFromPrompt(prompt);
            currentEncounter = ((FixedEncounters) encounters).getEncounter(encounterName);
        }
    }
}
