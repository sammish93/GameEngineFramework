package sammish93.tbage.models;

import org.junit.jupiter.api.Test;
import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.interfaces.Useable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PropTest {

    GameEngine gameEngine = mock(GameEngine.class);

    @Test
    void assertsPropIsUsedBooleanIsSetToTrueWhenPropIsUsed() throws InvalidValueException, InterruptedException {
        var prop = Prop.create();

        Useable onUse = (gameEngine) -> {
            // Does nothing.
        };

        prop.setOnUseBehaviour(onUse);

        assertFalse(prop.isUsed());

        prop.onUse(gameEngine);

        assertTrue(prop.isUsed());
    }

}