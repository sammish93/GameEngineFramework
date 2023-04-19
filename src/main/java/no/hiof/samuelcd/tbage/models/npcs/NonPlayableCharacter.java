package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public abstract class NonPlayableCharacter implements Serializable, Cloneable {

    private String name;
    private TreeMap<String, Ability> NpcAbilityPool;
    private TreeMap<String, Item> NpcItemTable;
    // ***********
    // Data type not decided yet for imagePath.
    // ***********
    private String imagePath;


    protected NonPlayableCharacter(String name, TreeMap<String, Ability> abilities, TreeMap<String, Item> items) {
        if (name != null) {
            this.setName(name);
        }

        NpcAbilityPool = Objects.requireNonNullElseGet(abilities, TreeMap::new);
        NpcItemTable = Objects.requireNonNullElseGet(items, TreeMap::new);
    }

    /**
     *
     * @return Returns a TreeMap of Abilities that a NonPlayerCharacter has.
     */
    public TreeMap<String, Ability> getNpcAbilityPool() {
        return NpcAbilityPool;
    }

    /**
     *
     * @param npcAbilityPool An existing TreeMap that replaces the Abilities of a NonPlayerCharacter.
     */
    public void setNpcAbilityPool(TreeMap<String, Ability> npcAbilityPool) {
        this.NpcAbilityPool = npcAbilityPool;
    }

    /**
     *
     * @param abilityName A string representing the name of an instantiated Ability object.
     * @return Returns an instantiated Ability object.
     */
    public Ability getAbilityFromAbilityPool(String abilityName) {
        return NpcAbilityPool.get(abilityName);
    }

    /**
     * Adds an ability to an already existing collection of abilities.
     * @param ability An instantiated Ability object.
     */
    public void addAbilityToAbilityPool(Ability ability) {
        NpcAbilityPool.put(ability.getName(), ability);
    }

    /**
     * Removes an ability from an already existing collection of abilities.
     * @param ability An instantiated Ability object.
     */
    public void removeAbilityFromAbilityPool(Ability ability) {
        NpcAbilityPool.remove(ability.getName());
    }

    /**
     * Removes an ability from an already existing collection of abilities.
     * @param abilityName A string representing the name of an instantiated Ability object.
     */
    public void removeAbilityFromAbilityPool(String abilityName) {
        NpcAbilityPool.remove(abilityName);
    }

    /**
     *
     * @return Returns a string representing the name of a NonPlayableCharacter.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name A string representing the name of an existing instantiated NonPlayableCharacter.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     *
     * @return Returns a TreeMap of Items that a NonPlayerCharacter has.
     */
    public TreeMap<String, Item> getNpcItemTable() {
        return NpcItemTable;
    }

    /**
     *
     * @param npcItemTable An existing TreeMap that replaces the Items of a NonPlayerCharacter.
     */
    public void setNpcItemTable(TreeMap<String, Item> npcItemTable) {
        NpcItemTable = npcItemTable;
    }

    /**
     *
     * @param itemName A string representing the name of an instantiated Item object.
     * @return Returns an instantiated Item object.
     */
    public Item getItemFromItemTable(String itemName) {
        return NpcItemTable.get(itemName);
    }

    /**
     * Adds an item to an already existing collection of items.
     * @param item An instantiated Item object.
     */
    public void addItemToItemTable(Item item) {
        NpcItemTable.put(item.getName(), item);
    }

    /**
     * Removes an item from an already existing collection of items.
     * @param item An instantiated Item object.
     */
    public void removeItemFromItemTable(Item item) {
        NpcItemTable.remove(item.getName());
    }

    /**
     * Removes an item from an already existing collection of items.
     * @param itemName A string representing the name of an instantiated Item object.
     */
    public void removeItemFromItemTable(String itemName) {
        NpcItemTable.remove(itemName);
    }

    /**
     * A method intended to be used to append to the toString() method of subclasses.
     * @param sb An instance of a StringBuilder class.
     * @see Ally#toString()
     * @see Enemy#toString()
     */
    protected void printItemTableAndAbilityPool(StringBuilder sb) {
        if (!getNpcItemTable().isEmpty()) {
            sb.append("\n\tItem Table: ");
            for (Map.Entry<String, Item> itemSet : getNpcItemTable().entrySet()) {
                Item item = itemSet.getValue();
                sb.append("\n\t\t" + item.toString());
            }
        }

        if (!getNpcAbilityPool().isEmpty()) {
            sb.append("\n\tAbility Table: ");
            for (Map.Entry<String, Ability> abilitySet : getNpcAbilityPool().entrySet()) {
                Ability ability = abilitySet.getValue();
                sb.append("\n\t\t" + ability.toString());
            }
        }
    }

    /**
     * This method is intended to be abe to provide functionality so that an Encounter can include
     * duplicate NonPlayableCharacters, but which can all behave individually.
     * @return Returns an NonPlayableCharacter object that is cloned from this specific object instance.
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    /**
     * Serialises to a local path. The file type extension is '.ser'.
     * @param path The location that the .ser file will be saved to, along with the file name.
     *             Example:
     *             nonPlayableCharacter.save("src/fileName");
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
     *             nonPlayableCharacter.save("src/fileName", "sav");
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
     *             var ally = (Ally) NonPlayableCharacter.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var enemy = (Enemy) NonPlayableCharacter.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static NonPlayableCharacter load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (NonPlayableCharacter) objectInputStream.readObject();
    }
}
