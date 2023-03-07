package no.hiof.samuelcd.tbage.models.abilities;

import java.util.Objects;
import java.util.TreeMap;

public class AbilityPool {

    private TreeMap<String, Ability> abilityMap;

    private AbilityPool(TreeMap<String, Ability> encounterMap) {
        // On null it should use a static ability table that is hard coded.
        this.abilityMap = Objects.requireNonNullElseGet(encounterMap, TreeMap::new);
    }

    public static AbilityPool create() {
        return new AbilityPool(null);
    }

    public static AbilityPool create(TreeMap<String, Ability> abilityMap) {
        return new AbilityPool(abilityMap);
    }

    public Ability getAbilityFromPool(String abilityName) {
        return abilityMap.get(abilityName);
    }

    public void addAbilityToPool(Ability ability) {
        abilityMap.put(ability.getName(), ability);
    }

    public void removeAbilityFromPool(Ability ability) {
        abilityMap.remove(ability.getName());
    }

    public void removeAbilityFromPool(String abilityName) {
        abilityMap.remove(abilityName);
    }

}
