package no.hiof.samuelcd.tbage.models.player;

import com.sun.source.tree.Tree;
import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Player implements Serializable {
    // Class that will hold information about a player throughout the game.
    private int inventorySlots ;
    private TreeMap<String, Item> inventory;
    private TreeMap<String, Integer> duplicateItemsInInventory;
    private double maxHealth;
    private double currentHealth;
    private double[] damage = new double[2];
    private TreeMap<String, Feat> feats;
    private double currencyAmount;


    private Player(int inventorySlots, TreeMap<String, Item> inventory, int maxHealth, int minDamage, int maxDamage, TreeMap<String, Feat> feats, double currencyAmount) {
        this.inventorySlots = inventorySlots;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damage[0] = minDamage;
        this.damage[1] = maxDamage;
        this.inventory = Objects.requireNonNullElseGet(inventory, TreeMap::new);
        this.duplicateItemsInInventory = new TreeMap<>();
        this.feats = Objects.requireNonNullElseGet(feats, TreeMap::new);
        this.currencyAmount = currencyAmount;

        if (inventory != null && !isSpaceInInventory()) {
            this.inventorySlots = inventory.size();
        }
    }

    public static Player create() {
        return new Player(10, null, 10, 2, 5, null, 0);
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

            try {
                int iteration = 1;

                if (getItemFromInventory(item.getName()) != null) {
                    int amountOfDuplicates = 2;

                    duplicateItemsInInventory.put(item.getName(), amountOfDuplicates);

                    Item itemFromInventory = (Item)getItemFromInventory(item.getName()).clone();
                    itemFromInventory.setName(item.getName() + " " + iteration++);

                    inventory.remove(item.getName());
                    inventory.put(itemFromInventory.getName(), itemFromInventory);
                } else if (duplicateItemsInInventory.get(item.getName()) != null) {
                    iteration = duplicateItemsInInventory.get(item.getName());
                    duplicateItemsInInventory.remove(item.getName());
                    duplicateItemsInInventory.put(item.getName(), iteration++ + 1);
                }

                if (iteration == 1) {
                    inventory.put(item.getName(), item);
                } else {
                    Item itemCloned = (Item)item.clone();
                    itemCloned.setName(itemCloned.getName() + " " + iteration);
                    inventory.put(itemCloned.getName(), itemCloned);
                }

                inventorySlots++;
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void removeItemFromInventory(Item item) {

        int iteration = 1;

        String nameWithoutIteration = getStringWithoutIteration(item.getName());

        if (duplicateItemsInInventory.get(nameWithoutIteration) != null) {
            iteration = duplicateItemsInInventory.get(nameWithoutIteration);
            duplicateItemsInInventory.remove(nameWithoutIteration);

            iteration--;

            if (iteration > 1) {
                duplicateItemsInInventory.put(nameWithoutIteration, iteration);
            }
        }

        if (iteration == 0) {
            inventory.remove(item.getName());
        } else if (iteration == 1) {
            inventory.remove(item.getName());

            TreeMap<String, Item> newInventory = (TreeMap<String, Item>)getInventory().clone();

            for (Map.Entry<String, Item> itemEntry : newInventory.entrySet()) {
                Item itemFromEntry = itemEntry.getValue();

                if (nameWithoutIteration.equalsIgnoreCase(getStringWithoutIteration(itemFromEntry.getName()))) {
                    inventory.remove(itemFromEntry.getName());

                    try {
                        itemFromEntry.setName(nameWithoutIteration);
                        inventory.put(itemFromEntry.getName(), itemFromEntry);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                };
            }
        } else
        {
            TreeMap<String, Item> newInventory = (TreeMap<String, Item>)getInventory().clone();

            for (Map.Entry<String, Item> itemEntry : newInventory.entrySet()) {
                boolean isItemToBeRemoved = false;

                Item itemFromEntry = itemEntry.getValue();

                if (itemFromEntry.getName().equalsIgnoreCase(item.getName())) {
                    isItemToBeRemoved = true;
                }

                String nameWithoutIterationItemFromEntry = getStringWithoutIteration(itemFromEntry.getName());

                if (isNumeric(getStringIteration(itemFromEntry.getName()))) {
                    Integer iterationFromEntry = Integer.parseInt(getStringIteration(itemFromEntry.getName()));
                    iterationFromEntry--;
                    inventory.remove(itemFromEntry.getName());

                    if (iterationFromEntry == 0) {
                        itemFromEntry.setName(nameWithoutIterationItemFromEntry + " " + 1);
                    } else {
                        itemFromEntry.setName(nameWithoutIterationItemFromEntry + " " + iterationFromEntry);

                    }
                    if (!isItemToBeRemoved) {
                        addItemToInventory(itemFromEntry);
                    }
                }
            }
        }
    }

    private String getStringWithoutIteration(String itemName) {
        String[] splitName = itemName.split("\\s+");

        String nameWithoutIteration = splitName[0];

        for (int i = 1; i < splitName.length - 1; i++) {
            nameWithoutIteration += " " + splitName[i];

        }
        return nameWithoutIteration;
    }

    private String getStringIteration(String itemName) {
        String[] splitName = itemName.split("\\s+");

        return splitName[splitName.length - 1];
    }

    private boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
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

    public double getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(double currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public void subtractFromCurrencyAmount(int i) {
        currencyAmount -= i;
        if (currencyAmount < 0) {
            currencyAmount = 0;
        }
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

    public boolean isAlive() {
        if (currentHealth > 0) {
            return true;
        }

        return false;
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
