package no.hiof.samuelcd.tbage.models.encounters;

public abstract class Encounter implements Comparable<Encounter>{
    private String encounterName;

    public String getEncounterName() {
        return encounterName;
    }

    public void setEncounterName(String encounterName) {
        this.encounterName = encounterName;
    }

    public int compareTo(Encounter encounter) {
        return encounterName.compareTo(encounter.getEncounterName());
    }
}
