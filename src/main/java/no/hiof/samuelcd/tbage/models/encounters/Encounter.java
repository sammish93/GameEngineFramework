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

    /**
     * A method to control the behaviour of an Encounter when initially traversed to. If an
     * onInitiationBehaviour exists then this is executed and the player can continue to interact with the
     * encounter.
     * @param gameEngine The current instance of the GameEngine is required so that dependencies such as the
     *                   current encounter and player can be referenced.
     */
    public void onInitiation(GameEngine gameEngine) {
        if (onInitiationBehaviour != null) {
            try {
                onInitiationBehaviour.onUse(gameEngine);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @return Returns true in the event that there is an on-use behavior set that can execute when the onUse()
     * method is called.
     */
    public boolean isOnInitiationUseable() {
        return (onInitiationBehaviour != null);
    }

    /**
     *
     * @param onInitiationBehaviour Intended to use a lambda to allow developers to create custom behaviours when an
     *                              encounter is first traversed to.
     *                              Example of a lambda created using the generic Useable interface:
     *                              Useable onUseInitiation = (gameEngine) -> {
     *                                var player = gameEngine.getPlayer();
     *                                player.subtractFromCurrentHealth(6);
     *                                gameEngine.printMessage("On entering the encounter you suffered 6 damage " +
     *                                "from a rusty doornail!");
     *                              };
     */
    public void setOnInitiationBehaviour(Useable onInitiationBehaviour) {
        this.onInitiationBehaviour = onInitiationBehaviour;
    }

    /**
     *
     * @return Returns the generic Useable interface if it exists.
     */
    public Useable getOnInitiationBehaviour() {
        return onInitiationBehaviour;
    }

    /**
     *
     * @return Returns a string representing the name of an Encounter.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name A string representing the name of an existing instantiated Encounter.
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
     * @return Returns all feats that are to be checked by an encounter.
     */
    public TreeMap<String, Feat> getFeatChecks() {
        return featChecks;
    }

    /**
     *
     * @param featChecks Sets an encounter's feat checks to an existing TreeMap.
     */
    public void setFeatChecks(TreeMap<String, Feat> featChecks) {
        this.featChecks = featChecks;
    }

    /**
     *
     * @param featName A string representing a feat's name.
     * @return Returns an instantiated Feat object.
     */
    public Feat getFeatFromFeatChecks(String featName) {
        return featChecks.get(featName);
    }

    /**
     *
     * @param feat Adds an instantiated Feat object to existing feat checks.
     */
    public void addFeatToFeatChecks(Feat feat) {
        featChecks.put(feat.getName(), feat);
    }

    /**
     * Removes an existing feat from an encounter's feat checks.
     * @param feat An instantiated Feat object.
     */
    public void removeFeatFromFeatChecks(Feat feat) {
        featChecks.remove(feat.getName());
    }

    /**
     * Removes an existing feat from an encounter's feat checks.
     * @param featName A string representing an instantiated Feat object.
     */
    public void removeFeatFromFeatChecks(String featName) {
        featChecks.remove(featName);
    }

    /**
     *
     * @return Returns all feats that are to be rewarded by an encounter.
     */
    public TreeMap<String, Feat> getFeatRewards() {
        return featRewards;
    }

    /**
     *
     * @param featRewards Sets an encounter's feat rewards to an existing TreeMap.
     */
    public void setFeatRewards(TreeMap<String, Feat> featRewards) {
        this.featRewards = featRewards;
    }

    /**
     *
     * @param featName A string representing a feat's name.
     * @return Returns an instantiated Feat object.
     */
    public Feat getFeatFromFeatRewards(String featName) {
        return featRewards.get(featName);
    }

    /**
     *
     * @param feat Adds an instantiated Feat object to existing feat rewards.
     */
    public void addFeatToFeatRewards(Feat feat) {
        featRewards.put(feat.getName(), feat);
    }

    /**
     * Removes an existing feat from an encounter's feat rewards.
     * @param feat An instantiated Feat object.
     */
    public void removeFeatFromFeatRewards(Feat feat) {
        featRewards.remove(feat.getName());
    }

    /**
     * Removes an existing feat from an encounter's feat rewards.
     * @param featName A string representing an instantiated Feat object.
     */
    public void removeFeatFromFeatRewards(String featName) {
        featRewards.remove(featName);
    }

    /**
     *
     * @return Returns all props that are in said encounter.
     */
    public TreeMap<String, Prop> getProps() {
        return props;
    }

    /**
     *
     * @param props Sets an encounter's props to an existing TreeMap.
     */
    public void setProps(TreeMap<String, Prop> props) {
        this.props = props;
    }

    /**
     *
     * @param propName A string representing a prop's name.
     * @return Returns an instantiated Prop object.
     */
    public Prop getPropFromProps(String propName) {
        return props.get(propName);
    }

    /**
     *
     * @param prop Adds an instantiated Prop object to existing props in said encounter.
     */
    public void addPropToProps(Prop prop) {
        try {
            Prop propCloned = (Prop)prop.clone();
            props.put(propCloned.getName(), propCloned);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Removes an existing prop from said encounter's props.
     * @param prop An instantiated Prop object.
     */
    public void removePropFromProps(Prop prop) {
        props.remove(prop.getName());
    }

    /**
     * Removes an existing prop from said encounter's props.
     * @param propName A string representing an instantiated Prop object.
     */
    public void removePropFromProps(String propName) {
        props.remove(propName);
    }

    /**
     *
     * @return Returns a string that is displayed upon traversing to an encounter.
     */
    public String getIntroductoryMessage() {
        return introductoryMessage;
    }

    /**
     *
     * @param introductoryMessage A string to be displayed upon traversing to an encounter.
     */
    public void setIntroductoryMessage(String introductoryMessage) {
        this.introductoryMessage = introductoryMessage;
    }

    /**
     *
     * @return Returns a string that is displayed upon defeating an encounter.
     */
    public String getOnDefeatedMessage() {
        return onDefeatedMessage;
    }

    /**
     *
     * @param onDefeatedMessage A string to be displayed upon defeating an encounter.
     */
    public void setOnDefeatedMessage(String onDefeatedMessage) {
        this.onDefeatedMessage = onDefeatedMessage;
    }

    /**
     *
     * @return Returns a string that can be used to set an encounter hint.
     */
    public String getHint() {
        return hint;
    }

    /**
     *
     * @param hint A string to be displayed upon retrieving an encounter's hint.
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     *
     * @return Returns a TreeMap of all navigational options (verbs) useable together with nouns in this
     * encounter.
     * @see StringParser
     */
    public TreeMap<String, String> getNavigationOptions() {
        return navigationOptions;
    }

    /**
     *
     * @param navigationOptions An existing TreeMap of navigational options in the format (prompt/noun) +
     *                          (encounter name).
     * @throws InvalidValueException
     * @see StringParser
     */
    protected void setNavigationOptions(TreeMap<String, String> navigationOptions) throws InvalidValueException {
        for (Map.Entry<String, String> entry : navigationOptions.entrySet()) {
            StringParser.addVerb(entry.getKey());
        }
        this.navigationOptions = navigationOptions;
    }

    /**
     *
     * @param prompt A navigational noun that can be used to traverse to a specific encounter via a (verb) +
     *               (noun) user input.
     * @param encounterName A string that represents an instantiated Encounter object.
     * @throws InvalidValueException
     * @see StringParser
     */
    protected void setNavigationOption(String prompt, String encounterName) throws InvalidValueException {
        StringParser.addVerb(prompt);
        this.navigationOptions.put(prompt, encounterName);
    }

    /**
     *
     * @return Returns an ArrayList of all navigational verbs that are in use for this encounter.
     */
    public ArrayList<String> getNavigationalVerbs() {
        return navigationalVerbs;
    }

    /**
     *
     * @param navigationalVerbs An existing ArrayList of navigational verbs.
     * @throws InvalidValueException
     * @see StringParser
     */
    public void setNavigationalVerbs(ArrayList<String> navigationalVerbs) throws InvalidValueException {
        for (String navVerb : navigationalVerbs) {
            StringParser.addVerb(navVerb);
        }
        this.navigationalVerbs = navigationalVerbs;
    }

    /**
     * Removes a navigational verb from the existing navigational verbs for this encounter.
     * @param verb A string representing a single navigation verb.
     * @see StringParser
     */
    public void removeNavigationalVerb(String verb) {
        navigationalVerbs.remove(verb);
    }

    /**
     * Removes all existing navigational verbs from this encounter.
     * NOTE: Developers should be careful to only use this command if they are going to enter their own
     * navigational verbs. Without any navigational verbs present there may arise unintended consequences.
     * @see StringParser
     */
    public void removeDefaultNavigationalVerbs() {
        navigationalVerbs.clear();
    }

    /**
     * Adds a navigational verb to the existing navigational verbs for this encounter.
     * @param verb A string representing a navigation verb for this encounter.
     * @throws InvalidValueException
     * @see StringParser
     */
    public void addNavigationalVerb(String verb) throws InvalidValueException {
        StringParser.addVerb(verb);
        navigationalVerbs.add(verb);
    }

    /**
     * Adds a navigational noun to the existing navigational nouns for this encounter.
     * @param noun A string representing a navigation noun for this encounter.
     * @throws InvalidValueException
     * @see StringParser
     */
    public void addNavigationalNoun(String noun) throws InvalidValueException {
        StringParser.addNoun(noun);
        navigationOptions.put(noun, "defeated");
    }

    private void addDefaultNavigationalVerbs() throws InvalidValueException {
        addNavigationalVerb("go");
        addNavigationalVerb("travel");
        addNavigationalVerb("move");
    }

    /**
     *
     * @param prompt A string prompt (noun) that is used to locate a specific encounter to traverse to.
     * @return Returns a string representing an instantiated Encounter object.
     */
    public String getEncounterFromPrompt(String prompt) {
        return navigationOptions.get(prompt);
    }

    /**
     *
     * @return Returns true if the encounter is defeated.
     */
    public boolean isDefeated() {
        return isDefeated;
    }

    /**
     *
     * @param defeated Sets a boolean value for if the encounter is defeated or not.
     */
    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    /**
     *
     * @return Returns true if the introduction message has been displayed.
     * @see Encounter#isBacktracking()
     */
    public boolean isIntroductionPrinted() {
        return isIntroductionPrinted;
    }

    /**
     *
     * @param introductionPrinted Sets a boolean value for if the introduction message has been displayed.
     * @see Encounter#isBacktracking()
     */
    public void setIntroductionPrinted(boolean introductionPrinted) {
        isIntroductionPrinted = introductionPrinted;
    }

    /**
     *
     * @return Returns whether a player is traversing to a previously defeated encounter.
     */
    public boolean isBacktracking() {
        return isBacktracking;
    }

    /**
     *
     * @param backtracking Sets a boolean value for if the player is backtracking to a previously
     *                     defeated encounter.
     */
    public void setBacktracking(boolean backtracking) {
        isBacktracking = backtracking;
    }

    /**
     * This method is intended to display inventory information in the game interface when the player
     * calls the 'inventory' command.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     */
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

    /**
     * This method is intended to display available commands in the game interface when the player
     * calls the 'help' command.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     */
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

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
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
