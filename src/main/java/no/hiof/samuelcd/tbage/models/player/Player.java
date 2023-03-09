package no.hiof.samuelcd.tbage.models.player;

import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.io.Serializable;
import java.util.Objects;
import java.util.TreeMap;

public class Player implements Serializable {
    // Class that will hold information about a player throughout the game.
    private int inventorySlots = 10;
    private TreeMap<String, Item> inventory;
    private int maxHealth;
    private int currentHealth;
    private TreeMap<String, Feat> feats;


    private Player(int inventorySlots, TreeMap<String, Item> inventory, int maxHealth, TreeMap<String, Feat> feats) {
        this.inventorySlots = inventorySlots;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.inventory = Objects.requireNonNullElseGet(inventory, TreeMap::new);
        this.feats = Objects.requireNonNullElseGet(feats, TreeMap::new);

        if (inventory != null && !isSpaceInInventory()) {
            this.inventorySlots = inventory.size();
        }
    }

    public static Player create() {
        return new Player(0, null, 0, null);
    }

    public boolean isSpaceInInventory() {
        int inventorySize = inventory.size();

        return inventorySize > inventorySlots;
    }
    public int getInventorySlots() {
        return inventorySlots;
    }

    public void setInventorySlots(int inventorySlots) {
        this.inventorySlots = inventorySlots;
    }

    public TreeMap<String, Item> getInventory() {
        return inventory;
    }

    public void setInventory(TreeMap<String, Item> inventory) {
        this.inventory = inventory;
    }

    public Item getItemFromInventory(String itemName) {
        return inventory.get(itemName);
    }

    public void addItemToInventory(Item item) {
        if (isSpaceInInventory()) {
            inventory.put(item.getName(), item);
            inventorySlots++;
        }
    }

    public void removeItemFromInventory(Item item) {
        inventory.remove(item.getName());
        inventorySlots--;
    }

    public void removeItemFromInventory(String itemName) {
        inventory.remove(itemName);
        inventorySlots--;
    }

    public TreeMap<String, Feat> getFeats() {
        return feats;
    }

    public void setFeats(TreeMap<String, Feat> feats) {
        this.feats = feats;
    }

    public Feat getFeatFromFeats(String featName) {
        return feats.get(featName);
    }

    public void addFeatToFeats(Feat feat) {
        feats.put(feat.getName(), feat);
    }

    public void removeFeatFromFeats(Feat feat) {
        feats.remove(feat.getName());
    }

    public void removeFeatFromFeats(String featName) {
        feats.remove(featName);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
