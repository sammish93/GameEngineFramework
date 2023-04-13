package no.hiof.samuelcd.tbage.models.abilities;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.Serializable;
import java.util.UUID;

public class Ability implements Useable, Serializable {

    private String name;
    private double abilityProbabilityPerTurn;
    private Useable useable;


    private Ability(String name, double abilityProbabilityPerTurn) throws InvalidValueException {
        this.name = name;
        if (abilityProbabilityPerTurn <= 1.00 && abilityProbabilityPerTurn > 0) {
            this.abilityProbabilityPerTurn = abilityProbabilityPerTurn;
        } else {
            throw new InvalidValueException("Value " + abilityProbabilityPerTurn + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }


    public static Ability create() throws InvalidValueException {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Ability(randomlyGeneratedId.toString(), 0.5);
    }

    public static Ability create(String name) throws InvalidValueException {
        return new Ability(name,  0.5);
    }

    public static Ability create(String name, double abilityProbabilityPerTurn) throws InvalidValueException {
        return new Ability(name, abilityProbabilityPerTurn);
    }


    public void onUse(GameEngine gameEngine) {
        try {
            useable.onUse(gameEngine);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isUseable() {
        return (useable != null);
    }

    public void setOnUseBehaviour(Useable useable) {
        this.useable = useable;
    }

    public Useable getOnUseBehaviour() {
        return useable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAbilityProbabilityPerTurn() {
        return abilityProbabilityPerTurn;
    }

    public void setAbilityProbabilityPerTurn(double abilityProbabilityPerTurn) throws InvalidValueException {
        if (abilityProbabilityPerTurn <= 1.00 && abilityProbabilityPerTurn > 0) {
            this.abilityProbabilityPerTurn = abilityProbabilityPerTurn;
        } else {
            throw new InvalidValueException("Value " + abilityProbabilityPerTurn + " is invalid. Enter a decimal " +
                    "value greater than 0 and less than or equal to 1.");
        }
    }

    @Override
    public String toString() {
        double i = abilityProbabilityPerTurn * 100;
        return "Ability Name: '" + name + "', " +
                "Probability Per Turn: " + (int) i + "%";
    }
}
