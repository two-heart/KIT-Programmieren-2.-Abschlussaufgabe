package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.ReadWrite;
import edu.kit.informatik.logic.LogicException;
import edu.kit.informatik.userinterface.InOutputStrings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to fully remove an assembly.
 * It deals with user in- and output.
 * Call {@link this#setArguments(String)} before calling {@link this#execute()}.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class RemoveAssembly extends Command {
    private static final Pattern PATTERN = Pattern.compile(String.format("removeAssembly%s(?<name>%s)",
            InOutputStrings.COMMAND_SEPARATOR,
            InOutputStrings.NAME_PATTERN));
    private String name;

    /**
     * Avoid initialisation outside of the package.
     */
    RemoveAssembly() {
    }

    /**
     * Obtain a regex pattern for this command.
     *
     * @return a pattern that, can be used to decide if this is the right command for a given
     * user input.
     */
    static Pattern getDefaultPattern() {
        return PATTERN;
    }

    @Override
    public void execute() {
        try {
            management.removeAssembly(name);
            ReadWrite.writeLine(InOutputStrings.POSITIVE);
        } catch (LogicException e) {
            ReadWrite.writeError(e.getMessage());
        }
    }

    @Override
    void setArguments(String argument) {
        Matcher matcher = PATTERN.matcher(argument);
        if ((!matcher.matches())) {
            throw new AssertionError("This is a bug. Method was called without prior matching");
        }
        name = matcher.group("name");
    }
}
