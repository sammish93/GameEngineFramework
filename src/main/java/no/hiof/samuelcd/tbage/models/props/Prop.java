package no.hiof.samuelcd.tbage.models.props;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;

import java.io.Serializable;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

public class Prop implements Serializable, Cloneable, Useable {
    private String name;
    private Useable onUseBehaviour;
    private boolean isUsed;


    private Prop(String name) {
        if (name != null) {
            this.setName(name);
        }

        isUsed = false;
    }

    public static Prop create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Prop(randomlyGeneratedId.toString());
    }

    public static Prop create(String name) {
        return new Prop(name);
    }


    public void onUse(GameEngine gameEngine) {
        // Item does this when it is used.
        if (!isUsed) {
            if (getOnUseBehaviour() != null) {
                try {
                    onUseBehaviour.onUse(gameEngine);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                isUsed = true;
            } else {
                gameEngine.printMessage("Interacting with this object does nothing.");
                isUsed = true;
            }
        } else {
            gameEngine.printMessage("You have already investigated this object.");
        }

    }

    public boolean isUseable() {
        return (onUseBehaviour != null);
    }

    public void setOnUseBehaviour(Useable useable) {
        this.onUseBehaviour = useable;
    }

    public Useable getOnUseBehaviour() {
        return onUseBehaviour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Prop Name: '" + name + "', " +
                "Is Useable: " + isUseable() + ", " +
                "Is Used: " + isUsed;
    }
}
