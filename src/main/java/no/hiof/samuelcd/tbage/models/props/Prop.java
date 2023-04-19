package no.hiof.samuelcd.tbage.models.props;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.UUID;

/**
 * This class is used to add props that can be interacted with (using the 'investigate' command) during runtime
 * in an Encounter.
 */
public class Prop implements Serializable, Cloneable, Useable {

    private String name;
    private Useable onUseBehaviour;
    private boolean isUsed;


    private Prop(String name) {
        if (name != null) {
            this.setName(name);
        }

        isUsed = false;
    }

    /**
     *
     * @return Returns an initialised Prop object with a default UUID value as a name.
     */
    public static Prop create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Prop(randomlyGeneratedId.toString());
    }

    /**
     *
     * @param name A string that is used as an identifier for this prop.
     * @return Returns an initialised Prop object.
     */
    public static Prop create(String name) {
        return new Prop(name);
    }


    /**
     * If a prop has no onUse behaviour set then it can still be interacted with, but a message will be displyed
     * to the player informing them that nothing happens. A prop's onUse() method can also only be called once.
     * The intention behind this is so that a prop can't be exploited (e.g. a player opens a chest prop, and
     * receives loot once and only once).
     * @param gameEngine The current instance of the GameEngine is required so that the lambda used can reference
     *                   other dependencies of the GameEngine class such as the player or current encounter.
     */
    public void onUse(GameEngine gameEngine) {
        if (!isUsed) {
            if (getOnUseBehaviour() != null) {
                try {
                    onUseBehaviour.onUse(gameEngine);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                isUsed = true;
            } else {
                gameEngine.printMessage("Interacting with this object does nothing.");
                isUsed = true;
            }
        } else {
            gameEngine.printMessage("You have already investigated this object.");
        }

    }

    /**
     *
     * @return Returns true in the event that there is an on-use behavior set that can execute when the onUse()
     * method is called.
     */
    public boolean isUseable() {
        return (onUseBehaviour != null);
    }

    /**
     *
     * @param useable Intended to use a lambda to allow developers to create custom behaviours when a specific
     *                ability is used.
     *                Example of a lambda created using the generic Useable interface:
     *                Useable onUseSwitch = (gameEngine) -> {
     *                  gameEngine.printMessage("A door unlocks in the distance..");
     *                  var encounter = EncounterTraversalController.getCurrentEncounter();
     *                  encounter.setDefeated(true);
     *                };
     */
    public void setOnUseBehaviour(Useable useable) {
        this.onUseBehaviour = useable;
    }

    /**
     *
     * @return Returns the generic Useable interface if it exists.
     */
    public Useable getOnUseBehaviour() {
        return onUseBehaviour;
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
     * @return Returns a boolean value whether or not this prop has already been used (interacted/investigated
     * with) by a player.
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     *
     * @param used Sets whether or not this object has been used.
     */
    public void setUsed(boolean used) {
        isUsed = used;
    }

    /**
     * This method is intended to be abe to provide functionality so that a single Encounter instance can include
     * duplicate props, but which can all be interacted with individually.
     * @return Returns a Prop object that is cloned from this specific object instance.
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
        return "Prop Name: '" + name + "', " +
                "Is Useable: " + isUseable() + ", " +
                "Is Used: " + isUsed;
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             prop.save("src/fileName");
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
     *             prop.save("src/fileName", "sav");
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
     *             var prop = Prop.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var prop = Prop.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static Prop load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Prop) objectInputStream.readObject();
    }
}
