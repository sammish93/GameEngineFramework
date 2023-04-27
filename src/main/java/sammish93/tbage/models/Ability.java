package sammish93.tbage.models;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.interfaces.Useable;
import sammish93.tbage.tools.StringParser;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.UUID;

/**
 * This class is used as means of giving an Enemy object an alternative option other than a melee attack.
 * An enemy can have multiple abilities in its pool, and an algorithm will run, based on weighted value
 * percentages, and the enemy's isMelee boolean value, along with its melee chance per turn.
 */
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


    /**
     *
     * @return Returns an initialised Ability object with a default UUID value as a name, and a 50% probability
     * chance for the ability to be chosen on an enemy's turn (weighted probability).
     * @throws InvalidValueException Is thrown if the ability probability is less than or equal to 0, or greater
     * than 1.
     */
    public static Ability create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Ability(randomlyGeneratedId.toString(), 0.5);
    }

    /**
     *
     * @param name A string that is used as an identifier for this ability.
     * @return Returns an initialised Ability object with a 50% probability chance for the ability to be
     * chosen on an enemy's turn (weighted probability).
     * @throws InvalidValueException Is thrown if the ability probability is less than or equal to 0, or greater
     * than 1.
     */
    public static Ability create(String name) throws InvalidValueException {
        return new Ability(name,  0.5);
    }

    /**
     *
     * @param name A string that is used as an identifier for this ability.
     * @param abilityProbabilityPerTurn A double value greater than 0, and less than or equal to 1. The value
     *                                  0.5 is equivalent to 50%, while 1 is equivalent to 100%. This probability
     *                                  chance is weighted, and is calculated via the StringParser class.
     * @return Returns an initialised Ability object based on the parameters provided.
     * @throws InvalidValueException Is thrown if the ability probability is less than or equal to 0, or greater
     * than 1.
     * @see StringParser
     */
    public static Ability create(String name, double abilityProbabilityPerTurn) throws InvalidValueException {
        return new Ability(name, abilityProbabilityPerTurn);
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
     *                Useable fireballAbility = (gameEngine) -> {
     *                  var player = gameEngine.getPlayer();
     *                  player.subtractFromCurrentHealth(5);
     *                  gameEngine.printMessage("You have taken 5 damage from a fireball!");
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
     * @return Returns a decimal value representing the probability chance of an ability being chosen to be
     * executed during an enemy's turn. This probability chance is weighted, and is calculated via the
     * StringParser class.
     */
    public double getAbilityProbabilityPerTurn() {
        return abilityProbabilityPerTurn;
    }

    /**
     *
     * @param abilityProbabilityPerTurn A decimal value representing the probability chance of an ability being
     *                                  chosen to be executed during an enemy's turn. The value must be greater
     *                                  than 0, and less than or equal to 1. The value 0.5 is equivalent to 50%,
     *                                  while 1 is equivalent to 100%. This probability chance is weighted, and
     *                                  is calculated via the StringParser class.
     * @throws InvalidValueException Is thrown if the ability probability is less than or equal to 0, or greater
     * than 1.
     */
    public void setAbilityProbabilityPerTurn(double abilityProbabilityPerTurn) throws InvalidValueException {
        if (abilityProbabilityPerTurn <= 1.00 && abilityProbabilityPerTurn > 0) {
            this.abilityProbabilityPerTurn = abilityProbabilityPerTurn;
        } else {
            throw new InvalidValueException("Value " + abilityProbabilityPerTurn + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
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
