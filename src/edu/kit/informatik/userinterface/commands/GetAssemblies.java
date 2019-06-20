package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.ReadWrite;
import edu.kit.informatik.logic.LogicException;
import edu.kit.informatik.userinterface.InOutputStrings;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to output the assemblies (and their amount) used in an other assembly.
 * It deals with user in- and output.
 * Call {@link this#setArguments(String)} before calling {@link this#execute()}.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class GetAssemblies extends Command {
    private static final Pattern PATTERN = Pattern.compile(String.format("getAssemblies%s(?<name>%s)",
            InOutputStrings.COMMAND_SEPARATOR,
            InOutputStrings.NAME_PATTERN.toString()));
    private String name;

    /**
     * Avoid initialisation outside of the package.
     */
    GetAssemblies() {
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
        Map<String, Long> assemblies;
        try {
            assemblies = management.getAssemblies(name);
        } catch (LogicException e) {
            ReadWrite.writeError(e.getMessage());
            return;
        }
        if (assemblies.isEmpty()) {
            ReadWrite.writeLine(InOutputStrings.EMPTY);
            return;
        }
        outputStringLongMap(assemblies, true);
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
