package no.hiof.samuelcd.tbage.models.encounters;

public abstract class Encounter implements Comparable<Encounter>{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Encounter encounter) {
        return name.compareTo(encounter.getName());
    }
}
