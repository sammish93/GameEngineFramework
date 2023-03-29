package no.hiof.samuelcd.tbage.models.player;

import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.io.*;
import java.util.Objects;
import java.util.TreeMap;

public class Player implements Serializable {
    // Class that will hold information about a player throughout the game.
    private int inventorySlots ;
    private TreeMap<String, Item> inventory;
    private double maxHealth;
    private double currentHealth;
    private double[] damage = new double[2];
    private TreeMap<String, Feat> feats;


    private Player(int inventorySlots, TreeMap<String, Item> inventory, int maxHealth, int minDamage, int maxDamage, TreeMap<String, Feat> feats) {
        this.inventorySlots = inventorySlots;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage[0] = minDamage;
        this.damage[1] = maxDamage;
        this.inventory = Objects.requireNonNullElseGet(inventory, TreeMap::new);
        this.feats = Objects.requireNonNullElseGet(feats, TreeMap::new);

        if (inventory != null && !isSpaceInInventory()) {
            this.inventorySlots = inventory.size();
        }
    }

    public static Player create() {
        return new Player(10, null, 10, 2, 5, null);
    }

    public boolean isSpaceInInventory() {
        int inventorySize = inventory.size();

        return inventorySize < inventorySlots;
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

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public double getMinDamage() {
        return damage[0];
    }

    public void setMinDamage(int minDamage) {
        this.damage[0] = minDamage;
    }

    public double getMaxDamage() {
        return damage[1];
    }

    public void setMaxDamage(int maxDamage) {
        this.damage[1] = maxDamage;
    }

    public void subtractFromCurrentHealth(int i) {
        currentHealth -= i;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public void addToCurrentHealth(int i) {
        currentHealth += i;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void save(String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);
    }

    public static Player load(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Player)objectInputStream.readObject();
    }
}
