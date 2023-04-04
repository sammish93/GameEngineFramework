package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.models.feats.Feat;

import java.io.*;
import java.util.Objects;
import java.util.TreeMap;

public abstract class Encounter implements Comparable<Encounter>, Serializable {
    private String name;
    private double weightedProbability;
    private String imagePath;
    private TreeMap<String, Feat> featChecks;
    private TreeMap<String, Feat> featRewards;
    private TreeMap<String, String> navigationOptions;
    private TreeMap<String, String> interactionOptions;
    private boolean isDefeated = false;
    private boolean isIntroductionPrinted = false;
    private boolean isBacktracking = false;


    protected Encounter(String name, double weightedProbability, String imagePath, TreeMap<String, Feat> featChecks, TreeMap<String, Feat> featRewards, TreeMap<String, String> navigationOptions) {
        this.name = name;
        this.weightedProbability = weightedProbability;
        this.imagePath = imagePath;

        this.featChecks = Objects.requireNonNullElseGet(featChecks, TreeMap::new);
        this.featRewards = Objects.requireNonNullElseGet(featRewards, TreeMap::new);
        this.navigationOptions = Objects.requireNonNullElseGet(navigationOptions, TreeMap::new);
    }

    public void onInitiation() {
        // Do something here when encounter begins.
        System.out.println("hello!");
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

    public void save(String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);
    }

    public static Encounter load(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Encounter)objectInputStream.readObject();
    }

    public abstract String run(GameEngine gameEngine);

    public TreeMap<String, String> getNavigationOptions() {
        return navigationOptions;
    }

    public void setNavigationOptions(TreeMap<String, String> navigationOptions) {
        this.navigationOptions = navigationOptions;
    }

    public void setNavigationOption(String prompt, String encounterName) {
        this.navigationOptions.put(prompt, encounterName);
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
}
