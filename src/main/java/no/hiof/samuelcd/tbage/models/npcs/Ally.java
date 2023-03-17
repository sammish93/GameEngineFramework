package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.util.TreeMap;

public class Ally extends NonPlayableCharacter {

    private Ally(String name, TreeMap<String, Ability> abilities, TreeMap<String, Item> items) {
        super(name, abilities, items);
    }

    public static Ally create() {
        return new Ally(null, null, null);
    }

    public static Ally create(String name, TreeMap<String, Ability> abilities, TreeMap<String, Item> items) {
        return new Ally(name, abilities, items);
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