package no.hiof.samuelcd.tbage.exceptions;

/**
 * A custom exception class intended to be used when an Item object is added to the player's inventory.
 * (TreeMap<String, Item> inventory class member inside the Player class).
 * This exception can, for example arise when the player purchases a new item by interacting and trading
 * with an ally, or by gaining an item from defeating an enemy..
 */
public class InventoryFullException extends Exception {
    /**
     *
     * @param message Allows a message to be passed to the developer for debugging purposes.
     */
    public InventoryFullException(String message)
    {
        super(message);
    }
}
