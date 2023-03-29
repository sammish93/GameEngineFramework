import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.encounters.CombatEncounter;
import no.hiof.samuelcd.tbage.models.encounters.Encounter;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.models.player.Player;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        var settings = GameSettings.create();
        var player = Player.create();
        var encounters = FixedEncounters.create();
        var encounter = CombatEncounter.create("ENCOUNTER 1");
        var encounter2 = CombatEncounter.create("ENCOUNTER 2");
        var encounter3 = CombatEncounter.create("ENCOUNTER 3");
        var enemy = Enemy.create("Skeleton King");
        var ability = Ability.create("ABILITY 1");
        var item = Item.create("ITEM 1");
        var enemy2 = Enemy.create("Skeleton Minion");
        var ability2 = Ability.create("ABILITY 2");
        var item2 = Item.create("ITEM 2");
        enemy.addItemToItemTable(item);
        enemy.addAbilityToAbilityPool(ability);
        encounter.addEnemyToEnemies(enemy);
        encounter.addEnemyToEnemies(enemy2);
        encounters.addEncounter(encounter);

        enemy2.addItemToItemTable(item2);
        enemy2.addAbilityToAbilityPool(ability2);
        encounter2.addEnemyToEnemies(enemy2);
        encounters.addEncounter(encounter, encounter2, "defeated");
        encounters.addEncounter(encounter, encounter3, "north");
        encounters.addEncounter(encounter3, encounter, "south");
        var game = GameEngine.create(settings, player, encounters);
        game.run();
    }
}
