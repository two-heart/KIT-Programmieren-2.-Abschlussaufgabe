package edu.kit.informatik.logic;

/**
 * String constants for the command line interface. The user output should not be
 * obtained via the {@link Enum#name()} method but via the {@link this#toString()} ()} method. This
 * class contains all <b>text</b> that during the normal is printed to the user and some
 * frequently used input patterns.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public enum ErrorMessages {

    /**
     * Error message saying there is a cycle.
     */
    CYCLE("this definition would create a cycle."),
    /**
     * Announce the illegal branch.
     */
    CYCLE_DESCRIPTION("The following branch is not allowed: "),
    /**
     * Error message for duplicates
     */
    DUPLICATE("there is already an BOM with this name."),
    /**
     * Error message saying the provided amount is less or equals zero.
     */
    NOT_POSITIVE("an amount must be positive."),
    /**
     * Error message saying there is no such item.
     */
    NONEXISTENT("no such item."),
    /**
     * To indicate something that should be an assembly is actually a component.
     */
    IS_COMPONENT("this is a component."),
    /**
     * A the direct amount of the child of a assembly is not between 1 and 1000.
     */
    BOUNDS("quantities in BOM must be between 1 and 1000.");

    private final String text;

    /**
     * @param text the error message
     */
    ErrorMessages(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
