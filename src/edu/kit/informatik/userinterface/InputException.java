package edu.kit.informatik.userinterface;

/**
 * Thrown if the user makes a syntactically incorrect input. This should exception should and must be caught and
 * the user should be printed a error message, without terminating the program.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class InputException extends Exception {
    /**
     * note this exception can be constructed and used throughout the whole program.
     *
     * @param message The cause of the exception, that might be printed to the user
     *                via <code>ReadWrite.writeError</code>
     */
    public InputException(final String message) {
        super(message);
    }
}
