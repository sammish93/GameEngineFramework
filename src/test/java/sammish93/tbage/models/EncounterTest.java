package sammish93.tbage.models;

import org.junit.jupiter.api.Test;
import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EncounterTest {

    GameEngine gameEngine = mock(GameEngine.class);

    @Test
    void assertsNonCombatEncounterCanOnlyHaveOneInstanceOfSingleAlly() throws InvalidValueException {
        var encounter = NonCombatEncounter.create();
        var ally = Ally.create();

        encounter.addAllyToAllies(ally);

        assertNotNull(encounter.getAllyFromAllies(ally.getName()));
        assertEquals(1, encounter.getAllies().size());

        encounter.addAllyToAllies(ally);

        assertEquals(1, encounter.getAllies().size());
    }

    @Test
    void assertsCombatEncounterDuplicatesSingleEnemy() throws InvalidValueException {
        var encounter = CombatEncounter.create();
        String enemyName = "Skeleton";
        var enemy = Enemy.create(enemyName);

        encounter.addEnemyToEnemies(enemy);

        assertNotNull(encounter.getEnemyFromEnemies(enemyName));
        assertEquals(1, encounter.getEnemies().size());

        encounter.addEnemyToEnemies(enemy);

        assertNull(encounter.getEnemyFromEnemies(enemyName));
        assertEquals(2, encounter.getEnemies().size());
    }

    @Test
    void assertsCombatEncounterDuplicatesRenameDynamically() throws InvalidValueException {
        var encounter = CombatEncounter.create();
        String enemyName = "Skeleton";
        var enemy = Enemy.create(enemyName);

        encounter.addEnemyToEnemies(enemy);

        assertNotNull(encounter.getEnemyFromEnemies(enemyName));

        encounter.addEnemyToEnemies(enemy);

        assertNull(encounter.getEnemyFromEnemies(enemyName));
        assertNotNull(encounter.getEnemyFromEnemies(enemyName + " 1"));
        assertNotNull(encounter.getEnemyFromEnemies(enemyName + " 2"));
    }

}