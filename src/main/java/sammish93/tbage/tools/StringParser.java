package sammish93.tbage.tools;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * A class intended to parse a string (in the case of this framework, the string that is taken as user input),
 * and determining if it is a valid command, or a valid combination of a verb and a noun. This class cannot be
 * instantiated, and contains only static methods.
 */
public class StringParser {

    private static ArrayList<String> verbs = new ArrayList<>();
    private static ArrayList<String> nouns = new ArrayList<>();
    private static ArrayList<String> commands = new ArrayList<>();


    /**
     * A method that determines if a string is of valid format to pass as either a single-word command, or a
     * combination of two words (verb as the first word, and noun as the second), before returning it as a
     * TreeMap.
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param input A string that is to be trimmed and split based on spaces (' ').
     *              Example:
     *              The string '   hello there  ' will be split into two different strings ('hello', and 'there').
     * @return Returns the string as a TreeMap. Each entry is comprised of the type of word ('command, 'verb',
     * or 'noun') as the key, and the word as the value.
     * @see StringParser#parse(GameEngine, String[])
     */
    public static TreeMap<String, String> read(GameEngine gameEngine, String input) throws InterruptedException {
        String[] splitString = input.trim().split("\\s+");
        TreeMap<String, String> map = new TreeMap<>();
        boolean isValid = false;

        if (input.isEmpty()) {
            gameEngine.printMessage("Please enter a command.");
        } else if (splitString.length > 2) {
            gameEngine.printMessage("Please enter no more than two commands.");
        } else {
            isValid = parse(gameEngine, splitString);
        }

        if (isValid) {
            if (splitString.length == 1) {
                map.put("command", splitString[0]);
            } else
            {
                map.put("verb", splitString[0]);
                map.put("noun", splitString[1]);
            }
        } else {
            gameEngine.printMessage("Sorry, I don't understand. Type 'help' for help.");
        }

        return map;
    }

    /**
     * Determines whether a split string is of the desired length (one to two words).
     * @param gameEngine Required to communicate with other dependencies such as the game interface.
     * @param splitString An array of split strings.
     * @return Returns either a true or false boolean value based on whether the string array is between one
     * to two words long. If no words are present, or there are three or more words, then the method returns false.
     */
    private static boolean parse(GameEngine gameEngine, String[] splitString) {

        if (splitString.length == 1) {
            return commands.contains(splitString[0]);
        } else if (splitString.length == 2) {
            return verbs.contains(splitString[0]) && nouns.contains(splitString[1]);
        }

        return false;
    }

    /**
     * Adds a command to a static ArrayList containing all recognised commands that are considered valid.
     * @param command A string comprised of a single word.
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void addCommand(String command) throws InvalidValueException {
        String[] splitString = command.trim().split("\\s+");

        if (splitString.length == 1) {
            commands.add(splitString[0]);
        } else {
            throw new InvalidValueException("The value '" + command + "' is invalid. " +
                    "Enter a single word without spaces.");
        }
    }

    /**
     * Adds a noun to a static ArrayList containing all recognised nouns that are considered valid.
     * @param noun A string comprised of a single word.
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void addNoun(String noun) throws InvalidValueException {
        String[] splitString = noun.trim().split("\\s+");

        if (splitString.length == 1) {
            nouns.add(splitString[0]);
        } else {
            throw new InvalidValueException("The value '" + noun + "' is invalid. " +
                    "Enter a single word without spaces.");
        }
    }

    /**
     * Adds a verb to a static ArrayList containing all recognised verbs that are considered valid.
     * @param verb A string comprised of a single word.
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void addVerb(String verb) throws InvalidValueException {
        String[] splitString = verb.trim().split("\\s+");

        if (splitString.length == 1) {
            verbs.add(splitString[0]);
        } else {
            throw new InvalidValueException("The value '" + verb + "' is invalid. " +
                    "Enter a single word without spaces.");
        }
    }

    /**
     * Removes a command from a static ArrayList containing all recognised commands that are considered valid.
     * Once removed, this command is no longer considered valid.
     * @param command A string comprised of a single word.
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void removeCommand(String command) throws InvalidValueException {

        commands.remove(command);
    }

    /**
     * Removes a noun from a static ArrayList containing all recognised nouns that are considered valid.
     * Once removed, this command is no longer considered valid.
     * @param noun A string comprised of a single word.
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void removeNoun(String noun) throws InvalidValueException {
        nouns.remove(noun);
    }

    /**
     * Removes a verb from a static ArrayList containing all recognised verbs that are considered valid.
     * Once removed, this command is no longer considered valid.
     * @param verb A string comprised of a single word.
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void removeVerb(String verb) throws InvalidValueException {
        verbs.remove(verb);
    }

    /**
     *
     * @return Retrieves a static ArrayList containing all verbs considered valid.
     */
    public static ArrayList<String> getVerbs() {
        return verbs;
    }

    /**
     * A method used to set a static ArrayList to a given ArrayList, where all string elements contained
     * within will be considered valid verbs.
     * @param verbs An ArrayList where each element is a string comprised of a single word
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void setVerbs(ArrayList<String> verbs) throws InvalidValueException {
        for (String verb : verbs) {
            String[] splitString = verb.trim().split("\\s+");

            if (splitString.length != 1) {
                throw new InvalidValueException("The value '" + verb + "' is invalid. " +
                        "Enter a single word without spaces.");
            }
        }

        StringParser.verbs = verbs;
    }

    /**
     *
     * @return Retrieves a static ArrayList containing all nouns considered valid.
     */
    public static ArrayList<String> getNouns() {
        return nouns;
    }

    /**
     * A method used to set a static ArrayList to a given ArrayList, where all string elements contained
     * within will be considered valid nouns.
     * @param nouns An ArrayList where each element is a string comprised of a single word
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void setNouns(ArrayList<String> nouns) throws InvalidValueException {
        for (String noun : nouns) {
            String[] splitString = noun.trim().split("\\s+");

            if (splitString.length != 1) {
                throw new InvalidValueException("The value '" + noun + "' is invalid. " +
                        "Enter a single word without spaces.");
            }
        }

        StringParser.nouns = nouns;
    }

    /**
     *
     * @return Retrieves a static ArrayList containing all commands considered valid.
     */
    public static ArrayList<String> getCommands() {
        return commands;
    }

    /**
     * A method used to set a static ArrayList to a given ArrayList, where all string elements contained
     * within will be considered valid commands.
     * @param commands An ArrayList where each element is a string comprised of a single word
     * @throws InvalidValueException Is thrown in an example such as if the string given as a parameter
     * is either empty or is comprised of more than one words.
     */
    public static void setCommands(ArrayList<String> commands) throws InvalidValueException {
        for (String command : commands) {
            String[] splitString = command.trim().split("\\s+");

            if (splitString.length != 1) {
                throw new InvalidValueException("The value '" + command + "' is invalid. " +
                        "Enter a single word without spaces.");
            }
        }

        StringParser.commands = commands;
    }
}
