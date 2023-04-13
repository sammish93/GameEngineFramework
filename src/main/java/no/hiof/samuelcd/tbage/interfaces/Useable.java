package no.hiof.samuelcd.tbage.interfaces;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.models.npcs.NonPlayableCharacter;

import java.io.Serializable;

public interface Useable extends Serializable {

    void onUse(GameEngine gameEngine) throws InvalidValueException;
}
