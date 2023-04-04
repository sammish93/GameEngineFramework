package no.hiof.samuelcd.tbage.tools;

import no.hiof.samuelcd.tbage.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StringParser {
    // A class to parse text during a dialogue sequence with a NonPlayerCharacter.
    // This comment is helpful.
    // https://www.reddit.com/r/learnjavascript/comments/24jr52/comment/ch83hf1/?utm_source=share&utm_medium=web2x&context=3
    private static ArrayList<String> verbs = new ArrayList<>();
    private static ArrayList<String> nouns = new ArrayList<>();
    private static ArrayList<String> commands = new ArrayList<>();

    public static TreeMap<String, String> read(GameEngine gameEngine, String input) {
        String[] splitString = input.split("\\s+");
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
            } else if (commands.contains(splitString[0])) {
                map.put("command", splitString[0]);
                map.put("noun", splitString[0]);
            } else
            {
                map.put("verb", splitString[0]);
                map.put("noun", splitString[0]);
            }
        }

        return map;
    }

    private static boolean parse(GameEngine gameEngine, String[] splitString) {

        if (splitString.length == 1) {
            return commands.contains(splitString[0]);
        } else if (splitString.length == 2) {
            return verbs.contains(splitString[0]) && nouns.contains(splitString[1]) ||
                    commands.contains(splitString[0]) && nouns.contains(splitString[1]);
        }

        return false;
    }

    public static void addCommand(String command) {
        String[] splitString = command.split("\\s+");

        if (splitString.length == 1) {
            commands.add(splitString[0]);
        }
    }

    public static void addNoun(String noun) {
        String[] splitString = noun.split("\\s+");

        if (splitString.length == 1) {
            nouns.add(splitString[0]);
        }
    }

    public static void addVerb(String verb) {
        String[] splitString = verb.split("\\s+");

        if (splitString.length == 1) {
            verbs.add(splitString[0]);
        }
    }

}
