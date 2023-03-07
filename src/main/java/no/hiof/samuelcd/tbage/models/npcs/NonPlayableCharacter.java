package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.util.Objects;
import java.util.TreeMap;

public abstract class NonPlayableCharacter {
    private String name;

    private TreeMap<String, Ability> NpcAbilityPool;

    private TreeMap<String, Item> NpcItemTable;


    public NonPlayableCharacter(String name, TreeMap<String, Ability> abilities) {
        if (name != null) {
            this.setName(name);
        }

        NpcAbilityPool = Objects.requireNonNullElseGet(abilities, TreeMap::new);
    }

    public abstract void processAbilities();

    public abstract void processItems();

    public TreeMap<String, Ability> getNpcAbilityPool() {
        return NpcAbilityPool;
    }

    public void setNpcAbilityPool(TreeMap<String, Ability> npcAbilityPool) {
        this.NpcAbilityPool = npcAbilityPool;
    }

    public Ability getAbilityFromEncounterPool(String abilityName) {
        return NpcAbilityPool.get(abilityName);
    }

    public void addAbilityToEncounterPool(Ability ability) {
        NpcAbilityPool.put(ability.getName(), ability);
    }

    public void removeAbilityFromEncounterPool(Ability ability) {
        NpcAbilityPool.remove(ability.getName());
    }

    public void removeAbilityFromEncounterPool(String abilityName) {
        NpcAbilityPool.remove(abilityName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
