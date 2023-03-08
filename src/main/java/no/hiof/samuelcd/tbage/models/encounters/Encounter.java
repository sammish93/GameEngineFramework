package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;

public abstract class Encounter implements Comparable<Encounter>, JsonExternalisable {
    private String name;
    private double weightedProbability;
    private String imagePath;


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

    public int compareTo(Encounter encounter) {
        return name.compareTo(encounter.getName());
    }
}
