package edu.kit.informatik.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * An assembly or rather the BOM that describes the assembly. This class contains methods to alter a given assembly and
 * with its constructor it is possible to create new assemblies. This class can be looked at as a tree of height 1.
 * Which itself will be used in a larger tree structure that is passed to instances of this object at construction.
 *
 * @author Liam Wachter
 * @version 1.0
 */
// I decided to use as less different names for the same thing as possible. Since I think it really gets confusing
// when the terms "bill of material" and "assembly" are mixed, I chose to only use the word assembly.

// The Javadoc of methods does not stick to the convention, because of a bug in the checkstyle, described here:
// https://ilias.studium.kit.edu/goto.php?target=frm_851721_119871_371461#371461
class Assembly {

    private static final int MIN_AMOUNT = 1;
    private static final int MAX_AMOUNT = 1000;
    private final String name;
    private final Register register;
    private final Map<String, Integer> children;

    /**
     * Create an assembly.
     *
     * @param name         the name of this assembly.
     * @param childrenList the name of the assemblies/parts this assembly consists of.
     * @param register     the mapping of all assembly names to their objects.
     * @throws LogicException if this assembly would be in a state illegal state. Especially it will throw a
     *                        {@link CycleException}, if creating this assembly would lead to a cycle in the product
     *                        structure.
     */
    Assembly(String name, Map<String, Integer> childrenList, Register register) throws LogicException {
        this.name = name;
        this.register = register;
        children = childrenList;
        if (children.values().stream().anyMatch(this::outOfBounds)) {
            throw new LogicException(ErrorMessages.BOUNDS.toString());
        }
        // checks if an assembly with this name already exits, if it does it must be a part.
        Assembly previous = register.get(name);
        if (previous != null) {
            throw new LogicException(ErrorMessages.DUPLICATE.toString());
        }
        CycleResult cycleResult = getFirstCycle(children);
        if (cycleResult.isCycle()) {
            throw new CycleException(ErrorMessages.CYCLE.toString(), cycleResult.getIllegalBranch());
        }
        register.put(name, this);
    }

    private boolean outOfBounds(int amount) {
        return amount < MIN_AMOUNT || amount > MAX_AMOUNT;
    }

    // One point of view could be that the cycle detection should be in the Register class. But from an object
    // oriented point of view those methods concern only instances of assembly. In each step it is one single
    // assembly that, if created or altered, creates a cycle. So I decided to place the cycle detection here.

    /**
     * Returns the first cycle for a given configuration.
     *
     * @param testChildes the new set of childes of this assembly.
     * @return if this configuration would create a cycle. If so the first illegal branch will be included.
     */
    private CycleResult getFirstCycle(Map<String, Integer> testChildes) {
        for (String nodeName : testChildes.keySet()) {
            CycleResult cycleResult = new CycleResult();
            if (testChildes.keySet().contains(name)) {
                cycleResult.append(name);
                cycleResult.append(nodeName);
                cycleResult.detectCycle();
                return cycleResult;
            }
            Assembly assembly = register.get(nodeName);
            if (assembly != null) {
                assembly.references(name, cycleResult);
                if (cycleResult.isCycle()) {
                    cycleResult.append(nodeName);
                    cycleResult.prepend(nodeName);
                    return cycleResult;
                }
            }
        }
        return new CycleResult();
    }

    /**
     * This method goes through a tree and determines if the identifier is used in a way that would create a cycle.
     * It is a private recursion helper method, to make the usage of {@link this#getFirstCycle(Map)} more intuitive.
     *
     * @param identifier  the name of the assembly/part to search for.
     * @param cycleResult when calling this from outside, there should be no cycle detected yet.
     * @return {@see CycleResult}
     */
    private CycleResult references(String identifier, CycleResult cycleResult) {
        if (children.keySet().contains(identifier)) {
            // The End of the recursion decent is reached.
            cycleResult.detectCycle();
            cycleResult.append(identifier);
            return cycleResult;
        }

        for (String nodeName : children.keySet()) {
            Assembly assembly = register.get(nodeName);
            if (assembly != null && assembly.references(identifier, cycleResult).isCycle()) {
                // During the recursion ascent the "stack trace" of the illegal branch is added (in reverse order).
                cycleResult.append(nodeName);
                return cycleResult;
            }
        }
        return cycleResult;
    }

    /**
     * Add a assembly to this assembly.
     *
     * @param partName the name of the assembly to add.
     * @param amount   the amount that should be added.
     * @throws LogicException if there would be more than 1000 assemblies of one kind directly in this assembly. In
     *                        the case that adding this parts would create a cycle a {@link CycleException} will be
     *                        thrown.
     */
    void addPart(String partName, int amount) throws LogicException {
        if (amount < 1) {
            throw new LogicException(ErrorMessages.NOT_POSITIVE.toString());
        }

        // for cycle checks increasing the amount does not make a difference.
        Map<String, Integer> newNodes = new HashMap<>(children);
        newNodes.put(partName, amount);
        CycleResult cycleResult = getFirstCycle(newNodes);
        if (cycleResult.isCycle()) {
            throw new CycleException(ErrorMessages.CYCLE.toString(), cycleResult.getIllegalBranch());
        }

        try {
            // using Map#merge with a merge function that simply adds the values, if there keys in both maps. To avoid a
            // sum grater than the maximum value the merge function throws a runtime exception and does not change
            // anything.
            children.merge(partName, amount, (oldValue, toAdd) -> {
                int sum = oldValue + toAdd;
                if (outOfBounds(sum)) {
                    throw new IllegalStateException(ErrorMessages.BOUNDS.toString());
                }
                return sum;
            });
        } catch (IllegalStateException e) {
            // to not pass a runtime exception through the program and eventually forget about it, the
            // IllegalStateException is converted to a LogicException (not a runtime exception).
            throw new LogicException(e.getMessage());
        }
    }

    /**
     * Decrease the amount of certain assemblies in this assembly.
     *
     * @param partName the name of the assembly, of which the amount should be decreased.
     * @param amount   a positive number that will be subtracted from the current amount of this assembly.
     * @throws LogicException if decreasing the amount would leave the assembly with a node that has a
     *                        negative amount. A {@link UnknownAssemblyException} will be thrown, if this assembly
     *                        does not have a node with name in <code>partName</code> provided name.
     */
    void removePart(String partName, int amount) throws LogicException {
        if (amount < 1) {
            throw new LogicException(ErrorMessages.NOT_POSITIVE.toString());
        }
        if (!children.containsKey(partName)) {
            throw new UnknownAssemblyException(false);
        }
        int newAmount = children.get(partName) - amount;
        if (newAmount == 0) {
            // fully remove
            children.remove(partName);
            if (children.isEmpty()) {
                register.remove(name);
            }
            // Checks if the removed part was an assembly used somewhere else.
            Assembly assembly = register.get(partName);
            if (assembly != null && register.hasPart(name)) {
                // It is not in use anymore, so it can be removed.
                register.remove(partName);
            }
        } else {
            if (outOfBounds(newAmount)) {
                throw new LogicException(ErrorMessages.BOUNDS.toString());
            }
            // decrease the amount
            children.put(partName, newAmount);
        }
    }

    /**
     * Obtain all direct children of a assembly.
     *
     * @return the names of the parts/assemblies this assembly is made off.
     */
    Map<String, Integer> getParts() {
        return children;
    }
}
