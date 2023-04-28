package sammish93.tbage.models;

import org.junit.jupiter.api.Test;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void assertsItemAddedToInventoryDuplicatesAndRenamesDynamically() throws InvalidValueException, InventoryFullException {

        var player = Player.create();

        String itemName = "Potion";
        var item = Item.create(itemName);

        player.addItemToInventory(item);

        assertTrue(player.getInventory().containsKey(itemName));

        player.addItemToInventory(item);

        assertFalse(player.getInventory().containsKey(itemName));
        assertTrue(player.getInventory().containsKey(itemName + " 1"));
        assertTrue(player.getInventory().containsKey(itemName + " 2"));

        player.removeItemFromInventory(itemName + " 1");
        assertTrue(player.getInventory().containsKey(itemName));
    }

    @Test
    void assertsItemAddedToFullInventoryThrowsException() throws InvalidValueException, InventoryFullException {

        var player = Player.create();
        player.setInventorySlots(5);

        String itemName = "Potion";
        var item = Item.create(itemName);

        player.addItemToInventory(item);
        player.addItemToInventory(item);
        player.addItemToInventory(item);
        player.addItemToInventory(item);
        player.addItemToInventory(item);

        assertThrows(InventoryFullException.class, () -> {
            player.addItemToInventory(item);
        });
    }

    @Test
    void assertsSubtractFromCurrentHealthIsNeverNegativeInteger() throws InvalidValueException, InventoryFullException {

        var player = Player.create();
        player.setCurrentHealth(10);

        assertEquals(10, player.getCurrentHealth());

        player.subtractFromCurrentHealth(5);

        assertEquals(5, player.getCurrentHealth());

        player.subtractFromCurrentHealth(9999);

        assertEquals(0, player.getCurrentHealth());
    }

    @Test
    void assertsSubtractFromCurrencyIsNeverNegativeInteger() throws InvalidValueException, InventoryFullException {

        var player = Player.create();
        player.setCurrencyAmount(10);

        assertEquals(10, player.getCurrencyAmount());

        player.subtractFromCurrencyAmount(5);

        assertEquals(5, player.getCurrencyAmount());

        player.subtractFromCurrencyAmount(9999);

        assertEquals(0, player.getCurrencyAmount());
    }

    @Test
    void assertsIsAliveReturnsAliveWhenPlayerHealthIsGreaterThanZero() throws InvalidValueException, InventoryFullException {

        var player = Player.create();
        player.setCurrentHealth(10);

        assertTrue(player.isAlive());

        player.subtractFromCurrentHealth(9999);

        assertFalse(player.isAlive());
    }

    @Test
    void assertsExceptionIsThrownWhenInvalidValuesAreGiven() throws InvalidValueException {
        var player = Player.create();

        assertThrows(InvalidValueException.class, () -> {
            player.setCurrentHealth(-1);
        });

        assertThrows(InvalidValueException.class, () -> {
            player.setCurrencyAmount(-1);
        });

        assertThrows(InvalidValueException.class, () -> {
            player.setInventorySlots(-1);
        });

        assertThrows(InvalidValueException.class, () -> {
            player.setMinDamage(-1);
        });

        assertThrows(InvalidValueException.class, () -> {
            player.setMaxDamage(-1);
        });
    }

}