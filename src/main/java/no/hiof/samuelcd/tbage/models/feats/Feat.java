package no.hiof.samuelcd.tbage.models.feats;

import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.UUID;

/**
 * This class is used by both the Player and the Encounter classes to add functionality that allow a developer
 * to add feat checks and rewards to their own game permutation. One example of use includes custom code in
 * the generic Useable interface lambda provided to the setOnInitiationBehaviour() to check to see if a specific
 * feat is present in the encounter's feat check TreeMap, and then to check whether a player has obtained that
 * feat, and thereafter alter the behavior based on this.
 *
 * Example:
 * Useable onUseInitiation = (gameEngine) -> {
 *   var player = gameEngine.getPlayer();
 *   if (player.getFeatFromFeats("TAKES_DAMAGE_FROM_RUSTY_NAIL_UPON_COMBAT_ENCOUNTER_ENTRY") == null) {
 *     player.subtractFromCurrentHealth(6);
 *     gameEngine.printMessage("On entering the encounter you suffered 6 damage from a rusty doornail!");
 *   } else {
 *     gameEngine.printMessage("Thanks to your heightened agility from your mead-drinking antics, you manage " +
 *       "to skip past a precarious rusty doornail. That could have been deadly!");
 *   }
 * };
 *
 * encounter.setOnInitiationBehaviour(onUseInitiation);
 * @see no.hiof.samuelcd.tbage.models.encounters.Encounter#setOnInitiationBehaviour(Useable)
 */
public class Feat implements Serializable {

    private String name;
    private boolean isSecret;


    private Feat(String name, boolean isSecret) {
        this.name = name;
        this.isSecret = isSecret;
    }

    /**
     *
     * @return Returns an initialised Feat object with a default UUID value as a name and a default isSecret value
     * of false.
     */
    public static Feat create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Feat(randomlyGeneratedId.toString(), false);
    }

    /**
     *
     * @param name A string that is used as an identifier for this feat.
     * @return Returns an initialised Feat object with a default isSecret value of false.
     */
    public static Feat create(String name) {
        return new Feat(name, false);
    }

    /**
     *
     * @param name A string that is used as an identifier for this feat.
     * @param isSecret A boolean value that determines whether a feat is hidden or not from a player. Secret
     *                 feats can be used to control behaviours that the developer may not intend to show to a
     *                 player.
     * @return Returns an initialised Feat object.
     */
    public static Feat create(String name, boolean isSecret) {
        return new Feat(name, isSecret);
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
     * @return Returns a boolean value of true if the feat is secret.
     */
    public boolean isSecret() {
        return isSecret;
    }

    /**
     *
     * @param secret Sets the boolean value of whether or not the feat is secret. If the feat is secret, then
     *               the player will not receive a message in the game interface upon receiving the feat.
     */
    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
    @Override
    public String toString() {
        return "Feat Name: '" + name + "', " +
                "isSecret: " + isSecret;
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             feat.save("src/fileName");
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
     *             feat.save("src/fileName", "sav");
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
     *             var feat = Feat.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var feat = Feat.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * underscores, and forward slashes.
     */
    public static GameSettings load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (GameSettings) objectInputStream.readObject();
    }
}
