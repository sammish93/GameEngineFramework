package org.example;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.interfaces.Useable;
import sammish93.tbage.models.*;
import sammish93.tbage.tools.EncounterController;
import sammish93.tbage.tools.EncounterTraversalController;

public class Main {
    public static void main(String[] args) throws InventoryFullException, InvalidValueException {

        // Creates a Player object and changes player's hitpoints and currency amount from the default values.
        var player = Player.create();
        player.setMaxHealth(30);
        player.setCurrentHealth(30);
        player.setCurrencyAmount(350);


        // This game example will have 4 encounters. 2 of them will be combat encounters.
        var nCoEncounter = NonCombatEncounter.create("The Shifty Tavern");
        var nCoEncounter2 = NonCombatEncounter.create("The Shifty Tavern's Storage Room");
        var encounter = CombatEncounter.create("ENCOUNTER 1");
        var encounter2 = CombatEncounter.create("ENCOUNTER 2");


        // Creating an item with a single use, and with a 50% chance of being dropped upon enemy being defeated.
        var item = Item.create("Mithril Javelin");
        item.setNumberOfUses(1);
        item.setValue(200);
        item.setDropChance(0.5);
        // A lambda expression containing an example of how the developer can further customise individual instantiated
        // object behaviours. In this case when the item is used then it inflicts 10 damage to an enemy that is
        // selected by the player.
        // Note that this lambda can be as simple or as complex as the developer requires. It doesn't even have to
        // be present at all.
        Useable onUseJavelin = (gameEngine) -> {
            // Retrieves the current encounter from the static .getCurrentEncounter() method.
            var encounterthing = EncounterTraversalController.getCurrentEncounter();
            // If conditional that handles behaviour if there are no enemies present.
            if (encounterthing.getClass() == NonCombatEncounter.class ||
                    ((CombatEncounter) encounterthing).getEnemies().isEmpty()) {
                gameEngine.printMessage("You throw the javelin at a nefarious visage. It was only the shadow " +
                        "of a mouse. Oops.");
            } else {
                // Calls the static chooseNpc() method to allow the player to select an NPC to use the item on.
                var target = EncounterController.chooseNpc(gameEngine, encounterthing);
                if (target instanceof Enemy) {
                    // Inflicts 10 damage to the enemy.
                    ((Enemy) target).subtractFromCurrentHealth(10);
                    gameEngine.printMessage(((Enemy) target).getName() + " has taken 10 damage!");
                    if (((Enemy) target).getEnemyHealthStatus().equalsIgnoreCase("dead")) {
                        gameEngine.printMessage(target.getName() + " dies from your item!");
                    }
                }
            }
        };
        item.setOnUseBehaviour(onUseJavelin);
        // Places two instances of the same object in the player's inventory. Items will be cloned and will be handled
        // as if they are individual.
        player.addItemToInventory(item);
        player.addItemToInventory(item);

        // Creates an item with a 100% drop chance. Note that this item doesn't have any onUse behaviour.
        var item2 = Item.create("Skeleton Key", 50);
        item2.setDropChance(1);


        // Creates three Prop objects that can be placed inside an encounter. These can be interacted with by the player
        // using the 'investigate' command. These props -can- be provided with the same Useable lambda expression as
        // shown above to provide additional customisation.
        var prop1 = Prop.create("Door");
        var prop2 = Prop.create("Barrel");
        var prop3 = Prop.create("Switch");

        Useable onUseBarrel = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            playerthing.addToCurrentHealth(1);
            gameEngine.printMessage("You find a bit of old cheese at the bottom of the barrel. It doesn't look " +
                    "too bad... Maybe a nibble won't hurt. You recuperate a single point of health!");
        };

        Useable onUseSwitch = (gameEngine) -> {
            gameEngine.printMessage("A door unlocks in the distance..");
            // Retrieves the current encounter and sets the defeated state to true.
            // Note that while combat encounters will automatically be defeated upon having no living enemies present,
            // non combat encounters will have to have behaviour present that directly calls the .setDefeated() method.
            // This can be implemented in a Prop object, or an allies .setOnInteractionBehaviour(), or even an
            // encounter's .setOnInitialBehaviour() (both shown as examples below).
            var encounterthing = EncounterTraversalController.getCurrentEncounter();
            encounterthing.setDefeated(true);
        };
        prop2.setOnUseBehaviour(onUseBarrel);
        prop3.setOnUseBehaviour(onUseSwitch);
        // Adds the props to the specified encounter.
        nCoEncounter.addPropToProps(prop1);
        nCoEncounter.addPropToProps(prop2);
        nCoEncounter.addPropToProps(prop3);
        // This string will be printed to the interface when the player traverses to the encounter for the first time.
        nCoEncounter.setIntroductoryMessage("You have entered what appears to be a makeshift tavern.");

        // Creates an Ally object, implements a Useable lambda that upgrades a player's weapon, sets the lambda to
        // run when the player first interacts with the Ally using the 'interact' command. The ally's inventory is then
        // populated with two items which the player can optionally purchase. Finally, the ally is added to the non
        // combat encounter.
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
        ally1.addItemToItemTable(item);
        ally1.addItemToItemTable(item2);
        nCoEncounter.addAllyToAllies(ally1);

        // Feats can also be rewarded to the player upon defeating an encounter. Here two feats are rewarded to the
        // player upon defeating said encounter.
        Feat featNotSecret = Feat.create("Mead King");
        // This feat is hidden from the player, with the 'isSecret' value set to true. This can be useful when the
        // developer wishes to hide the 'inner workings' of their game, but modify future encounters based on choices
        // and behaviours that the player was involved in earlier.
        Feat featSecret = Feat.create("SECRET_FEAT_CONTROLLING_INFLUENCING_FUTURE_BEHAVIOUR", true);
        nCoEncounter.addFeatToFeatRewards(featNotSecret);
        nCoEncounter.addFeatToFeatRewards(featSecret);


        nCoEncounter2.setIntroductoryMessage("You enter a dark room. You hear mice crawling on the floor, and can " +
                "make out what appears to be footprints that disappear right before a carelessly flung carpet.");
        nCoEncounter2.setHint("The carpet looks to be covering something. Maybe you should investigate.");
        var hatchProp = Prop.create("Hatch");
        Useable onUseHatch = (gameEngine) -> {
            gameEngine.printMessage("It looks like it could be opened!");
        };
        hatchProp.setOnUseBehaviour(onUseHatch);
        nCoEncounter2.addPropToProps(hatchProp);


        // An example of a Useable lambda to customise behaviour when the player initially traverses to the encounter.
        Useable onUseInitiationFriendly = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            playerthing.addToCurrentHealth(4);
            gameEngine.printMessage("On entering the inn a friendly bartender gives you a wink and hands you a " +
                    "pint of mead. You take a sip and immediately feel quenched. You recuperate 4 health!");
        };
        nCoEncounter.setOnInitiationBehaviour(onUseInitiationFriendly);


        // Enemy objects can be placed in combat encounters and can be interacted with using the 'attack' command to
        // initiate turn-based combat. Here the enemy's default damage values are changed, and the enemy is provided
        // with an ability.
        var enemy = Enemy.create("Skeleton King");
        enemy.setMinDamage(3);
        enemy.setMaxDamage(6);
        // This enemy has a 50% chance of a melee attack each turn.
        enemy.setMeleeChancePerTurn(0.5);
        var ability = Ability.create("Fireball");
        Useable onUseFireball = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            playerthing.subtractFromCurrentHealth(8);
            gameEngine.printMessage("You have taken 8 damage from a fireball!");
        };
        ability.setOnUseBehaviour(onUseFireball);
        // The enemy has a 50% chance of casting fireball each turn. Note that this is weighted against any other
        // abilities that the enemy may be capable of.
        ability.setAbilityProbabilityPerTurn(0.5);
        enemy.addAbilityToAbilityPool(ability);

        // This enemy will have a chance to drop said item based on the drop chance of the item (not weighted).
        enemy.addItemToItemTable(item2);
        // This enemy will provide 20 currency to the player upon being defeated.
        enemy.setCurrencyReceivedOnDeath(20);

        var enemy2 = Enemy.create("Skeleton Minion");
        enemy2.setCurrencyReceivedOnDeath(12);

        // Adds three enemies to an encounter. Note that 'enemy2' is added twice. This object is cloned, and thus is
        // treated as two separate instances.
        encounter.addEnemyToEnemies(enemy);
        encounter.addEnemyToEnemies(enemy2);
        encounter.addEnemyToEnemies(enemy2);


        // Encounters can have an optional hint that can be displayed in the interface. This implementation of the hint
        // provides the player with an item that has 3 uses. If a hint exists then it will be printed to the interface.
        var hintItem = Item.create("Hint Scroll");
        hintItem.setNumberOfUses(3);
        Useable onUseHint = (gameEngine) -> {
            var encounterthing = EncounterTraversalController.getCurrentEncounter();
            if (!encounterthing.getHint().isEmpty()) {
                gameEngine.printMessage(encounterthing.getHint());
            } else {
                gameEngine.printMessage("There isn't a hint to be found.");
            }
        };
        hintItem.setOnUseBehaviour(onUseHint);
        player.addItemToInventory(hintItem);
        encounter.setHint("The big skeleton hits the hardest.");


        // A Useable lambda that checks to see if the player has been awarded with a specific feat previously. If said
        // feat does not exist then the player receives damage.
        Useable onUseInitiation = (gameEngine) -> {
            var playerthing = gameEngine.getPlayer();
            if (playerthing.getFeatFromFeats("Mead King") != null) {
                gameEngine.printMessage("Thanks to your heightened agility from your mead-drinking antics, you " +
                        "manage to skip past a precarious rusty doornail. That could have been deadly!");
            } else {
                playerthing.subtractFromCurrentHealth(6);
                gameEngine.printMessage("On entering the encounter you suffered 6 damage from a rusty doornail!");
            }
        };
        encounter.setIntroductoryMessage("The room is pitch black. You take a few steps forward and suddenly the " +
                "sconces on the wall come to life. A pile of bones scattered about the floor begin to coalesce into " +
                "a threatening form. Ready your weapon!.");
        encounter.setOnInitiationBehaviour(onUseInitiation);
        encounter.setOnDefeatedMessage("The skeletons are reduced to a pile of bone meal. You find a skeleton key in " +
                "the ash. Maybe it will fit in the door to the north..");
        encounter.addPropToProps(prop2);

        encounter2.setIntroductoryMessage("You have reached the end of the game, congratulations!");


        // An example of how to provide a fixed traversal method where the player goes from nCoEncounter to
        // nCoEncounter2. The player then progresses to encounter upon entering 'open hatch' (alternatively, the player
        // can return to the previous encounter by entering 'previous encounter', enabling backtracking). The player then
        // has to defeat the enemies present (because it is a combat encounter) before they can enter the prompt
        // 'unlock door' to progress to the final encounter).
        // Note that fixed traversal -can- be linear, but can also be non-linear, with a single encounter leading to
        // several others.
        // FIXED TRAVERSAL
        var encounters = FixedEncounters.create();

        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter, nCoEncounter2);
        encounters.addEncounter(nCoEncounter2, nCoEncounter, "encounter");

        nCoEncounter2.removeDefaultNavigationalVerbs();
        nCoEncounter2.addNavigationalVerb("previous");
        nCoEncounter2.addNavigationalVerb("open");
        encounters.addEncounter(nCoEncounter2, encounter, "hatch");

        encounter.removeDefaultNavigationalVerbs();
        encounter.addNavigationalVerb("unlock");
        encounters.addEncounter(encounter, encounter2, "door");


        // An example of how to implement a random encounter permutation. Note that the encounters provided are the
        // same as the ones used for fixed traversal. The only difference is that the order will be randomised each
        // time, and will only be an A-to-B linear method of traversal.
        /*
        // RANDOM TRAVERSAL
        var encounters = RandomEncounters.create();
        nCoEncounter2.removeDefaultNavigationalVerbs();
        nCoEncounter2.addNavigationalVerb("open");
        nCoEncounter2.addNavigationalNoun("hatch");
        encounter.removeDefaultNavigationalVerbs();
        encounter.addNavigationalVerb("unlock");
        encounter.addNavigationalNoun("door");
        encounters.addEncounter(nCoEncounter);
        encounters.addEncounter(nCoEncounter2);
        encounters.addEncounter(encounter);
        */

        // Custom instances of Player and Encounters are provided as parameters here, while a default implementation
        // of GameSettings is created by default. Other overloads exist.
        var game = GameEngine.create(player, encounters);

        // Runs the game in a Java Swing GUI.
        game.setPlatformToSwing();
        // Runs the game in a window at the specificed resolution.
        game.getGameSettings().setWindowResolution(1200,800);

        // Executes the code required to run the game.
        game.run();
    }
}
