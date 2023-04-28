package sammish93.tbage.models;

import org.junit.jupiter.api.Test;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;

import static org.junit.jupiter.api.Assertions.*;

class NonPlayableCharacterTest {

    @Test
    void assertsItemAddedToInventoryDoesNotDuplicate() throws InvalidValueException, InventoryFullException {

        var npc = Ally.create();

        String itemName = "Potion";
        var item = Item.create(itemName);

        npc.addItemToItemTable(item);

        assertTrue(npc.getNpcItemTable().containsKey(itemName));

        npc.addItemToItemTable(item);

        assertTrue(npc.getNpcItemTable().containsKey(itemName));
    }

    @Test
    void assertsAbilityAddedToEnemyExists() throws InvalidValueException, InventoryFullException {

        var npc = Enemy.create();

        String abilityName = "Fireball";
        var ability = Ability.create(abilityName);

        npc.addAbilityToAbilityPool(ability);

        assertEquals(abilityName, npc.getAbilityFromAbilityPool(abilityName).getName());
    }

    @Test
    void assertsEnemyWithZeroPercentMeleeChanceIsNotMelee() throws InvalidValueException {

        var npc = Enemy.create();

        assertTrue(npc.isMelee());

        npc.setMeleeChancePerTurn(0);

        assertFalse(npc.isMelee());
    }

    @Test
    void assertsEnemyHealthStatusUpdatesWithCurrentHealth() throws InvalidValueException {

        var npc = Enemy.create();

        assertEquals("Full Health", npc.getEnemyHealthStatus());

        npc.subtractFromCurrentHealth(1);
        assertEquals("Slightly Injured", npc.getEnemyHealthStatus());

        npc.subtractFromCurrentHealth(5);
        assertEquals("Injured", npc.getEnemyHealthStatus());

        npc.subtractFromCurrentHealth(3);
        assertEquals("Close to Death", npc.getEnemyHealthStatus());

        npc.subtractFromCurrentHealth(9999);
        assertEquals("Dead", npc.getEnemyHealthStatus());
    }

    @Test
    void assertsSubtractFromCurrentHealthIsNeverNegativeInteger() throws InvalidValueException, InventoryFullException {

        var npc = Enemy.create();
        npc.setCurrentHealth(10);

        assertEquals(10, npc.getCurrentHealth());

        npc.subtractFromCurrentHealth(5);

        assertEquals(5, npc.getCurrentHealth());

        npc.subtractFromCurrentHealth(9999);

        assertEquals(0, npc.getCurrentHealth());
    }

    @Test
    void assertsExceptionIsThrownWhenInvalidValuesAreGiven() throws InvalidValueException {
        var npc = Enemy.create();

        assertThrows(InvalidValueException.class, () -> {
            npc.setCurrentHealth(-1);
        });

        assertThrows(InvalidValueException.class, () -> {
            npc.setCurrencyReceivedOnDeath(-1);
        });

        assertThrows(InvalidValueException.class, () -> {
            npc.setMinDamage(-1);
        });

        assertThrows(InvalidValueException.class, () -> {
            npc.setMaxDamage(-1);
        });
    }

}