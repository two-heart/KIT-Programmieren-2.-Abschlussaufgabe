package edu.kit.informatik.logic;

import java.util.List;

/**
 * Signals that the intended operation would create a cycle. It it possible to obtain a error message that contains
 * the (first) illegal branch that would have been created.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class CycleException extends LogicException {

    /**
     * Preferably use {@link this#CycleException(String, List)}!
     *
     * @param message what went wrong?
     */
    CycleException(String message) {
        super(message);
    }

    /**
     * Constructs a CycleExceptions with the provided message followed by information about the illegal branch.
     *
     * @param message a description of what went wrong.
     * @param trace   the illegal branch of the tree, that would have been created.
     */
    CycleException(String message, List<String> trace) {
        super(message + " " + ErrorMessages.CYCLE_DESCRIPTION + String.join("-", trace));
    }

}
