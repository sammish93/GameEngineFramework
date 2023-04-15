package no.hiof.samuelcd.tbage.models.props;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.UUID;

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

    public static Prop create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Prop(randomlyGeneratedId.toString());
    }

    public static Prop create(String name) {
        return new Prop(name);
    }


    public void onUse(GameEngine gameEngine) {
        // Item does this when it is used.
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

    public boolean isUseable() {
        return (onUseBehaviour != null);
    }

    public void setOnUseBehaviour(Useable useable) {
        this.onUseBehaviour = useable;
    }

    public Useable getOnUseBehaviour() {
        return onUseBehaviour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

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
