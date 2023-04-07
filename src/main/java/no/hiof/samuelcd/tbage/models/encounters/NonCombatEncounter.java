package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.feats.Feat;
import no.hiof.samuelcd.tbage.models.npcs.Ally;

import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

public class NonCombatEncounter extends Encounter {

    private int allyCount;
    private TreeMap<String, Ally> allies;

    private NonCombatEncounter(String name, String imagePath, TreeMap<String, Feat> featChecks, TreeMap<String, Feat> featRewards,  TreeMap<String, Ally> allies, TreeMap<String, String> navigationOptions) {
        super(name, imagePath, featChecks, featRewards, navigationOptions);

        this.allies = Objects.requireNonNullElseGet(allies, TreeMap::new);
    }

    public static NonCombatEncounter create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new NonCombatEncounter(randomlyGeneratedId.toString(), null, null, null, null, null);
    }

    public static NonCombatEncounter create(String name) {
        return new NonCombatEncounter(name, null, null, null, null, null);
    }

    public TreeMap<String, Ally> getAllies() {
        return allies;
    }

    public void setAllies(TreeMap<String, Ally> allies) {
        this.allies = allies;
    }

    public Ally getAllyFromAllies(String allyName) {
        return allies.get(allyName);
    }

    public void addAllyToAllies(Ally ally) {
        allies.put(ally.getName(), ally);
    }

    public void removeAllyFromAllies(Ally ally) {
        allies.remove(ally.getName());
    }

    public void removeAllyFromAllies(String allyName) {
        allies.remove(allyName);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String run(GameEngine gameEngine) {
        return "defeated";
    }

    private void nonCombatEncounterIntroduction(GameEngine gameEngine) {
        if (!getIntroductoryMessage().isEmpty()) {
            gameEngine.printMessage(getIntroductoryMessage());
        }
    }
}
