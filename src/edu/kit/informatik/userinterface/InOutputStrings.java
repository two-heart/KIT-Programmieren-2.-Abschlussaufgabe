package edu.kit.informatik.userinterface;

/**
 * String constants for the command line interface. The user output should not be obtained via the
 * {@link Enum#name()} method but via the {@link this#toString()} method. This class contains all <b>text</b> that
 * during the normal is printed to the user and some frequently used input patterns.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public enum InOutputStrings {
    /**
     * Regex pattern for a identifier.
     */
    NAME_PATTERN("[a-zA-Z]+"),
    /**
     * Regex pattern for natural numbers without leading zeros.
     */
    NUMBER_PATTERN("(?!(0[0-9]))[0-9]+"),
    /**
     * Separates the command from its potential arguments.
     */
    COMMAND_SEPARATOR(" "),
    /**
     * Separates one argument from another.
     */
    ARGUMENT_SEPARATOR(";"),
    /**
     * Separates the parts of one argument.
     */
    INNER_SEPARATOR(":"),
    /**
     * Symbol for a definition.
     */
    DEFINITION_SEPARATOR("="),
    /**
     * Signals the user command went well, and there is no other output to show.
     */
    POSITIVE("OK"),
    /**
     * If a argument is grater than {@link Integer#MAX_VALUE}. It contains a hint to let the user know that he is far
     * off and should try a way smaller number.
     */
    WAY_TO_HUGE("This number is way to big. Try something less or equals 1000."),
    /**
     * Signal an assembly does not have any more assembly it consists of.
     */
    EMPTY("EMPTY"),
    /**
     * Tell the user that something is a component.
     */
    SINGLE_PART("COMPONENT"),
    /**
     * Tell the user that they are trying to define something twice (or more).
     */
    DUPLICATE_DEFINITION("duplicate definition of an assembly/part"),
    /**
     * To tell the user a command exists, but expects different arguments.
     */
    WRONG_ARGUMENTS("the supplied argument is invalid."),
    /**
     * To tell the user is no such command.
     */
    NO_MATCHING_COMMAND("no matching command found.");

    private final String text;

    /**
     * @param text The string that might be used to output it to the user
     */
    InOutputStrings(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
