package no.hiof.samuelcd.tbage.models.feats;

import java.io.Serializable;
import java.util.UUID;

public class Feat implements Serializable {

    private String name;


    private Feat(String name) {
        this.name = name;
    }

    public static Feat create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Feat(randomlyGeneratedId.toString());
    }

    public static Feat create(String name) {
        return new Feat(name);
    }


    public void onUse() {
        // Feat behaviour.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
