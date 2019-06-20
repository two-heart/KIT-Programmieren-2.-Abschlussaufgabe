package edu.kit.informatik.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for the communication between front and backend. Instances of this class will return
 * and use cloned objects, so external changes always go via the public methods of this class.
 * <p>
 * If the effect of the method call would create an illegal state of the system or if the an
 * unknown object was requested, methods of this class will throw a {@link LogicException}.
 *
 * @author Liam Wachter
 * @version 1.0
 */
// Note: Strings are not cloned, since they are immutable, so passing the reference is not a problem.
public class MaterialManagement {

    private final Register register = new Register();

    /**
     * Creates a new assembly.
     *
     * @param assemblyName The name of the new assembly.
     * @param nodes        The parts/assemblies this assembly is made with.
     * @throws LogicException If an assembly with this name is already present or if adding this assembly would
     *                        create a cycle.
     */
    public void addAssembly(String assemblyName, Map<String, Integer> nodes) throws LogicException {
        new Assembly(assemblyName, new HashMap<>(nodes), register);
    }

    /**
     * Adds parts/assembly to an existing assembly.
     *
     * @param assemblyName The identifier of the assembly, that should be changed.
     * @param amount       How many should parts should be added. (a positive natural number)
     * @param part         The identifier of the part to add.
     * @throws LogicException If there is no such assembly. If it would create a cycle. If the amount was defined
     *                        incorrectly or the new amount would be greater than 1000.
     */
    public void addPart(String assemblyName, int amount, String part) throws LogicException {
        Assembly assembly = register.get(assemblyName);
        if (assembly == null) {
            throw new UnknownAssemblyException(register.hasPart(assemblyName));
        }
        assembly.addPart(part, amount);
    }

    /**
     * Obtain the amount of different assemblies that are required to build a given assembly.
     *
     * @param name the name of the assembly.
     * @return A mapping between assembly names and their amount or an empty map if the requested assembly is a part.
     * @throws LogicException if there is no such assembly.
     */
    public Map<String, Long> getAssemblies(String name) throws LogicException {
        return new HashMap<>(register.getAssemblies(name));
    }

    /**
     * Obtain the amount of different parts that are required to build a given assembly.
     *
     * @param name the name of the assembly.
     * @return A mapping between part names and their amount.
     * @throws LogicException if there is no such assembly.
     */
    public Map<String, Long> getComponents(String name) throws LogicException {
        return new HashMap<>(register.getComponents(name));
    }

    /**
     * Obtain the parts/assemblies a given assembly directly consists of.
     *
     * @param name the name of the assembly.
     * @return A mapping between part/assembly names and their amount
     * @throws LogicException if there is no such assembly.
     */
    public Map<String, Integer> printAssembly(String name) throws LogicException {
        Assembly assembly = register.get(name);
        if (assembly == null) {
            throw new UnknownAssemblyException(register.hasPart(name));
        }
        return new HashMap<>(assembly.getParts());
    }

    /**
     * Remove a assembly. If the assembly is used somewhere else it will be viewed as a part.
     *
     * @param name the name of the assembly to remove.
     * @throws LogicException if there is no such assembly.
     */
    public void removeAssembly(String name) throws LogicException {
        if (register.remove(name) == null) {
            throw new UnknownAssemblyException(false);
        }
    }

    /**
     * Decrease the amount of parts/assemblies in an given assembly
     *
     * @param name     the name of the assembly to remove from.
     * @param amount   the amount of parts/assemblies to remove.
     * @param partName the name of the part/name to remove.
     * @throws LogicException if there is no such assembly or the operation would create an illegal state of the
     *                        assembly. I.e an negative amount.
     */
    public void removePart(String name, int amount, String partName) throws LogicException {
        Assembly assembly = register.get(name);
        if (assembly == null) {
            throw new UnknownAssemblyException(false);
        }
        assembly.removePart(partName, amount);
    }
}
