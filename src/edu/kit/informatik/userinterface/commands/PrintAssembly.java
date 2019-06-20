package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.ReadWrite;
import edu.kit.informatik.logic.LogicException;
import edu.kit.informatik.logic.UnknownAssemblyException;
import edu.kit.informatik.userinterface.InOutputStrings;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to output the (direct) parts/assemblies a given assembly is made of.
 * It deals with user in- and output.
 * Call {@link this#setArguments(String)} before calling {@link this#execute()}.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class PrintAssembly extends Command {
    /**
     * The syntax of the command as a regex. (Not part of the command interface but handy to have
     * in a command.)
     */
    private static final Pattern PATTERN = Pattern.compile(String.format("printAssembly%s(?<name>%s)",
            InOutputStrings.COMMAND_SEPARATOR,
            InOutputStrings.NAME_PATTERN.toString()));

    private String name;

    /**
     * Avoid initialisation outside of the package.
     */
    PrintAssembly() {
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
            Map<String, Integer> nodes = management.printAssembly(name);
            outputStringIntegerMap(nodes, false);
        } catch (UnknownAssemblyException e) {
            if (e.nonExisting()) {
                ReadWrite.writeError(e.getMessage());
            } else {
                ReadWrite.writeLine(InOutputStrings.SINGLE_PART.toString());
            }
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
