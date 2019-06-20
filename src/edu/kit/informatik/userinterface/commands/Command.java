package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.ReadWrite;
import edu.kit.informatik.logic.MaterialManagement;
import edu.kit.informatik.userinterface.InOutputStrings;
import edu.kit.informatik.userinterface.InputException;
import edu.kit.informatik.userinterface.Session;

import java.util.*;


/**
 * Each command should extend this in order to be created and distributed by the {@link CommandFactory}.
 * It also reduces duplicate code by implementing some useful methods to be used in the commands.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public abstract class Command {

    /**
     * The reference to the logic. To allow commands to alter or query a {@link MaterialManagement}.
     */
    protected MaterialManagement management;

    /**
     * Runs the command.
     */
    public abstract void execute();

    /**
     * This allows the a command to manipulate a {@link Session}. E.g. call {@link Session#terminate()} on
     * it or most commonly get the current {@link MaterialManagement}.
     * For commands that don't have to interact with the session this will be ignored.
     *
     * @param session the session the creating factory was initiated with.
     */
    void setSession(Session session) {
        this.management = session.getMaterialManagement();
    }

    /**
     * Passes valid arguments to the command. If the command does not expect any arguments this will be ignored. This
     * method asserts valid input. I.e checked against the regex of the command.
     *
     * @param argument the arguments the user passed one string.
     * @throws InputException if <b>despite</b> the matching against the pattern the arguments turn out to be
     *                        <b>syntactically</b> incorrect.
     */
    abstract void setArguments(String argument) throws InputException;

    /**
     * Sorts the mapping between Strings and Longs in the given way and outputs the result to the user.
     *
     * @param toOutput     the map that should be printed to the user.
     * @param sortByAmount whether to sort by amount and than name or just by name.
     */
    protected void outputStringLongMap(Map<String, Long> toOutput, boolean sortByAmount) {
        List<Map.Entry<String, Long>> list;
        if (sortByAmount) {
            list = sortByAmountAndName(toOutput);
        } else {
            list = sortByName(toOutput);
        }
        StringBuilder output = new StringBuilder();
        // The following two statements are basically a string.join with an more complex formatting and outputting
        // the resulting String.
        list.forEach(entry -> output.append(String.format("%s%s%s%d",
                InOutputStrings.ARGUMENT_SEPARATOR, entry.getKey(),
                InOutputStrings.INNER_SEPARATOR, entry.getValue())));
        ReadWrite.writeLine(output.toString().replaceFirst(InOutputStrings.ARGUMENT_SEPARATOR.toString(), ""));
    }

    /**
     * Sorts the mapping between Strings and Integers in the given way and outputs the result to the user.
     *
     * @param toOutput     the map that should be printed to the user.
     * @param sortByAmount whether to sort by amount and than name or just by name.
     */
    protected void outputStringIntegerMap(Map<String, Integer> toOutput, boolean sortByAmount) {
        outputStringLongMap(remap(toOutput), sortByAmount);
    }

    /**
     * @param numberAsString a {@link String} that is expected to be an <code>int</code>.
     * @return the value of <code>numberAsString</code>
     * @throws InputException if this is too big for an int.
     */
    protected int tryParse(String numberAsString) throws InputException {
        int number;
        try {
            number = Integer.parseInt(numberAsString);
        } catch (NumberFormatException e) {
            throw new InputException(InOutputStrings.WAY_TO_HUGE.toString());
        }
        return number;
    }

    private Map<String, Long> remap(Map<String, Integer> toSort) {
        Map<String, Long> longMap = new HashMap<>();
        toSort.forEach((key, value) -> longMap.put(key, (long) value));
        return longMap;
    }

    private List<Map.Entry<String, Long>> sortByAmountAndName(Map<String, Long> toSort) {
        List<Map.Entry<String, Long>> toReturn = new LinkedList<>(toSort.entrySet());
        toReturn.sort((left, right) -> {
            if (left.getValue().equals(right.getValue())) {
                // In case the amount is the same. So it will be sorted by name.
                return left.getKey().compareTo(right.getKey());
            }
            return right.getValue().compareTo(left.getValue());
        });
        return toReturn;
    }

    private List<Map.Entry<String, Long>> sortByName(Map<String, Long> toSort) {
        List<Map.Entry<String, Long>> toReturn = new LinkedList<>(toSort.entrySet());
        // The standard string comparision is exactly what is required.
        toReturn.sort(Comparator.comparing(Map.Entry::getKey));
        return toReturn;
    }
}
