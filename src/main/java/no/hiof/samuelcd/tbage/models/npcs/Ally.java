package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.models.abilities.Ability;

import java.util.TreeMap;

public class Ally extends NonPlayableCharacter {

    private Ally(String name, TreeMap<String, Ability> abilities) {
        super(name, abilities);
    }

    public static Ally create() {
        return new Ally(null, null);
    }

    public static Ally create(String name, TreeMap<String, Ability> abilities) {
        return new Ally(name, abilities);
    }

    @Override
    public void processAbilities() {
        // Iterates through abilityPool to find which events (onEncounterStart and onEncounterFinish) are TRUE.
    }

    @Override
    public void processItems() {
        // Determines if items are available from a specific Ally.
    }

    @Override
    public String toString() {
        return super.toString();
    }
}