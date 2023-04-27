package sammish93.tbage.models;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.interfaces.Useable;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.UUID;

/**
 * A class used to model items that can be stored in a player's inventory, dropped by an enemy, or sold by an ally.
 * Items can be used by a player.
 */
public class Item implements Useable, Serializable, Cloneable {

    private String name;
    private int value;
    private double dropChance;
    // ***********
    // Data type not decided yet for imagePath.
    // ***********
    private String imagePath;
    private Useable useable;
    private int numberOfUses;


    private Item(String name, int value, double dropChance, int numberOfUses) throws InvalidValueException {
        this.name = name;

        if (value >= 0) {
            this.value = value;
        } else {
            throw new InvalidValueException("Value " + value + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }

        if (dropChance <= 1.00 && dropChance > 0) {
            this.dropChance = dropChance;
        } else {
            throw new InvalidValueException("Value " + dropChance + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }

        if (numberOfUses >= 0) {
            this.numberOfUses = numberOfUses;
        } else {
            throw new InvalidValueException("Value " + numberOfUses + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
    }


    /**
     *
     * @return Returns an initialised Item object with a default UUID value as a name, a default currency value of 0,
     * a 50% probability chance for the item to be dropped on an Enemy being defeated, and an infinite number of uses.
     * @throws InvalidValueException Is thrown if the item drop chance is less than or equal to 0, or greater
     * than 1, or if the currency value or number of uses is a negative integer value.
     */
    public static Item create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Item(randomlyGeneratedId.toString(), 0, 0.5, 0);
    }

    /**
     *
     * @return Returns an initialised Item object with a default currency value of 0,
     * a 50% probability chance for the item to be dropped on an Enemy being defeated, and an infinite number of uses.
     * @param name A string that is used as an identifier for this item.
     * @throws InvalidValueException Is thrown if the item drop chance is less than or equal to 0, or greater
     * than 1, or if the currency value or number of uses is a negative integer value.
     */
    public static Item create(String name) throws InvalidValueException {
        return new Item(name, 0, 0.5, 0);
    }

    /**
     *
     * @return Returns an initialised Item object with a 50% probability chance for the item to be dropped on an Enemy
     * being defeated, and an infinite number of uses.
     * @param name A string that is used as an identifier for this item.
     * @param value A positive integer value determining the currency value of this item. Used during interaction
     *              (trading) with an Ally.
     * @throws InvalidValueException Is thrown if the item drop chance is less than or equal to 0, or greater
     * than 1, or if the currency value or number of uses is a negative integer value.
     */
    public static Item create(String name, int value) throws InvalidValueException {
        return new Item(name, value, 0.5, 0);
    }

    /**
     *
     * @return Returns an initialised Item object with an infinite number of uses.
     * @param name A string that is used as an identifier for this item.
     * @param value A positive integer value determining the currency value of this item. Used during interaction
     *              (trading) with an Ally.
     * @param dropChance A decimal value greater than 0 and less than or equal to 1 that determines the chance of
     *                   an item being dropped upon an Enemy being defeated.
     *                   NOTE: This value is not a weighted probability (unlike various other probabilities in this
     *                   framework), and a single enemy can drop multiple items (or no items at all).
     * @throws InvalidValueException Is thrown if the item drop chance is less than or equal to 0, or greater
     * than 1, or if the currency value or number of uses is a negative integer value.
     */
    public static Item create(String name, int value, double dropChance) throws InvalidValueException {
        return new Item(name, value, dropChance, 0);
    }

    /**
     *
     * @return Returns an initialised Item object.
     * @param name A string that is used as an identifier for this item.
     * @param value A positive integer value determining the currency value of this item. Used during interaction
     *              (trading) with an Ally.
     * @param dropChance A decimal value greater than 0 and less than or equal to 1 that determines the chance of
     *                   an item being dropped upon an Enemy being defeated.
     *                   NOTE: This value is not a weighted probability (unlike various other probabilities in this
     *                   framework), and a single enemy can drop multiple items (or no items at all).
     * @param numberOfUses A positive integer value determining how many times a player can use this item.
     * @throws InvalidValueException Is thrown if the item drop chance is less than or equal to 0, or greater
     * than 1, or if the currency value or number of uses is a negative integer value.
     */
    public static Item create(String name, int value, double dropChance, int numberOfUses)
            throws InvalidValueException {
        return new Item(name, value, dropChance, numberOfUses);
    }


    /**
     *
     * @param gameEngine The current instance of the GameEngine is required so that the lambda used can reference
     *                   other dependencies of the GameEngine class such as the player or current encounter.
     */
    public void onUse(GameEngine gameEngine) {
        try {
            useable.onUse(gameEngine);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return Returns true in the event that there is an on-use behavior set that can execute when the onUse()
     * method is called.
     */
    public boolean isUseable() {
        return (useable != null);
    }

    /**
     *
     * @param useable Intended to use a lambda to allow developers to create custom behaviours when a specific
     *                ability is used.
     *                Example of a lambda created using the generic Useable interface:
     *                Useable onUse = (gameEngine) -> {
     *                  var player = gameEngine.getPlayer();
     *                  player.addToCurrentHealth(5);
     *
     *                  gameEngine.printMessage("You take a sip of the health potion and recover 5 health!");
     *                };
     */
    public void setOnUseBehaviour(Useable useable) {
        this.useable = useable;
    }

    /**
     *
     * @return Returns the generic Useable interface if it exists.
     */
    public Useable getOnUseBehaviour() {
        return useable;
    }

    /**
     *
     * @return Returns a string representing the name of this object. If no name is set then an UUID converted
     * to a string will be returned.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name Sets the name of this object to a new string value.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return Returns the currency value of this item.
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @param value Sets the currency value of the item. This is used during interaction (trading) with an Ally.
     * @throws InvalidValueException Is thrown in the event that a negative integer value is provided.
     */
    public void setValue(int value) throws InvalidValueException {
        if (value >= 0) {
            this.value = value;
        } else {
            throw new InvalidValueException("Value " + value + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
    }

    /**
     *
     * @return Returns a decimal value of the drop chance of this item upon Enemy being defeated. A value of 0.5
     * is equivalent to a 50% chance, and a value of 1 is equivalent to a 100% chance.
     * NOTE: This value is not a weighted probability (unlike various other probabilities in this
     * framework), and a single enemy can drop multiple items (or no items at all).
     */
    public double getDropChance() {
        return dropChance;
    }

    /**
     *
     * @param dropChance Sets a positive integer value of the drop chance of this item upon Enemy being defeated.
     *                   A value of 0.5 is equivalent to a 50% chance, and a value of 1 is equivalent to a 100%
     *                   chance.
     *                   NOTE: This value is not a weighted probability (unlike various other probabilities in
     *                   this framework), and a single enemy can drop multiple items (or no items at all).
     * @throws InvalidValueException Is thrown in the event that a negative integer value is provided.
     */
    public void setDropChance(double dropChance) throws InvalidValueException {
        if (dropChance <= 1.00 && dropChance > 0) {
            this.dropChance = dropChance;
        } else {
            throw new InvalidValueException("Value " + dropChance + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     *
     * @return Returns the number of uses remaining of this specific item.
     */
    public int getNumberOfUses() {
        return numberOfUses;
    }

    /**
     *
     * @param numberOfUses Sets the number of uses to an integer value greater than or equal to 0.
     *                     NOTE: If the number of uses is set to 0 then this item will be considered to have
     *                     infinite number of uses.
     * @throws InvalidValueException Is thrown in the event that a negative integer value is provided.
     */
    public void setNumberOfUses(int numberOfUses) throws InvalidValueException {
        if (numberOfUses >= 0) {
            this.numberOfUses = numberOfUses;
        } else {
            throw new InvalidValueException("Value " + numberOfUses + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
    }

    /**
     * This method is intended to be abe to provide functionality so that a player inventory can include
     * duplicate items, but which can all be used individually.
     * @return Returns an Item object that is cloned from this specific object instance.
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
    @Override
    public String toString() {
        double i = dropChance * 100;
        return "Item Name: '" + name + "', " +
                "Value: " + value + ", " +
                "Drop Chance: " + (int) i + "%, " +
                "Is Useable: " + isUseable();
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             item.save("src/fileName");
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
     *             item.save("src/fileName", "sav");
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
     *             var item = Item.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var item = Item.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static Item load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Item) objectInputStream.readObject();
    }
}
