package no.hiof.samuelcd.tbage.models.encounters;

import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;

public abstract class Encounter implements Comparable<Encounter>, JsonExternalisable {
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
