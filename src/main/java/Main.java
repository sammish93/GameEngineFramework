import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.encounters.CombatEncounter;
import no.hiof.samuelcd.tbage.models.encounters.FixedEncounters;
import no.hiof.samuelcd.tbage.models.encounters.NonCombatEncounter;
import no.hiof.samuelcd.tbage.models.encounters.RandomEncounters;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.npcs.Ally;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;
import no.hiof.samuelcd.tbage.models.player.Player;
import no.hiof.samuelcd.tbage.models.props.Prop;
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
            if (((CombatEncounter) encounterthing).getEnemies().isEmpty()) {
                gameEngine.printMessage("There are no enemies to use this item on.");
            } else {
                var target = EncounterController.chooseNpc(gameEngine, encounterthing);
                if (target instanceof Enemy) {
                    ((Enemy) target).subtractFromCurrentHealth(10);
                    gameEngine.printMessage(((Enemy) target).getName() + " has taken 10 damage!");
                    if (((Enemy) target).getEnemyHealthStatus().equalsIgnoreCase("dead")) {
                        gameEngine.printMessage(target.getName() + " dies from your item!");
                    }
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
        item2.setValue(200);

        var hintItem = Item.create("Hint Scroll");
        hintItem.setNumberOfUses(1);

        Useable onUseHint = (gameEngine) -> {
            var encounterthing = EncounterTraversalController.getCurrentEncounter();
            if (!encounterthing.getHint().isEmpty()) {
                gameEngine.printMessage(encounterthing.getHint());
            } else {
                gameEngine.printMessage("There isn't a hint to be found.");
            }
        };

        hintItem.setOnUseBehaviour(onUseHint);

        Useable onUseInitiation = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            playerthing.subtractFromCurrentHealth(6);
            gameEngine.printMessage("On entering the encounter you suffered 6 damage from a rusty doornail!");
        };

        encounter.setOnInitiationBehaviour(onUseInitiation);

        encounter.setHint("The big skeleton hits the hardest.");

        var item3 = Item.create("Skeleton Key");
        item3.setValue(50);
        enemy.addItemToItemTable(item);
        enemy.addAbilityToAbilityPool(ability);
        encounter.addEnemyToEnemies(enemy);
        encounter.addEnemyToEnemies(enemy2);
        encounter.addEnemyToEnemies(enemy2);

        enemy2.addItemToItemTable(item2);
        enemy2.addAbilityToAbilityPool(ability2);
        encounter2.addEnemyToEnemies(enemy2);

        var nCoEncounter = NonCombatEncounter.create();
        var prop1 = Prop.create("Door");
        var prop2 = Prop.create("Barrel");
        var prop3 = Prop.create("Switch");

        Useable onUseBarrel = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            playerthing.addToCurrentHealth(1);
            gameEngine.printMessage("You find a bit of old cheese at the bottom of the barrel. It doesn't look too " +
                    "bad... Maybe a nibble won't hurt. You recuperate a single point of health!");
            var encounterthing = EncounterTraversalController.getCurrentEncounter();
            encounterthing.setDefeated(true);
        };

        Useable onUseSwitch = (gameEngine) -> {
            gameEngine.printMessage("A door unlocks in the distance..");
            var encounterthing = EncounterTraversalController.getCurrentEncounter();
            encounterthing.setDefeated(true);
        };
        prop2.setOnUseBehaviour(onUseBarrel);
        prop3.setOnUseBehaviour(onUseSwitch);
        nCoEncounter.addPropToProps(prop1);
        nCoEncounter.addPropToProps(prop2);
        nCoEncounter.addPropToProps(prop3);
        nCoEncounter.setIntroductoryMessage("You have entered what appears to be a makeshift tavern");

        Ally ally1 = Ally.create("Friendly Wizard");
        Useable onUseWizard = (gameEngine) -> {
            gameEngine.printMessage("You look tired. Let me enhance your weapon..");
            var playerthing = gameEngine.getPlayer();
            playerthing.setMaxDamage((int)playerthing.getMaxDamage() + 2);
            playerthing.setMinDamage((int)playerthing.getMinDamage() + 3);
            gameEngine.printMessage("Your weapon glows. You can now deal between "
                    + (int)playerthing.getMinDamage() +
                    " and " + (int)playerthing.getMaxDamage() + " damage per swing.");
        };
        ally1.setOnInteractionBehaviour(onUseWizard);
        ally1.addItemToItemTable(item3);
        ally1.addItemToItemTable(item2);
        ally1.addItemToItemTable(item2);
        nCoEncounter.addAllyToAllies(ally1);

        Useable onUseInitiationFriendly = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            playerthing.addToCurrentHealth(4);
            gameEngine.printMessage("On entering the inn a friendly bartender gives you a wink and hands you a pint " +
                    "of mead. You take a sip and immediately feel quenched. You recuperate 4 health!");
        };
        nCoEncounter.setOnInitiationBehaviour(onUseInitiationFriendly);



        encounter.setIntroductoryMessage("This is a nice encounter with 3 friendly skeletons.");
        var encounters = FixedEncounters.create();
        //encounters.addEncounter(encounter);
        //encounters.addEncounter(encounter, nCoEncounter, "defeated");
        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter, encounter2, "defeated");

        /*
        encounters.addEncounter(encounter2, encounter, "previous");
        encounters.addEncounter(encounter, encounter3, "north");
        encounters.addEncounter(encounter3, encounter, "ladder");
         */
        ArrayList<String> traversal = new ArrayList<>();
        traversal.add("climb");
        //encounters.getEncounter(encounter3.getName()).setNavigationalVerbs(traversal);




        /*
        var encounters = RandomEncounters.create();
        encounters.addEncounter(encounter);
        encounters.addEncounter(encounter2);
        encounters.addEncounter(encounter3);
        */


        player.addItemToInventory(item);
        player.addItemToInventory(item2);
        player.addItemToInventory(item2);
        player.addItemToInventory(hintItem);
        player.setCurrencyAmount(1000.0);


        var game = GameEngine.create(settings, player, encounters);

        game.run();
    }
}
