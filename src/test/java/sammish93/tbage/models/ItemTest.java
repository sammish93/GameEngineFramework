package sammish93.tbage.models;

import org.junit.jupiter.api.Test;
import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.interfaces.Useable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ItemTest {

    GameEngine gameEngine = mock(GameEngine.class);

    @Test
    void assertsNumberOfItemUsesDecreases() throws InvalidValueException {
        var item = Item.create();
        item.setNumberOfUses(3);

        Useable onUse = (gameEngine) -> {
            // Does nothing.
        };

        item.setOnUseBehaviour(onUse);

        item.onUse(gameEngine);

        assertEquals(2, item.getNumberOfUses());
    }

    @Test
    void assertsNumberOfItemUsesDoesNotDecreaseIfItemHasInfiniteUses() throws InvalidValueException {
        var item = Item.create();
        item.setNumberOfUses(0);

        Useable onUse = (gameEngine) -> {
            // Does nothing.
        };

        item.setOnUseBehaviour(onUse);

        item.onUse(gameEngine);

        assertEquals(0, item.getNumberOfUses());
    }

}