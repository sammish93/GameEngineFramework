package sammish93.tbage.models;

import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A class that models a player's player character. This character persists throughout the life of the game, and
 * a player can traverse multiple encounters using the same player character. This class also includes an
 * inventory system that allows a player to trade and loot Item objects, a feat system that allows a developer to
 * change future encounter behaviours based on specific feats earned by the player in previous encounters, as well
 * as hit points and damage values that allow a player to engage in combat.
 */
public class Player implements Serializable {

    private int inventorySlots ;
    private TreeMap<String, Item> inventory;
    private TreeMap<String, Integer> duplicateItemsInInventory;
    private double maxHealth;
    private double currentHealth;
    private double[] damage = new double[2];
    private TreeMap<String, Feat> feats;
    private double currencyAmount;


    private Player(int maxHealth, int minDamage, int maxDamage, TreeMap<String, Feat> feats,
                   double currencyAmount, int inventorySlots, TreeMap<String, Item> inventory)
            throws InvalidValueException {
        if (inventorySlots >= 0) {
            this.inventorySlots = inventorySlots;
        } else {
            throw new InvalidValueException("Value " + inventorySlots + " is invalid. Enter an integer " +
                    "value greater than 0");
        }

        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
            this.currentHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + maxHealth + " is invalid. Enter a decimal " +
                    "value greater than 0");
        }

        if (minDamage >= 0) {
            damage[0] = minDamage;
        } else {
            throw new InvalidValueException("Value " + minDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }

        if (maxDamage >= 0) {
            damage[1] = maxDamage;
        } else {
            throw new InvalidValueException("Value " + maxDamage + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }

        this.inventory = Objects.requireNonNullElseGet(inventory, TreeMap::new);
        this.duplicateItemsInInventory = new TreeMap<>();
        this.feats = Objects.requireNonNullElseGet(feats, TreeMap::new);

        if (currencyAmount >= 0) {
            this.currencyAmount = currencyAmount;
        } else {
            throw new InvalidValueException("Value " + currencyAmount + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }

        if (inventory != null && !isSpaceInInventory()) {
            this.inventorySlots = inventory.size();
        }
    }

    /**
     *
     * @return Returns an initialised Player object with default values. Current and maximum health is set to 10,
     * minimum damage is set to 2, maximum damage is set to 5, currency is set to 100, and inventory slots is set to 10.
     * @throws InvalidValueException Thrown in the event that the player's maximum health is set to a value less than
     * or equal to 0, or the minimum or maximum damage values are set to a negative integer value.
     */
    public static Player create() throws InvalidValueException {
        return new Player(10, 2, 5, null, 100, 10,
                null);
    }

    /**
     *
     * @param maxHealth A positive integer value that determines both the current and maximum health of the player.
     * @param minDamage A non-negative integer value that determines the minimum damage that a player can inflict
     *                  upon attacking an Enemy.
     * @param maxDamage A non-negative integer value that determines the maximum damage that a player can inflict
     *                  upon attacking an Enemy.
     * @param feats A collection of instantiated Feat objects that a player has acquired.
     * @param currencyAmount The amount of currency that a player has.
     * @param inventorySlots The amount of inventory slots that a player has. A player that attempts to buy or loot
     *                       an item while having a full (as many items as inventory slots) inventory will result in
     *                       an InventoryFullException being thrown.
     * @param inventory A collection of instantiated Item objects that a player has acquired.
     * @return Returns an initialised Player object.
     * @throws InvalidValueException Thrown in the event that the player's maximum health is set to a value less than
     * or equal to 0, or the minimum or maximum damage values are set to a negative integer value.
     * @see InventoryFullException
     */
    public static Player create(int maxHealth, int minDamage, int maxDamage, TreeMap<String, Feat> feats,
                                double currencyAmount, int inventorySlots, TreeMap<String, Item> inventory)
            throws InvalidValueException {
        return new Player(maxHealth, minDamage, maxDamage, feats, currencyAmount, inventorySlots, inventory);
    }


    private boolean isSpaceInInventory() {
        int inventorySize = inventory.size();

        return inventorySize < inventorySlots;
    }

    /**
     *
     * @return Returns the amount of inventory slots that a player has (including slots that are occupied).
     */
    public int getInventorySlots() {
        return inventorySlots;
    }

    /**
     *
     * @param inventorySlots A positive integer value determining how many slots a player has.
     * @throws InvalidValueException Thrown in the event that an integer value less than or equal to 0 is provided.
     */
    public void setInventorySlots(int inventorySlots) throws InvalidValueException {
        if (inventorySlots >= 0) {
            this.inventorySlots = inventorySlots;
        } else {
            throw new InvalidValueException("Value " + inventorySlots + " is invalid. Enter an integer " +
                    "value greater than 0");
        }
    }

    /**
     *
     * @return Returns a TreeMap of items in a player's inventory. The key is a string representing the item's name,
     * and the value is the Item object itself.
     */
    public TreeMap<String, Item> getInventory() {
        return inventory;
    }

    /**
     *
     * @param inventory Sets the inventory to an already existing TreeMap.
     */
    public void setInventory(TreeMap<String, Item> inventory) {
        this.inventory = inventory;
    }

    /**
     *
     * @param itemName A string value representing the name of an item.
     * @return Returns an Item object.
     */
    public Item getItemFromInventory(String itemName) {
        return inventory.get(itemName);
    }

    /**
     * This class is used to add an item to an inventory. It is also used in the event that duplicate items
     * exist in the inventory at the same time, in which an index value is added to the suffix of an item's name.
     * (e.g. 'Health Potion' is in a player's inventory. The player loots another 'Health Potion', and the first item
     * is renamed to 'Health Potion 1', while the second is renamed to 'Health Potion 2'. Once one of these items
     * is consumed, the other item wil be renamed back to 'Health Potion' without the suffix.
     * @param item An instantiated Item object.
     * @throws InventoryFullException Thrown in the event that an item is added to the inventory when a player
     * doesn't have enough inventory space.
     */
    public void addItemToInventory(Item item) throws InventoryFullException {
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

            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new InventoryFullException("You cannot add " + item.getName() + " to your inventory because" +
                    " your inventory is full.");
        }
    }

    /**
     *
     * @param itemName A string value representing the name of an item object.
     * @throws InventoryFullException Thrown in the event that an item is added to the inventory when a player
     * doesn't have enough inventory space.
     * @see Player#addItemToInventory(Item)
     */
    public void addItemToInventory(String itemName) throws InventoryFullException {
        Item item = getItemFromInventory(itemName);
        addItemToInventory(item);
    }

    /**
     * This class is used to remove an item to an inventory. It is also used in the event that duplicate items
     * exist in the inventory at the same time, in which an index value is added to the suffix of an item's name.
     * (e.g. 'Health Potion' is in a player's inventory. The player loots another 'Health Potion', and the first item
     * is renamed to 'Health Potion 1', while the second is renamed to 'Health Potion 2'. Once one of these items
     * is consumed, the other item wil be renamed back to 'Health Potion' without the suffix.
     * @param item An instantiated Item object.
     * @throws InventoryFullException Thrown in the event that an item is added to the inventory when a player
     * doesn't have enough inventory space.
     */
    public void removeItemFromInventory(Item item) throws InventoryFullException {

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

    /**
     *
     * @param itemName A string value representing the name of an item object.
     * @throws InventoryFullException Thrown in the event that an item is added to the inventory when a player
     * doesn't have enough inventory space.
     * @see Player#removeItemFromInventory(Item)
     */
    public void removeItemFromInventory(String itemName) {
        inventory.remove(itemName);
        inventorySlots--;
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

    /**
     *
     * @return Returns all feats that a player has acquired.
     */
    public TreeMap<String, Feat> getFeats() {
        return feats;
    }

    /**
     *
     * @param feats Sets a player's acquired feats to an existing TreeMap.
     */
    public void setFeats(TreeMap<String, Feat> feats) {
        this.feats = feats;
    }

    /**
     *
     * @param featName A string representing a feat's name.
     * @return Returns an instantiated Feat object.
     */
    public Feat getFeatFromFeats(String featName) {
        return feats.get(featName);
    }

    /**
     *
     * @param feat Adds an instantiated Feat object to existing acquired feats.
     */
    public void addFeatToFeats(Feat feat) {
        feats.put(feat.getName(), feat);
    }

    /**
     * Removes an existing feat from a player's acquired feats.
     * @param feat An instantiated Feat object.
     */
    public void removeFeatFromFeats(Feat feat) {
        feats.remove(feat.getName());
    }

    /**
     * Removes an existing feat from a player's acquired feats.
     * @param featName A string representing an instantiated Feat object.
     */
    public void removeFeatFromFeats(String featName) {
        feats.remove(featName);
    }

    /**
     *
     * @return Returns a decimal value representing the maximum health of a player.
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the maximum health of a player.
     * @param maxHealth A positive integer value.
     * @throws InvalidValueException Thrown when a value less than or equal to 0 is provided.
     */
    public void setMaxHealth(int maxHealth) throws InvalidValueException {
        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + maxHealth + " is invalid. Enter an integer " +
                    "value greater than 0");
        }
    }

    /**
     *
     * @return Returns a decimal value representing a player's current health.
     */
    public double getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current health of a player.
     * @param currentHealth A positive integer value.
     * @throws InvalidValueException Thrown when a value less than or equal to 0 is provided.
     */
    public void setCurrentHealth(int currentHealth) throws InvalidValueException {
        if (currentHealth >= 0) {
            this.currentHealth = maxHealth;
        } else {
            throw new InvalidValueException("Value " + currentHealth + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
    }

    /**
     *
     * @return Returns a decimal value representing the minimum amount of damage a player can inflict to an
     * Enemy during combat.
     */
    public double getMinDamage() {
        return damage[0];
    }

    /**
     * Sets the minimum amount of damage a player can inflict to an Enemy during combat.
     * @param minDamage An integer value greater than or equal to 0.
     */
    public void setMinDamage(int minDamage) {
        this.damage[0] = minDamage;
    }

    /**
     *
     * @return Returns a decimal value representing the maximum amount of damage a player can inflict to an
     * Enemy during combat.
     */
    public double getMaxDamage() {
        return damage[1];
    }

    /**
     * Sets the maximum amount of damage a player can inflict to an Enemy during combat.
     * @param maxDamage An integer value greater than or equal to 0.
     */
    public void setMaxDamage(int maxDamage) {
        this.damage[1] = maxDamage;
    }

    /**
     *
     * @return Returns the amount of currency that a player has.
     */
    public double getCurrencyAmount() {
        return currencyAmount;
    }

    /**
     * Sets the amount of currency that a player has.
     * @param currencyAmount A non-negative decimal value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
    public void setCurrencyAmount(double currencyAmount) throws InvalidValueException {
        if (currencyAmount >= 0) {
            this.currencyAmount = currencyAmount;
        } else {
            throw new InvalidValueException("Value " + currencyAmount + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    /**
     * Sets the amount of currency that a player has.
     * @param currencyAmount A non-negative integer value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
    public void setCurrencyAmount(int currencyAmount) throws InvalidValueException {
        if (currencyAmount >= 0) {
            this.currencyAmount = currencyAmount;
        } else {
            throw new InvalidValueException("Value " + currencyAmount + " is invalid. Enter a decimal " +
                    "value greater than or equal to 0");
        }
    }

    /**
     * A method intended to reduce the amount of code a developer has to write when subtracting currency from a
     * player's total currency.
     * @param i A positive integer value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
    public void subtractFromCurrencyAmount(int i) throws InvalidValueException {
        if (i >= 0) {
            currencyAmount -= i;
        } else {
            throw new InvalidValueException("Value " + i + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
        if (currencyAmount < 0) {
            currencyAmount = 0;
        }
    }

    /**
     * A method intended to reduce the amount of code a developer has to write when adding currency to a
     * player's total currency.
     * @param i A positive integer value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
    public void addToCurrencyAmount(int i) throws InvalidValueException {
        if (i >= 0) {
            currencyAmount += i;
        } else {
            throw new InvalidValueException("Value " + i + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
    }

    /**
     * A method intended to reduce the amount of code a developer has to write when subtracting health from a
     * player's current health total.
     * @param i A positive integer value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
    public void subtractFromCurrentHealth(int i) throws InvalidValueException {
        if (i >= 0) {
            currentHealth -= i;
        } else {
            throw new InvalidValueException("Value " + i + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    /**
     * A method intended to reduce the amount of code a developer has to write when adding health to a
     * player's current health total.
     * @param i A positive integer value.
     * @throws InvalidValueException Thrown in the event of a negative value being provided.
     */
    public void addToCurrentHealth(int i) throws InvalidValueException {
        if (i >= 0) {
            currentHealth += i;
        } else {
            throw new InvalidValueException("Value " + i + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    /**
     *
     * @return Returns true if a player is considered alive (current health is greater than 0).
     */
    public boolean isAlive() {
        if (currentHealth > 0) {
            return true;
        }

        return false;
    }

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Current Health: " + (int) currentHealth + ", " +
                "Maximum Health: " + (int) maxHealth + ", " +
                "Minimum Damage: " + (int) damage[0] + ", " +
                "Maximum Damage: " + (int) damage[1] + ", " +
                "Currency Amount: " + (int)currencyAmount + ", " +
                "Inventory Slots: " + inventorySlots);

        if (!getInventory().isEmpty()) {
            sb.append("\nInventory Table: ");
            for (Map.Entry<String, Item> inventorySet : getInventory().entrySet()) {
                Item item = inventorySet.getValue();
                sb.append("\n\t" + item.toString());
            }
        }

        if (!getFeats().isEmpty()) {
            sb.append("\nFeat Table: ");
            for (Map.Entry<String, Feat> featSet : getFeats().entrySet()) {
                Feat feat = featSet.getValue();
                sb.append("\n\t" + feat.toString());
            }
        }

        return sb.toString();
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             player.save("src/fileName");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws InvalidPathException Arises if the path contains characters other than the English alphabet,
     * underscores, and forward slashes.
     */
    public void save(String path) throws IOException {
        boolean isOnlyAlphaNumericAndUnderscores = path.matches("^[a-zA-Z0-9_/]*$");

        if (isOnlyAlphaNumericAndUnderscores) {
            FileOutputStream fileOutputStream = new FileOutputStream(path + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this);
        } else {
            throw new InvalidPathException(path, "The path isn't recognised as a valid file path.");
        }

    }

    /**
     * Serialises to a local path. The file type extension is decided by the developer.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             player.save("src/fileName", "sav");
     * @param fileExtension The file type extension the developer wishes to save the file as.
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws InvalidPathException Arises if the path contains characters other than the English alphabet,
     * underscores, and forward slashes.
     */
    public void save(String path, String fileExtension) throws IOException {
        boolean isOnlyAlphaNumericAndUnderscores = path.matches("^[a-zA-Z0-9_/]*$");

        if (isOnlyAlphaNumericAndUnderscores) {
            FileOutputStream fileOutputStream = new FileOutputStream(path + "." + fileExtension);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this);
        } else {
            throw new InvalidPathException(path, "The path isn't recognised as a valid file path.");
        }

    }

    /**
     * Serialises to a local path. The file type extension is only required when the extension is something other
     * than '.ser'.
     * @param path The location that the file is located at. This can either be with or without the file extension.
     *             Examples:
     *             var player = Player.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var player = Player.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static Player load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Player) objectInputStream.readObject();
    }
}
