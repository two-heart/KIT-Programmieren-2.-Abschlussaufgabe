package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.userinterface.InOutputStrings;
import edu.kit.informatik.userinterface.InputException;
import edu.kit.informatik.userinterface.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A factory in the spirit of the factory PATTERN. It provides subclasses of {@link Command} depending on user input.
 * <p>
 * This class deals with user input.
 *
 * @author Liam Wachter
 * @version 1.0
 */
// This should avoid accidentally creating invalid commands in the code outside of this package.
// Instead of the factory pattern I could also use a enum with a static method that returns the matching entry for a
// string, but I think this is more expandable and fault save approach.
public class CommandFactory {
    /**
     * It is possible to assign an alias for an command or create another set of commands e.g for testing.
     */
    private static final Map<Pattern, Class<? extends Command>> COMMAND_PACKAGE
            = new HashMap<Pattern, Class<? extends Command>>() {
        {
            put(Quit.getDefaultPattern(), Quit.class);
            put(AddAssembly.getDefaultPattern(), AddAssembly.class);
            put(AddPart.getDefaultPattern(), AddPart.class);
            put(GetAssemblies.getDefaultPattern(), GetAssemblies.class);
            put(GetComponents.getDefaultPattern(), GetComponents.class);
            put(PrintAssembly.getDefaultPattern(), PrintAssembly.class);
            put(RemoveAssembly.getDefaultPattern(), RemoveAssembly.class);
            put(RemovePart.getDefaultPattern(), RemovePart.class);
        }
    };

    private final Session session;

    /**
     * Constructs a {@link CommandFactory} for one specific session.
     *
     * @param session the {@link Session} that uses this {@link CommandFactory}
     */
    // Technically it would be possible to not pass the session in here, but adding a return
    // value to the command execute() method would be bad regarding the command query separation and adding more
    // methods to check for example for quit or reset, would make the usage unintuitive.
    public CommandFactory(final Session session) {
        this.session = session;
    }

    /**
     * Get a command from the default package. At the moment there is just one package.
     *
     * @param input the expression that will be matched against the command syntax
     * @return the command that matches the supplied pattern or null if no command matches
     */
    private static Class<? extends Command> getFromDefaultPackage(final String input) {
        // This lambda searches for the first entry in the hash map that has a regex pattern that matches the input.
        // If none was found it returns null.
        // This kind of kills me all advantages of a hash map when it comes to speed.
        return COMMAND_PACKAGE.keySet()
                .stream()
                .filter(s -> s.matcher(input).matches())
                .findFirst()
                .map(COMMAND_PACKAGE::get)
                .orElse(null);
    }

    /**
     * This method can be used, when no command matches the user input, to check if there is a command with this
     * name. So the user just got the arguments of a command wrong.
     *
     * @param input the raw command the user entered.
     * @return if there is a command with this name.
     */
    private boolean similarCommand(String input) {
        // make sure the split afterwards has at least size one
        if (input.matches(" .*")) {
            return false;
        }
        final String enteredCommand = input.split(InOutputStrings.COMMAND_SEPARATOR.toString())[0];
        return COMMAND_PACKAGE
                .keySet()
                .stream()
                .map(Pattern::toString)
                .anyMatch(patternText -> patternText.startsWith(enteredCommand + InOutputStrings.COMMAND_SEPARATOR));
    }

    /**
     * Get a command by a input from the user.
     *
     * @param input the raw string the user enters.
     * @return a reference to the corresponding command.
     * @throws InputException if there is no matching command.
     */
    public Command getCommand(final String input) throws InputException {
        final Command command;
        Class<? extends Command> model = getFromDefaultPackage(input);
        if (model == null) {
            if (similarCommand(input)) {
                throw new InputException(InOutputStrings.WRONG_ARGUMENTS.toString());
            } else {
                throw new InputException(InOutputStrings.NO_MATCHING_COMMAND.toString());
            }
        }
        try {
            command = model.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // This should never happen – when this code is reached the program terminates.
            throw new AssertionError("The commands are implemented incorrectly. This is a bug.");
        }
        command.setArguments(input);
        command.setSession(session);
        return command;
    }

}
