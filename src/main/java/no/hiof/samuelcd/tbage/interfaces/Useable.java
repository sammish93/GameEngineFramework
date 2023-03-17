package no.hiof.samuelcd.tbage.interfaces;

import no.hiof.samuelcd.tbage.GameEngine;

import java.io.Serializable;

public interface Useable extends Serializable {

    void onUse(GameEngine gameEngine);
}
