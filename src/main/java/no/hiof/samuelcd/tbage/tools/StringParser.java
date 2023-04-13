package no.hiof.samuelcd.tbage.tools;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;

import java.util.ArrayList;
import java.util.TreeMap;

public class StringParser {
    // A class to parse text during a dialogue sequence with a NonPlayerCharacter.
    // This comment is helpful.
    // https://www.reddit.com/r/learnjavascript/comments/24jr52/comment/ch83hf1/?utm_source=share&utm_medium=web2x&context=3
    private static ArrayList<String> verbs = new ArrayList<>();
    private static ArrayList<String> nouns = new ArrayList<>();
    private static ArrayList<String> commands = new ArrayList<>();


    public static TreeMap<String, String> read(GameEngine gameEngine, String input) {
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

    private static boolean parse(GameEngine gameEngine, String[] splitString) {

        if (splitString.length == 1) {
            return commands.contains(splitString[0]);
        } else if (splitString.length == 2) {
            return verbs.contains(splitString[0]) && nouns.contains(splitString[1]);
        }

        return false;
    }

    public static void addCommand(String command) throws InvalidValueException {
        String[] splitString = command.trim().split("\\s+");

        if (splitString.length == 1) {
            commands.add(splitString[0]);
        } else {
            throw new InvalidValueException("The value '" + command + "' is invalid. " +
                    "Enter a single word without spaces.");
        }
    }

    public static void addNoun(String noun) throws InvalidValueException {
        String[] splitString = noun.trim().split("\\s+");

        if (splitString.length == 1) {
            nouns.add(splitString[0]);
        } else {
            throw new InvalidValueException("The value '" + noun + "' is invalid. " +
                    "Enter a single word without spaces.");
        }
    }

    public static void addVerb(String verb) throws InvalidValueException {
        String[] splitString = verb.trim().split("\\s+");

        if (splitString.length == 1) {
            verbs.add(splitString[0]);
        } else {
            throw new InvalidValueException("The value '" + verb + "' is invalid. " +
                    "Enter a single word without spaces.");
        }
    }

    public static ArrayList<String> getVerbs() {
        return verbs;
    }

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

    public static ArrayList<String> getNouns() {
        return nouns;
    }

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

    public static ArrayList<String> getCommands() {
        return commands;
    }

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
