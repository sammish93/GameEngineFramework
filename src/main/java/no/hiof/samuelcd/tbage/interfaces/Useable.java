package no.hiof.samuelcd.tbage.interfaces;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;

import java.io.Serializable;

public interface Useable extends Serializable {

    void onUse(GameEngine gameEngine) throws InvalidValueException;
}
