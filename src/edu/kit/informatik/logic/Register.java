package edu.kit.informatik.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * The Register is a mapping between assemblies and their name. Looking at the product structure as a tree it
 * contains references to all inner vertexes.
 *
 * @author Liam Wachter
 * @version 1.0
 */
// This class is not only useful because of its methods, but it also improves readability, since when using a map
// directly it wouldn't be as clear as now what this map is used for.
class Register extends HashMap<String, Assembly> {
    /**
     * Check if there is a component named <code>name</code> somewhere.
     *
     * @param name the name of the component to search for
     * @return if somewhere in the product structure a part with the name <code>name</code> is in use.
     */
    boolean hasPart(String name) {
        // this lambda goes through all assemblies (until a matching part was found) and checks if this assembly
        // contains a part with the provided name.
        return values().stream()
                .flatMap(toCheck -> toCheck.getParts().entrySet().stream())
                .anyMatch(child -> child.getKey().equals(name));
    }

    // the following methods are just capsuling the recursion methods, so when using them from outside this
    // class one does not have to know the default weight of 1. So the implementation is hidden as far as possible.

    /**
     * Traverse the three starting with the assembly <code>name</code> to get all assemblies, and the amount of them,
     * "below" the given assembly.
     *
     * @param name the name of the assembly to get the children of
     * @return a mapping between the assemblies and their amount
     * @throws LogicException if there is no such assembly
     */
    Map<String, Long> getAssemblies(String name) throws LogicException {
        // standard weight=1 because this is the start of the recursion where all entries can simply be added together.
        return getAssemblies(name, 1);
    }

    /**
     * Traverse the three starting with the assembly <code>name</code> to get all leaves (representing parts) and the
     * amount of them.
     *
     * @param name the name of the assembly to get the parts of
     * @return a mapping between the assemblies and their amount
     * @throws LogicException if there is no such assembly
     */
    Map<String, Long> getComponents(String name) throws LogicException {
        // standard weight=1 because this is the start of the recursion where all entries can simply be added together.
        return getComponents(name, 1);
    }

    private Map<String, Long> getAssemblies(String name, long weight) throws LogicException {
        Map<String, Long> toReturn = new HashMap<>();
        Assembly assembly = get(name);
        if (assembly == null) {
            throw new UnknownAssemblyException(hasPart(name));
        }
        for (Map.Entry<String, Integer> node : assembly.getParts().entrySet()) {
            if (keySet().contains(node.getKey())) {
                // add direct leaves
                toReturn.merge(node.getKey(), (long) node.getValue() * weight, Long::sum);
                // search for more
                getAssemblies(node.getKey(), weight * node.getValue())
                        .forEach((key, value) -> toReturn.merge(key, value, Long::sum));
            }
        }
        return toReturn;
    }

    private Map<String, Long> getComponents(String name, long weight) throws LogicException {
        Assembly assembly = get(name);
        Map<String, Long> toReturn = new HashMap<>();
        if (assembly == null) {
            throw new UnknownAssemblyException(hasPart(name));
        }
        for (Map.Entry<String, Integer> node : assembly.getParts().entrySet()) {
            if (!containsKey(node.getKey())) {
                // its an part, so it can be added
                toReturn.merge(node.getKey(), (long) node.getValue() * weight, Long::sum);
            } else {
                // go to next step of the recursion and add the result to the list that will be returned.
                getComponents(node.getKey(), node.getValue() * weight)
                        .forEach((key, value) -> toReturn.merge(key, value, Long::sum));
            }
        }
        return toReturn;
    }
}
