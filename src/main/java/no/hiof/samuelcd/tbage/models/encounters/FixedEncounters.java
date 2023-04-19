package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.tools.StringParser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A class that allows the developer to manually control encounter order and traversal paths that will appear in
 * a single game's permutation.
 */
public class FixedEncounters extends Encounters {

    private LinkedHashMap<String, Encounter> encounters;
    private Encounter initialEncounter;


    private FixedEncounters() {
        encounters = new LinkedHashMap<>();
    }

    /**
     *
     * @return Returns an instantiated FixedEncounters object.
     */
    public static FixedEncounters create() {
        return new FixedEncounters();
    }


    /**
     * This method is intended to be used to add an initial encounter that acts as the first encounter that
     * the player starts in on runtime.
     * @param encounter An instantiated Encounter object.
     */
    public void addEncounter(Encounter encounter) {
        initialEncounter = encounter;
        encounters.put(encounter.getName(), encounter);
    }

    /**
     * This method is intended to link encounters together so that the play can traverse from one encounter to 
     * another using a specific noun (event).
     *
     * Example:
     * encounters.addEncounter(nonCombatEncounter);
     * encounters.addEncounter(nonCombatEncounter, nonCombatEncounter2, "defeated");
     * encounters.addEncounter(nonCombatEncounter2, combatEncounter, "north");
     *
     * NOTE: A single encounter can have traversal paths to several other encounters.
     * NOTE: If the developer wishes for the encounter to automatically progress once the encounter has been 
     * defeated (e.g. when all enemies have been defeated) then it is recommended to use the overloaded 
     * addEncounter() class without an event string being provided.
     * @param encounterFrom An instantiated Encounter object.
     * @param encounterTo An instantiated Encounter object that a user can travel to from the Encounter set as
     *                    encounterFrom.
     * @param event A noun used to navigate by via a two word command in the format (verb) + (noun).
     *              NOTE: An encounter can only be traversed using a valid verb together with the noun. See
     *              the addNavigationalVerb() in the Encounter class for more information.
     * @throws InvalidValueException
     * @see FixedEncounters#addEncounter(Encounter, Encounter)
     * @see Encounter#addNavigationalVerb(String)
     */
    public void addEncounter(Encounter encounterFrom, Encounter encounterTo, String event)
            throws InvalidValueException {
        encounterFrom.setNavigationOption(event, encounterTo.getName());
        encounters.put(encounterTo.getName(), encounterTo);
        StringParser.addNoun(event);
        // Encounter x progresses to encounter y if event is triggered.
    }

    /**
     * A method intended to use when the default traversal method is to be employed. Traversal will trigger once
     * an encounter is defeated (e.g. when all enemies have been defeated).
     * @param encounterFrom An instantiated Encounter object.
     * @param encounterTo An instantiated Encounter object that a user can travel to from the Encounter set as
     *                    encounterFrom.
     * @throws InvalidValueException
     * @see FixedEncounters#addEncounter(Encounter, Encounter, String)
     */
    public void addEncounter(Encounter encounterFrom, Encounter encounterTo) throws InvalidValueException {
        String defaultEvent = "defeated";
        encounterFrom.setNavigationOption(defaultEvent, encounterTo.getName());
        encounters.put(encounterTo.getName(), encounterTo);
        StringParser.addNoun(defaultEvent);
        // Encounter x progresses to encounter y if event is triggered.
    }

    /**
     *
     * @return Returns an instantiated Encounter object that is the initial encounter.
     */
    public Encounter getInitialEncounter() {
        return initialEncounter;
    }

    /**
     *
     * @param encounterName A string representing the name of an instantiated Encounter.
     * @return Returns an instantiated Encounter object.
     */
    public Encounter getEncounter(String encounterName) {
        return encounters.get(encounterName);
    }

    /**
     *
     * @return Returns a TreeMap of all encounters present.
     */
    public LinkedHashMap<String, Encounter> getEncounters() {
        return encounters;
    }

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fixed Encounters:");

        for (Map.Entry<String, Encounter> encounterSet : encounters.entrySet()) {
            sb.append("\n" + encounterSet.getValue().toString());
        }

        return sb.toString();
    }
}
