package sammish93.tbage.exceptions;

/**
 * A custom exception class that is intended to be used for input validation before compile time to ensure
 * that all objects are created with no unintended effects (such as a zero value being given, and then being
 * passed to a multiplication operator).
 */
public class InvalidValueException extends Exception {
    /**
     *
     * @param message Allows a message to be passed to the developer for debugging purposes.
     */
    public InvalidValueException(String message)
    {
        super(message);
    }
}
