package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;
import no.hiof.samuelcd.tbage.models.feats.Feat;

import java.util.TreeMap;

public abstract class Encounter implements Comparable<Encounter>, JsonExternalisable {
    private String name;
    private double weightedProbability;
    private String imagePath;
    private TreeMap<String, Feat> featChecks;
    private TreeMap<String, Feat> featRewards;


    protected Encounter(String name, double weightedProbability, String imagePath) {
        this.name = name;
        this.weightedProbability = weightedProbability;
        this.imagePath = imagePath;
    }

    public void onInitiation() {
        // Do something here when encounter begins.
    };

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

    public double getWeightedProbability() {
        return weightedProbability;
    }

    public void setWeightedProbability(double weightedProbability) {
        this.weightedProbability = weightedProbability;
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

    public int compareTo(Encounter encounter) {
        return name.compareTo(encounter.getName());
    }
}
