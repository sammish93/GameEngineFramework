# Example Code

The code provided in this folder can be used as a reference as to some of the uses of TBAGE.

## Example Game

An fully-executable example of a game with three encounters is provided [here](./Main.java).

# Scenarios

## Game Settings

### Create and run a game
```
main() {
	var game = GameEngine.create();

	game.run();
}
```

### Change a game’s general settings
```
main() {
	var game = GameEngine.create();

	game.getGameSettings().setEncounterModifier(100);

	var encounters = RandomEncounters.create(10);
	game.setEncounters(encounters);

	game.run();
}
```

### Change a game’s build settings to run in Java Swing
```
main() {
	var game = GameEngine.create();

	game.setPlatformToSwing();
	var settings = GameSettings.create();

	settings.setFontOutput(new Font("Monospaced", Font.BOLD, 14));
	settings.setFontInput(new Font("Papyrus", Font.ITALIC, 14));
	settings.setFontGeneral(new Font("Monospaced", Font.BOLD, 14));
	settings.setFontAnimated(true);
	settings.setFontAnimationSpeed(50);

	settings.setWindowResolution(1366, 768);
	settings.setFullscreen(false);
	settings.setWindowTitle("Dragon Hunter XVI");

	game.setGameSettings(settings);
	game.run();
}
```

## Game Progression and Encounter Generation

### Change encounter order to be pre-defined
```
main() {
	var game = GameEngine.create();

	var encounters = FixedEncounters.create();

	var introEncounter = NonCombatEncounter.create();
	var middleEncounter = NonCombatEncounter.create();
	var finalEncounter = CombatEncounter.create();

	encounters.addEncounter(introEncounter);
	encounters.addEncounter(middleEncounter, introEncounter);
	encounters.addEncounter(finalEncounter, middleEncounter);


	game.setEncounters(encounters);
	game.run();
}
```

### Change encounter order to be non-linear
```
main() {
	var game = GameEngine.create();

	var encounters = FixedEncounters.create();

	var introEncounter = NonCombatEncounter.create();
	var middleEncounter = NonCombatEncounter.create();
	var finalEncounter = CombatEncounter.create();

	encounters.addEncounter(introEncounter);
	encounters.addEncounter(introEncounter, middleEncounter);
	encounters.addEncounter(introEncounter, finalEncounter, "hatch");
	encounters.addEncounter(middleEncounter, finalEncounter);

	introEncounter.removeDefaultNavigationalVerbs();
	introEncounter.addNavigationalVerb("open");

	game.setEncounters(encounters);
	game.run();

}
```

## Encounter Customisation

### Create a custom combat encounter and add to a game
```
main() {
	var game = GameEngine.create();

	var encounter = CombatEncounter.create();

	var enemy = Enemy.create("skeleton");
	encounter.addEnemyToEnemies(enemy);

	var enemyRef = encounter.getEnemyFromEnemies("skeleton");
	enemyRef.setName("skeleton king");
	enemyRef.setMaxHealth(50);
	enemyRef.setMinDamage(10);
	enemyRef.setMaxDamage(15);
	enemyRef.setEnemyType("undead");

	var item = Item.create("Potion of Poison", 50, 0.8);
	Useable onUse = (gameEngine) -> {
   		var playerthing = gameEngine.getPlayer();
   		playerthing.subtractFromCurrentHealth(5);

   		gameEngine.printMessage("You have taken 5 damage!");
	};

	item.setOnUseBehaviour(onUse);
	enemyRef.addItemToItemTable(item);

	var ability = Ability.create("fireball");
	Useable onUse = (gameEngine) -> {
  	var player = gameEngine.getPlayer;
		player.subtractFromCurrentHealth(20);

   	gameEngine.printMessage("You took 20 damage from a fireball!");
	};
	ability.setOnUseBehaviour(onUse);
	enemyRef.addAbilityToAbilityPool(ability);

	var encounters = RandomEncounters.create(1);
	encounters.addEncounter(encounter);
	game.setEncounters(encounters);

	game.run();
}
```

### Further customisation of existing encounter
```
main() {
	var encounters = RandomEncounters.create(1);
	var encountersRef = ((RandomEncounters) game.getEncounters());
	var encounter = encountersRef.getEncounterPool().get("encounter name");
	encounters.removeEncounter(encounter);

	encounter.setImagePath("src/resources/encounter_image.png");

	encounter.setIntroductoryMessage("You have entered what appears to be a makeshift tavern.");
	encounter.setOnDefeatedMessage("The skeletons are reduced to a pile of bone meal. You find a skeleton key in the ash. Maybe " + 	"it will fit in the door to the north..");
	encounter.setHint("The big skeleton hits the hardest.");

	var prop1 = Prop.create("Door");
	var prop2 = Prop.create("Barrel");

	Useable onUseBarrel = (gameEngine) -> {
		var playerthing = gameEngine.getPlayer();
		playerthing.addToCurrentHealth(1);
		gameEngine.printMessage("You find a bit of old cheese at the bottom of the barrel. It doesn't look too bad... Maybe a nibble won't hurt. You recuperate a single point of health!");
	};
	Useable onUseSwitch = (gameEngine) -> {
		gameEngine.printMessage("A door unlocks in the distance..");
		var encounterthing = EncounterTraversalController.getCurrentEncounter();
   	encounterthing.setDefeated(true);
	};

	prop1.setOnUseBehaviour(onUseBarrel);
	prop2.setOnUseBehaviour(onUseSwitch);
	encounter.addPropToProps(prop1);
	encounter.addPropToProps(prop2);
	encounters.addEncounter(encounter, 0.5);

	game.setEncounters(encounters);

	game.run();
}
```

### Add alternative behaviour(s) to existing encounter based on previous encounter(s)
```
main() {
	var game = GameEngine.create();
	
	var feat = Feat.create("Mead King");
	encounter.addFeatToFeatRewards(feat);
	encounterTwo.addFeatToFeatChecks(feat);

	Useable onUseInitiation = (gameEngine) -> {
		var playerthing = gameEngine.getPlayer();
		if (playerthing.getFeatFromFeats("Mead King") != null) {
			gameEngine.printMessage("Thanks to your heightened agility from your mead-drinking antics, you manage to skip past a precarious rusty doornail. That could have been deadly!");
		} else {
			playerthing.subtractFromCurrentHealth(6);
			gameEngine.printMessage("On entering the encounter you suffered 6 damage from a rusty doornail!");
		}
	};

	encounterTwo.setOnInitiationBehaviour(onUseInitiation);

	game.run();
}
```

## Game and Module DTOs

### Export and import game
```
main() {
	var game = GameEngine.create();
	
	game.save("src/savedFile");

	var game2 = GameEngine.load("src/savedFile");

	game2.run();
}
```

### Export and import an encounter
```
main() {
	var game = GameEngine.create();

	encounter.save("src/encounter");

	var encounterImported = Encounter.load("src/encounter");

	var game2 = GameEngine.create();
	
	var encountersRef = ((RandomEncounters) game2.getEncounters());
	encountersRef.addEncounter(encounterImported);

	game2.run();
}
```
