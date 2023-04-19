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

    /**
     *
     * @return Returns an initialised Ally object with a default UUID value as a name.
     */
    public static Ally create() {
        UUID randomlyGeneratedId = UUID.randomUUID();
        return new Ally(randomlyGeneratedId.toString(), null, null);
    }

    /**
     *
     * @param name A string that is used as an identifier for this ally.
     * @return Returns an initialised Ally object.
     */
    public static Ally create(String name) {
        return new Ally(name, null, null);
    }


    /**
     *
     * @param name A string that is used as an identifier for this ally.
     * @param abilities An existing TreeMap containing Ability objects.
     * @param items An existing TreeMap containing Item objects.
     * @return Returns an initialised Ally object.
     */
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
                            gameEngine.printMessage("You now have " + (int) player.getCurrencyAmount() +
                                    " gold remaining.");

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

    /**
     * A method to control the behaviour of an Ally when interacted with. If an onInteractionBehaviour exists
     * then this is executed, and then a trade is started.
     * @param gameEngine The current instance of the GameEngine is required so that dependencies such as the
     *                   current encounter and player can be referenced.
     */
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

    /**
     *
     * @return Returns true in the event that there is an on-use behavior set that can execute when the onUse()
     * method is called.
     */
    public boolean InteractionIsUseable() {
        return (onInteractionBehaviour != null);
    }

    /**
     *
     * @param onInteractionBehaviour Intended to use a lambda to allow developers to create custom behaviours when a
     *                               specific ally is interacted with.
     *                               Example of a lambda created using the generic Useable interface:
     *                               Useable onUse= (gameEngine) -> {
     *                                 gameEngine.printMessage("You look tired. Let me enhance your weapon..");
     *                                 var player = gameEngine.getPlayer();
     *                                 player.setMaxDamage((int)player.getMaxDamage() + 2);
     *                                 player.setMinDamage((int)player.getMinDamage() + 3);
     *                                 gameEngine.printMessage("Your weapon glows. You can now deal between "
     *                                         + (int)player.getMinDamage() +
     *                                         " and " + (int)player.getMaxDamage() + " damage per swing.");
     *                               };
     */
    public void setOnInteractionBehaviour(Useable onInteractionBehaviour) {
        this.onInteractionBehaviour = onInteractionBehaviour;
    }

    /**
     *
     * @return Returns the generic Useable interface if it exists.
     */
    public Useable getOnInteractionBehaviour() {
        return onInteractionBehaviour;
    }

    /**
     *
     * @return Returns a boolean value whether or not this ally has already been interacted with.
     */
    public boolean isInteracted() {
        return isInteracted;
    }

    /**
     *
     * @param interacted Sets whether or not this ally has been interacted with.
     */
    public void setInteracted(boolean interacted) {
        isInteracted = interacted;
    }

    /**
     *
     * @return Returns a string representation of this object. Note that this method is overridden, and not the same
     * as the default implementation.
     */
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