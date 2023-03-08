package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.util.Objects;
import java.util.TreeMap;

public abstract class NonPlayableCharacter implements JsonExternalisable {
    private String name;
    private TreeMap<String, Ability> NpcAbilityPool;
    private TreeMap<String, Item> NpcItemTable;
    private String imagePath;


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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public TreeMap<String, Item> getNpcItemTable() {
        return NpcItemTable;
    }

    public void setNpcItemTable(TreeMap<String, Item> npcItemTable) {
        NpcItemTable = npcItemTable;
    }

    public Item getItemFromItemTable(String itemName) {
        return NpcItemTable.get(itemName);
    }

    public void addItemToItemTable(Item item) {
        NpcItemTable.put(item.getName(), item);
    }

    public void removeItemFromItemTable(Item item) {
        NpcItemTable.remove(item.getName());
    }

    public void removeItemFromItemTable(String itemName) {
        NpcItemTable.remove(itemName);
    }
}
