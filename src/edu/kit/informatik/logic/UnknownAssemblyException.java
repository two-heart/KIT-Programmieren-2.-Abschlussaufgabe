package edu.kit.informatik.logic;

/**
 * Signals that there is no such assembly.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class UnknownAssemblyException extends LogicException {

    private boolean isPart;

    /**
     * It is recommended to use the other constructor {@link this#UnknownAssemblyException(boolean)}.
     *
     * @param message what went wrong?
     */
    UnknownAssemblyException(String message) {
        super(message);
    }

    /**
     * This will automatically chose the right exception message depending on <code>isPart</code>.
     *
     * @param isPart <code>false</code> if there is no such item at all.
     *               <code>true</code> if there was an assembly expected but a part name was provided.
     */
    UnknownAssemblyException(boolean isPart) {
        super(isPart ? ErrorMessages.IS_COMPONENT.toString() : ErrorMessages.NONEXISTENT.toString());
        this.isPart = isPart;
    }

    /**
     * Check if the requested item is non existing or is it just of the wrong type.
     *
     * @return <code>true</code> if there is no such item at all. <code>false</code> if there was an assembly
     * expected but a part name was provided.
     */
    public boolean nonExisting() {
        return !isPart;
    }
}
