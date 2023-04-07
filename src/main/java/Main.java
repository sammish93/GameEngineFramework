import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.encounters.CombatEncounter;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.encounters.RandomEncounters;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.models.player.Player;
import no.hiof.samuelcd.tbage.tools.EncounterController;
import no.hiof.samuelcd.tbage.tools.EncounterTraversalController;
import no.hiof.samuelcd.tbage.tools.StringParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        var settings = GameSettings.create();
        var player = Player.create();
        //var encounters = FixedEncounters.create();
        var encounter = CombatEncounter.create("ENCOUNTER 1");
        var encounter2 = CombatEncounter.create("ENCOUNTER 2");
        var encounter3 = CombatEncounter.create("ENCOUNTER 3");
        var enemy = Enemy.create("Skeleton King");
        var ability = Ability.create("Heal");
        var item = Item.create("Potion of Poison");
        enemy.addItemToItemTable(item);

        enemy.setMeleeChancePerTurn(0.5);

        Useable onUse = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            playerthing.subtractFromCurrentHealth(5);

            gameEngine.printMessage("You have taken 5 damage!");
        };

        Useable onUse2 = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            //CombatTurn.
            var encounterthing = EncounterTraversalController.getCurrentEncounter();
            var target = EncounterController.chooseNpc(gameEngine, encounterthing);
            if (target instanceof Enemy) {
                ((Enemy) target).subtractFromCurrentHealth(10);
                gameEngine.printMessage(((Enemy) target).getName() + " has taken 10 damage!");
                if (((Enemy) target).getEnemyHealthStatus().equalsIgnoreCase("dead")) {
                    gameEngine.printMessage(target.getName() + " dies from your item!");
                }
            }
        };
        item.setOnUseBehaviour(onUse);

        Useable onUse3 = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();

            playerthing.subtractFromCurrentHealth(5);
            gameEngine.printMessage("You have taken 5 damage!");
        };
        ability.setOnUseBehaviour(onUse3);
        ability.setAbilityProbabilityPerTurn(0.6);



        var enemy2 = Enemy.create("Skeleton Minion");
        var ability2 = Ability.create("Fireball");
        var item2 = Item.create("Mithril Javelin");
        item2.setNumberOfUses(0);
        item2.setOnUseBehaviour(onUse2);

        var item3 = Item.create("Skeleton Key");
        enemy.addItemToItemTable(item);
        enemy.addAbilityToAbilityPool(ability);
        encounter.addEnemyToEnemies(enemy);
        encounter.addEnemyToEnemies(enemy2);
        encounter.addEnemyToEnemies(enemy2);

        enemy2.addItemToItemTable(item2);
        enemy2.addAbilityToAbilityPool(ability2);
        encounter2.addEnemyToEnemies(enemy2);
        /*
        encounters.addEncounter(encounter);
        encounters.addEncounter(encounter, encounter3, "defeated");
        encounters.addEncounter(encounter2, encounter, "previous");
        encounters.addEncounter(encounter, encounter3, "north");
        encounters.addEncounter(encounter3, encounter, "ladder");
        ArrayList<String> traversal = new ArrayList<>();
        traversal.add("climb");
        encounters.getEncounter(encounter3.getName()).setNavigationalVerbs(traversal);
         */

        var encounters = RandomEncounters.create();
        encounters.addEncounter(encounter);
        encounters.addEncounter(encounter2);
        encounters.addEncounter(encounter3);


        player.addItemToInventory(item);
        player.addItemToInventory(item2);
        player.addItemToInventory(item2);
        player.addItemToInventory(item2);

        var game = GameEngine.create(settings, player, encounters);

        game.run();
    }
}
