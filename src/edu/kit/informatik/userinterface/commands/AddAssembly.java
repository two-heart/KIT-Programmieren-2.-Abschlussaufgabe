package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.ReadWrite;
import edu.kit.informatik.logic.LogicException;
import edu.kit.informatik.userinterface.InOutputStrings;
import edu.kit.informatik.userinterface.InputException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to create a new assembly.
 * It deals with user in- and output.
 * Call {@link this#setArguments(String)} before calling {@link this#execute()}.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class AddAssembly extends Command {

    private static final String SINGLE_DEFINITION = InOutputStrings.NUMBER_PATTERN.toString()
            + InOutputStrings.INNER_SEPARATOR + InOutputStrings.NAME_PATTERN;
    private static final Pattern PATTERN = Pattern.compile(String.format(
            "addAssembly%s(?<name>%s)%s(?<nodes>%s(%s%s)*)",
            InOutputStrings.COMMAND_SEPARATOR,
            InOutputStrings.NAME_PATTERN,
            InOutputStrings.DEFINITION_SEPARATOR,
            SINGLE_DEFINITION,
            InOutputStrings.ARGUMENT_SEPARATOR,
            SINGLE_DEFINITION));
    private String name;
    private Map<String, Integer> nodes;

    /**
     * Avoid initialisation outside of the package.
     */
    AddAssembly() {
    }

    /**
     * Obtain a regex pattern for this command.
     *
     * @return a pattern that, can be used to decide if this is the right command for a given user input.
     */
    static Pattern getDefaultPattern() {
        return PATTERN;
    }

    @Override
    public void execute() {
        try {
            management.addAssembly(name, nodes);
            ReadWrite.writeLine(InOutputStrings.POSITIVE);
        } catch (LogicException e) {
            ReadWrite.writeError(e.getMessage());
        }
    }

    @Override
    void setArguments(String argument) throws InputException {
        Matcher matcher = PATTERN.matcher(argument);
        if ((!matcher.matches())) {
            throw new AssertionError("This is a bug. Method was called without prior matching");
        }
        name = matcher.group("name");
        String stringNodes = matcher.group("nodes");
        String[] nodeParts = stringNodes.split(InOutputStrings.ARGUMENT_SEPARATOR.toString());
        nodes = new HashMap<>();
        for (String nodePart : nodeParts) {
            String[] split = nodePart.split(InOutputStrings.INNER_SEPARATOR.toString());
            // Looking at a one nodePart (that has the form of a SINGLE_DEFINITION) it makes sense that the first
            // part should be interpreted as a number and stands at the position 0 in the array and so the other part
            // at position 1 is the name.
            if (nodes.put(split[1], tryParse(split[0])) != null) {
                throw new InputException(InOutputStrings.DUPLICATE_DEFINITION.toString());
            }
        }
    }
}
