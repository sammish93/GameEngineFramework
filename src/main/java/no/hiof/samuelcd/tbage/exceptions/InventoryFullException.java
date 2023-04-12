package no.hiof.samuelcd.tbage.exceptions;

public class InventoryFullException extends Exception {
    public InventoryFullException(String message)
    {
        super(message);
    }
}
