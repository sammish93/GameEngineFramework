import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.encounters.CombatEncounter;
import no.hiof.samuelcd.tbage.models.encounters.Encounter;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.models.npcs.Enemy;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        var game = GameEngine.create();
        //game.setPlatformToSwing();
        //game.run();


        var item = Item.create("the item name");
        //System.out.println(game);
        Useable onUse = (gameEngine) -> {
            gameEngine.getPlayer().setCurrentHealth(500);
            gameEngine.printMessage("Your health increased!");
        };

        item.setOnUseBehaviour(onUse);

        //System.out.println(game.getPlayer().getInventorySlots());
        game.getPlayer().addItemToInventory(item);

        //System.out.println(game.getPlayer().getItemFromInventory("the item name"));
        game.save("src/game.ser");


        var game2 = GameEngine.load("src/game.ser");
        System.out.println(game2.getPlayer().getCurrentHealth());
        game2.run();
        //encounter2.getEnemyFromEnemies("defaultNpcName").getItemFromItemTable("the item name").onUse(game);

        //System.out.println(game.getPlayer().getCurrentHealth());


        /*
        System.out.println(game.getPlayer().getCurrentHealth());
        CombatEncounter encounter2 = (CombatEncounter)Encounter.load("src/encounter.ser");

        System.out.println(encounter2.getEnemyFromEnemies("defaultNpcName").getItemFromItemTable("the item name").getName());
        encounter2.getEnemyFromEnemies("defaultNpcName").getItemFromItemTable("the item name").onUse();
        System.out.println(game.getPlayer().getCurrentHealth());
        */
    }
}
