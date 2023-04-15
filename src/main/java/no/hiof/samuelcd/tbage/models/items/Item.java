package no.hiof.samuelcd.tbage.models.items;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.UUID;

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


    public static Item create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Item(randomlyGeneratedId.toString(), 0, 0.5, 0);
    }

    public static Item create(String name) throws InvalidValueException {
        return new Item(name, 0, 0.5, 0);
    }

    public static Item create(String name, int value) throws InvalidValueException {
        return new Item(name, value, 0.5, 0);
    }

    public static Item create(String name, int value, double dropChance) throws InvalidValueException {
        return new Item(name, value, dropChance, 0);
    }
    public static Item create(String name, int value, double dropChance, int numberOfUses) throws InvalidValueException {
        return new Item(name, value, dropChance, numberOfUses);
    }


    public void onUse(GameEngine gameEngine) {
        try {
            useable.onUse(gameEngine);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isUseable() {
        return (useable != null);
    }

    public void setOnUseBehaviour(Useable useable) {
        this.useable = useable;
    }

    public Useable getOnUseBehaviour() {
        return useable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) throws InvalidValueException {
        if (value >= 0) {
            this.value = value;
        } else {
            throw new InvalidValueException("Value " + value + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
    }

    public double getDropChance() {
        return dropChance;
    }

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

    public int getNumberOfUses() {
        return numberOfUses;
    }

    public void setNumberOfUses(int numberOfUses) throws InvalidValueException {
        if (numberOfUses >= 0) {
            this.numberOfUses = numberOfUses;
        } else {
            throw new InvalidValueException("Value " + numberOfUses + " is invalid. Enter an integer " +
                    "value greater than or equal to 0");
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

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
