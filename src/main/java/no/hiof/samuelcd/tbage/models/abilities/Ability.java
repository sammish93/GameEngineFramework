package no.hiof.samuelcd.tbage.models.abilities;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.UUID;

public class Ability implements Useable, Serializable {

    private String name;
    private double abilityProbabilityPerTurn;
    private Useable useable;


    private Ability(String name, double abilityProbabilityPerTurn) throws InvalidValueException {
        this.name = name;
        if (abilityProbabilityPerTurn <= 1.00 && abilityProbabilityPerTurn > 0) {
            this.abilityProbabilityPerTurn = abilityProbabilityPerTurn;
        } else {
            throw new InvalidValueException("Value " + abilityProbabilityPerTurn + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }


    public static Ability create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Ability(randomlyGeneratedId.toString(), 0.5);
    }

    public static Ability create(String name) throws InvalidValueException {
        return new Ability(name,  0.5);
    }

    public static Ability create(String name, double abilityProbabilityPerTurn) throws InvalidValueException {
        return new Ability(name, abilityProbabilityPerTurn);
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

    public double getAbilityProbabilityPerTurn() {
        return abilityProbabilityPerTurn;
    }

    public void setAbilityProbabilityPerTurn(double abilityProbabilityPerTurn) throws InvalidValueException {
        if (abilityProbabilityPerTurn <= 1.00 && abilityProbabilityPerTurn > 0) {
            this.abilityProbabilityPerTurn = abilityProbabilityPerTurn;
        } else {
            throw new InvalidValueException("Value " + abilityProbabilityPerTurn + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }

    @Override
    public String toString() {
        double i = abilityProbabilityPerTurn * 100;
        return "Ability Name: '" + name + "', " +
                "Probability Per Turn: " + (int) i + "%";
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             ability.save("src/fileName");
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
     *             ability.save("src/fileName", "sav");
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
     *             var ability = Ability.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var ability = Ability.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static Ability load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Ability) objectInputStream.readObject();
    }
}
