package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.ReadWrite;
import edu.kit.informatik.logic.LogicException;
import edu.kit.informatik.userinterface.InOutputStrings;
import edu.kit.informatik.userinterface.InputException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to alter an assembly by removing parts/assemblies of a given amount from it.
 * It deals with user in- and output.
 * Call {@link this#setArguments(String)} before calling {@link this#execute()}.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class RemovePart extends Command {

    private static final Pattern PATTERN = Pattern.compile(
            String.format("removePart%s(?<name>%s)-(?<amount>%s)%s(?<part>%s)",
                    InOutputStrings.COMMAND_SEPARATOR,
                    InOutputStrings.NAME_PATTERN,
                    InOutputStrings.NUMBER_PATTERN,
                    InOutputStrings.INNER_SEPARATOR,
                    InOutputStrings.NAME_PATTERN));
    private String name;
    private String partName;
    private int amount;

    /**
     * Avoid initialisation outside of the package.
     */
    RemovePart() {
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
            management.removePart(name, amount, partName);
            ReadWrite.writeLine(InOutputStrings.POSITIVE.toString());
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
        partName = matcher.group("part");
        amount = tryParse(matcher.group("amount"));
    }
}
