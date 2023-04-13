package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public abstract class NonPlayableCharacter implements Serializable, Cloneable {
    private String name;
    private TreeMap<String, Ability> NpcAbilityPool;
    private TreeMap<String, Item> NpcItemTable;
    // ***********
    // Data type not decided yet for imagePath.
    // ***********
    private String imagePath;


    public NonPlayableCharacter(String name, TreeMap<String, Ability> abilities, TreeMap<String, Item> items) {
        if (name != null) {
            this.setName(name);
        }

        NpcAbilityPool = Objects.requireNonNullElseGet(abilities, TreeMap::new);
        NpcItemTable = Objects.requireNonNullElseGet(items, TreeMap::new);
    }


    public TreeMap<String, Ability> getNpcAbilityPool() {
        return NpcAbilityPool;
    }

    public void setNpcAbilityPool(TreeMap<String, Ability> npcAbilityPool) {
        this.NpcAbilityPool = npcAbilityPool;
    }

    public Ability getAbilityFromAbilityPool(String abilityName) {
        return NpcAbilityPool.get(abilityName);
    }

    public void addAbilityToAbilityPool(Ability ability) {
        NpcAbilityPool.put(ability.getName(), ability);
    }

    public void removeAbilityFromAbilityPool(Ability ability) {
        NpcAbilityPool.remove(ability.getName());
    }

    public void removeAbilityFromAbilityPool(String abilityName) {
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

    protected void printItemTableAndAbilityPool(StringBuilder sb) {
        if (!getNpcItemTable().isEmpty()) {
            sb.append("\n\tItem Table: ");
            for (Map.Entry<String, Item> itemSet : getNpcItemTable().entrySet()) {
                Item item = itemSet.getValue();
                sb.append("\n\t\t" + item.toString());
            }
        }

        if (!getNpcAbilityPool().isEmpty()) {
            sb.append("\n\tAbility Table: ");
            for (Map.Entry<String, Ability> abilitySet : getNpcAbilityPool().entrySet()) {
                Ability ability = abilitySet.getValue();
                sb.append("\n\t\t" + ability.toString());
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
