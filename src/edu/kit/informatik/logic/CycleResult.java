package edu.kit.informatik.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * The return value of the cycle detection.
 *
 * @author Liam Wachter
 * @version 1.0
 */
class CycleResult {
    private boolean isCycle;
    private LinkedList<String> callChain = new LinkedList<>();

    /**
     * Add the name of a node to the call chain.
     *
     * @param name name of the node to add.
     */
    void append(String name) {
        callChain.add(name);
    }

    /**
     * Add the name of a node at the beginning of the call chain.
     *
     * @param name name of the node to add.
     */
    void prepend(String name) {
        callChain.addFirst(name);
    }

    /**
     * Call this method in the moment a cycle is detected.
     */
    void detectCycle() {
        isCycle = true;
    }

    /**
     * Get the cycle.
     *
     * @return the illegal branch in the format, that problematic nodes are at beginning and the end of the list.
     */
    List<String> getIllegalBranch() {
        // There are multiple representation conceivable but I think this one makes the structure of the cycle the
        // clearest.
        List<String> toReturn = new LinkedList<>(callChain);
        Collections.reverse(toReturn);
        return new LinkedList<>(toReturn);
    }

    /**
     * Check if there was a cycle.
     *
     * @return whether a cycle was detected.
     */
    boolean isCycle() {
        return isCycle;
    }
}
