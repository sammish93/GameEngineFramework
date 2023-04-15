package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.exceptions.InventoryFullException;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.props.Prop;
import no.hiof.samuelcd.tbage.tools.StringParser;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public abstract class Encounter implements Serializable {
    private String name;
    private String imagePath;
    private String introductoryMessage;
    private String onDefeatedMessage;
    private String hint;
    private TreeMap<String, Feat> featChecks;
    private TreeMap<String, Feat> featRewards;
    private TreeMap<String, String> navigationOptions;
    private ArrayList<String> navigationalVerbs;
    private TreeMap<String, Prop> props;
    private Useable onInitiationBehaviour;
    private boolean isDefeated = false;
    private boolean isIntroductionPrinted = false;
    private boolean isBacktracking = false;


    protected Encounter(String name, String imagePath, TreeMap<String, Feat> featChecks, TreeMap<String, Feat> featRewards, TreeMap<String, String> navigationOptions, TreeMap<String, Prop> props) throws InvalidValueException {
        this.name = name;
        this.imagePath = imagePath;

        this.featChecks = Objects.requireNonNullElseGet(featChecks, TreeMap::new);
        this.featRewards = Objects.requireNonNullElseGet(featRewards, TreeMap::new);
        this.navigationOptions = Objects.requireNonNullElseGet(navigationOptions, TreeMap::new);
        this.props = Objects.requireNonNullElseGet(props, TreeMap::new);
        navigationalVerbs = new ArrayList<>();
        introductoryMessage = "";
        onDefeatedMessage = "";
        hint = "";

        addDefaultNavigationalVerbs();
    }


    public void onInitiation(GameEngine gameEngine) {
        if (onInitiationBehaviour != null) {
            try {
                onInitiationBehaviour.onUse(gameEngine);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isOnInitiationUseable() {
        return (onInitiationBehaviour != null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public TreeMap<String, Feat> getFeatChecks() {
        return featChecks;
    }

    public void setFeatChecks(TreeMap<String, Feat> featChecks) {
        this.featChecks = featChecks;
    }

    public Feat getFeatFromFeatChecks(String featName) {
        return featChecks.get(featName);
    }

    public void addFeatToFeatChecks(Feat feat) {
        featChecks.put(feat.getName(), feat);
    }

    public void removeFeatFromFeatChecks(Feat feat) {
        featChecks.remove(feat.getName());
    }

    public void removeFeatFromFeatChecks(String featName) {
        featChecks.remove(featName);
    }

    public TreeMap<String, Feat> getFeatRewards() {
        return featRewards;
    }

    public void setFeatRewards(TreeMap<String, Feat> featRewards) {
        this.featRewards = featRewards;
    }

    public Feat getFeatFromFeatRewards(String featName) {
        return featRewards.get(featName);
    }

    public void addFeatToFeatRewards(Feat feat) {
        featRewards.put(feat.getName(), feat);
    }

    public void removeFeatFromFeatRewards(Feat feat) {
        featRewards.remove(feat.getName());
    }

    public void removeFeatFromFeatRewards(String featName) {
        featRewards.remove(featName);
    }

    public TreeMap<String, Prop> getProps() {
        return props;
    }

    public void setProps(TreeMap<String, Prop> props) {
        this.props = props;
    }

    public Prop getPropFromProps(String propName) {
        return props.get(propName);
    }

    public void addPropToProps(Prop prop) {
        try {
            Prop propCloned = (Prop)prop.clone();
            props.put(propCloned.getName(), propCloned);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public void removePropFromProps(Prop prop) {
        props.remove(prop.getName());
    }

    public void removePropFromProps(String propName) {
        props.remove(propName);
    }

    public String getIntroductoryMessage() {
        return introductoryMessage;
    }

    public void setIntroductoryMessage(String introductoryMessage) {
        this.introductoryMessage = introductoryMessage;
    }

    public String getOnDefeatedMessage() {
        return onDefeatedMessage;
    }

    public void setOnDefeatedMessage(String onDefeatedMessage) {
        this.onDefeatedMessage = onDefeatedMessage;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setOnInitiationBehaviour(Useable onInitiationBehaviour) {
        this.onInitiationBehaviour = onInitiationBehaviour;
    }

    public Useable getOnInitiationBehaviour() {
        return onInitiationBehaviour;
    }

    public TreeMap<String, String> getNavigationOptions() {
        return navigationOptions;
    }

    protected void setNavigationOptions(TreeMap<String, String> navigationOptions) throws InvalidValueException {
        for (Map.Entry<String, String> entry : navigationOptions.entrySet()) {
            StringParser.addVerb(entry.getKey());
        }
        this.navigationOptions = navigationOptions;
    }

    protected void setNavigationOption(String prompt, String encounterName) throws InvalidValueException {
        StringParser.addVerb(prompt);
        this.navigationOptions.put(prompt, encounterName);
    }

    public ArrayList<String> getNavigationalVerbs() {
        return navigationalVerbs;
    }

    public void setNavigationalVerbs(ArrayList<String> navigationalVerbs) throws InvalidValueException {
        for (String navVerb : navigationalVerbs) {
            StringParser.addVerb(navVerb);
        }
        this.navigationalVerbs = navigationalVerbs;
    }

    public void removeNavigationalVerb(String verb) {
        navigationalVerbs.remove(verb);
    }

    public void removeDefaultNavigationalVerbs() {
        navigationalVerbs.clear();
    }

    public void addNavigationalVerb(String verb) throws InvalidValueException {
        StringParser.addVerb(verb);
        navigationalVerbs.add(verb);
    }

    public void addNavigationalNoun(String noun) throws InvalidValueException {
        StringParser.addNoun(noun);
        navigationOptions.put(noun, "defeated");
    }

    private void addDefaultNavigationalVerbs() throws InvalidValueException {
        addNavigationalVerb("go");
        addNavigationalVerb("travel");
        addNavigationalVerb("move");
    }

    public String getEncounterFromPrompt(String prompt) {
        return navigationOptions.get(prompt);
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public boolean isIntroductionPrinted() {
        return isIntroductionPrinted;
    }

    public void setIntroductionPrinted(boolean introductionPrinted) {
        isIntroductionPrinted = introductionPrinted;
    }

    public boolean isBacktracking() {
        return isBacktracking;
    }

    public void setBacktracking(boolean backtracking) {
        isBacktracking = backtracking;
    }

    protected void printInventory(GameEngine gameEngine) {
        int itemIteration = 1;
        var player = gameEngine.getPlayer();



        if (player.getInventory().isEmpty() && player.getCurrencyAmount() == 0) {
            gameEngine.printMessage("You currently have no gold, nor any items in your inventory.");
        } else if (player.getInventory().isEmpty()) {
            gameEngine.printMessage("You have " + (int)player.getCurrencyAmount() + " gold.");
            gameEngine.printMessage("You currently have no items in your inventory.");
        } else {
            gameEngine.printMessage("Your inventory (" + (int)player.getCurrencyAmount() + " gold): ");
            for (Map.Entry<String, Item> entry : player.getInventory().entrySet()) {
                var item = entry.getValue();
                gameEngine.printMessage(itemIteration++ + ": " + item.getName());
            }
        }
    }

    protected void printOptions(GameEngine gameEngine) {
        gameEngine.printMessage("Type one of the following commands: ");
        gameEngine.printMessageFormatted("%-15s %s\n", "Help", "Prints a list of commands that the player can enter.");
        gameEngine.printMessageFormatted("%-15s %s\n", "Exit", "Exits the game.");
        gameEngine.printMessageFormatted("%-15s %s\n", "Inventory", "Lists the items and gold a player currently has in their inventory.");
        gameEngine.printMessageFormatted("%-15s %s\n", "Status", "Lists the player's health points, as well as the condition of all enemies in an encounter.");
        if (this instanceof CombatEncounter) {
            gameEngine.printMessageFormatted("%-15s %s\n", "Attack", "Starts a new round of combat.");
        }
        if (this instanceof NonCombatEncounter) {
            gameEngine.printMessageFormatted("%-15s %s\n", "Interact", "Interact with non-hostile beings.");
        }
        gameEngine.printMessageFormatted("%-15s %s\n", "Investigate", "Investigates your immediate surroundings.");
        gameEngine.printMessageFormatted("%-15s %s\n", "Back", "Exits the current activity when possible.");
        gameEngine.printMessageFormatted("%-15s %s\n", "<navigation>", "Navigates to another encounter when possible (e.g. 'north').");
    }

    public abstract String run(GameEngine gameEngine) throws InventoryFullException, InvalidValueException;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Encounter Name: " + getName() + ", " +
                "Is Defeated: " + isDefeated() + ", " +
                "Is Introduction Printed: " + isIntroductionPrinted() + ", " +
                "Is Backtracking: " + isBacktracking() + ", " +
                "Introductory Message: \n\t'" + getIntroductoryMessage() + "'" +
                "\nHint: \n\t'" + getHint() + "'" +
                "\nOn Defeated Message: \n\t'" + getOnDefeatedMessage() + "'");

        if (!getNavigationOptions().isEmpty()) {
            sb.append("\nNavigational Options: ");
            for (Map.Entry<String, String> navigationalOptionsSet : getNavigationOptions().entrySet()) {
                if (navigationalOptionsSet.getValue().equalsIgnoreCase("defeated")) {
                    sb.append("\n\t'" + navigationalOptionsSet.getKey() + "' leads to the next encounter");
                } else {
                    sb.append("\n\t'" + navigationalOptionsSet.getKey() + "' leads to '" +
                            navigationalOptionsSet.getValue() + "'");
                }
            }
        }

        if (!getNavigationalVerbs().isEmpty()) {
            sb.append("\nNavigational Verbs: ");
            for (String verb : getNavigationalVerbs()) {
                sb.append("\n\t'" + verb + "'");
            }
        }

        if (!getFeatChecks().isEmpty()) {
            sb.append("\nFeat Check Table: ");
            for (Map.Entry<String, Feat> featSet : getFeatChecks().entrySet()) {
                Feat feat = featSet.getValue();
                sb.append("\n\t" + feat.toString());
            }
        }

        if (!getFeatRewards().isEmpty()) {
            sb.append("\nFeat Check Table: ");
            for (Map.Entry<String, Feat> featSet : getFeatRewards().entrySet()) {
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
     *             encounter.save("src/fileName");
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
     *             encounter.save("src/fileName", "sav");
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
     *             var combatEncounter = (CombatEncounter) Encounter.load("src/fileNameWithSavExtension.sav");
     *             or
     *             var nonCombatEncounter = (NonCombatEncounter) Encounter.load("src/fileNameWithSerExtension");
     * @throws IOException Arises if the file path cannot be serialised to.
     * @throws ClassNotFoundException Arises if the file cannot be deserialised to this class.
     * underscores, and forward slashes.
     */
    public static Encounter load(String path) throws IOException, ClassNotFoundException {
        String[] splitPath = path.trim().split("\\.");
        String mergedPath = "";
        if (splitPath.length == 2) {
            mergedPath = splitPath[0] + "." + splitPath[1];
        } else {
            mergedPath = splitPath[0] + ".ser";
        }

        FileInputStream fileInputStream = new FileInputStream(mergedPath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Encounter) objectInputStream.readObject();
    }
}
