package no.hiof.samuelcd.tbage.models.items;

import java.util.Objects;
import java.util.TreeMap;

public class ItemPool {

    TreeMap<String, Item> itemMap;


    private ItemPool(TreeMap<String, Item> encounterMap) {
        // On null it should use a static loot table that is hard coded.
        this.itemMap = Objects.requireNonNullElseGet(encounterMap, TreeMap::new);
    }

    public static ItemPool create() {
        return new ItemPool(null);
    }

    public static ItemPool create(TreeMap<String, Item> itemMap) {
        return new ItemPool(itemMap);
    }

    public void addEncounter(Item item) {
        // Should implement behaviour if encounter already exists.
        itemMap.put(item.getName(), item);
    }

    public void removeEncounter(String itemName) {
        itemMap.remove(itemName);
    }
    public void removeEncounter(Item item) {
        itemMap.remove(item.getName());
    }

    public Item getEncounter(String itemName) {
        return itemMap.get(itemName);
    }
}
