package edu.kit.informatik.logic;

/**
 * Signals that a public method in the logic package can not be executed with the provided arguments at this time.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class LogicException extends Exception {
    /**
     * Constructs a new {@link LogicException} with an <code>message</code> that can later be obtained via
     * {@link Exception#getMessage()}.
     *
     * @param message what went wrong?
     */
    LogicException(String message) {
        super(message);
    }
}
