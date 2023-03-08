package no.hiof.samuelcd.tbage.models.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class Player implements JsonExternalisable {
    // Class that will hold information about a player throughout the game.
    private int inventorySlots = 10;
    private TreeMap<String, Item> inventory;
    private int maxHealth;
    private int currentHealth;


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
    public void writeToJson(File file) throws IOException {
        ObjectMapper om = new ObjectMapper();
        boolean fileExists = file.exists();

        if (!fileExists) {
            fileExists = file.createNewFile();
        }

        if (fileExists) {
            om.writeValue(file, this);
        }

        // Jackson auto-closes the stream and mapper.
    }

    @Override
    public void readFromJson(File file) throws IOException{
        ObjectMapper om = new ObjectMapper();
        Player player = om.readValue(file, Player.class);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
