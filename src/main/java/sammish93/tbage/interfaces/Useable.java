package sammish93.tbage.interfaces;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;

import java.io.Serializable;

/**
 * A functional interface intended to be used as a way for a developer to pass custom method behaviours while
 * also remaining serialisable. This interface is used throughout the framework in many cases.
 * One example of use include in both the Prop, Ability and Item classes to program how they behave when they
 * are used by a place during runtime. Another example is the Encounter class, with custom behaviours happening
 * during initiation.
 *
 * Example of a lambda being used for this functional interface is as follows:
 * Useable onUse = (gameEngine) -> {
 *             var player = gameEngine.getPlayer();
 *             player.subtractFromCurrentHealth(5);
 *
 *             gameEngine.printMessage("You have taken 5 damage!");
 *         };
 */
public interface Useable extends Serializable {

    /**
     *
     * @param gameEngine The current instance of the GameEngine is required so that the lambda used can reference
     *                   other dependencies of the GameEngine class such as the player or current encounter.
     */
    void onUse(GameEngine gameEngine) throws InvalidValueException;
}
