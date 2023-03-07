package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.models.npcs.abilities.Ability;

import java.util.Objects;
import java.util.TreeMap;

public abstract class NonPlayableCharacter {
    private String name;

    private TreeMap<String, Ability> abilityPool;


    public NonPlayableCharacter(String name, TreeMap<String, Ability> abilities) {
        if (name != null) {
            this.setName(name);
        }

        abilityPool = Objects.requireNonNullElseGet(abilities, TreeMap::new);
    }

    public abstract void processAbilities();

    public TreeMap<String, Ability> getAbilityPool() {
        return abilityPool;
    }

    public void setAbilityPool(TreeMap<String, Ability> abilityPool) {
        this.abilityPool = abilityPool;
    }

    public Ability getAbilityFromPool(String abilityName) {
        return abilityPool.get(abilityName);
    }

    public void addAbilityToPool(Ability ability) {
        abilityPool.put(ability.getName(), ability);
    }

    public void removeAbilityFromPool(Ability ability) {
        abilityPool.remove(ability.getName());
    }

    public void removeAbilityFromPool(String abilityName) {
        abilityPool.remove(abilityName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
