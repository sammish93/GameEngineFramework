package no.hiof.samuelcd.tbage.models.npcs;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.exceptions.InvalidValueException;
import no.hiof.samuelcd.tbage.exceptions.InventoryFullException;
import no.hiof.samuelcd.tbage.interfaces.Useable;
import no.hiof.samuelcd.tbage.models.abilities.Ability;
import no.hiof.samuelcd.tbage.models.items.Item;
import no.hiof.samuelcd.tbage.tools.EncounterController;

import java.util.TreeMap;
import java.util.UUID;

import static no.hiof.samuelcd.tbage.GameEngine.scanner;

public class Ally extends NonPlayableCharacter {

    private Useable onInteractionBehaviour;
    private boolean isInteracted;


    private Ally(String name, TreeMap<String, Ability> abilities, TreeMap<String, Item> items) {
        super(name, abilities, items);

        isInteracted = false;
    }

    public static Ally create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Ally(randomlyGeneratedId.toString(), null, null);
    }

    public static Ally create(String name) {
        return new Ally(name, null, null);
    }


    public static Ally create(String name, TreeMap<String, Ability> abilities, TreeMap<String, Item> items) {
        return new Ally(name, abilities, items);
    }


    private void trade(GameEngine gameEngine) {
        if (!getNpcItemTable().isEmpty()) {
            gameEngine.printMessage("Do you wish to trade?");

            boolean isFinishedPurchasing = false;

            String answer;

            answer = scanner.nextLine();

            while (!isFinishedPurchasing) {
                if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) {
                    Item item = EncounterController.chooseItem(gameEngine, this);
                    if (item != null) {
                        var player = gameEngine.getPlayer();
                        try {
                            player.addItemToInventory(item);
                            player.subtractFromCurrencyAmount(item.getValue());
                            gameEngine.printMessage(item.getName() + " is added to your inventory.");
                            gameEngine.printMessage("You now have " + (int) player.getCurrencyAmount() + " gold remaining.");

                        } catch (InventoryFullException ex) {
                            gameEngine.printMessage(ex.getMessage());
                        } catch (InvalidValueException e) {
                            e.printStackTrace();
                        }
                    }
                }

                gameEngine.printMessage("Would you like to purchase another item?");
                answer = scanner.nextLine();

                if (!(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y"))) {
                    isFinishedPurchasing = true;
                }
            }

            gameEngine.printMessage("You stopped interacting.");
        }
    }

    public void onInteraction(GameEngine gameEngine) {
        if (onInteractionBehaviour != null) {
            if (!isInteracted) {
                if (getOnInteractionBehaviour() != null) {
                    try {
                        onInteractionBehaviour.onUse(gameEngine);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    trade(gameEngine);
                    isInteracted = true;
                } else {
                    gameEngine.printMessage("Interacting with this being does nothing.");
                    isInteracted = true;
                }
            } else {
                gameEngine.printMessage("You have already interacted with this being.");

                trade(gameEngine);
            }
        }
    }

    public boolean InteractionIsUseable() {
        return (onInteractionBehaviour != null);
    }

    public void setOnInteractionBehaviour(Useable onInteractionBehaviour) {
        this.onInteractionBehaviour = onInteractionBehaviour;
    }

    public Useable getOnInteractionBehaviour() {
        return onInteractionBehaviour;
    }

    public boolean isInteracted() {
        return isInteracted;
    }

    public void setInteracted(boolean interacted) {
        isInteracted = interacted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Ally Name: '" + getName() + "', " +
                "Has Custom Interaction: " + InteractionIsUseable() + ", " +
                "Is Interacted With: " + isInteracted);

        printItemTableAndAbilityPool(sb);

        return sb.toString();
    }
}