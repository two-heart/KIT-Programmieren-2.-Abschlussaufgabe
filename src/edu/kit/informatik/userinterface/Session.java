package edu.kit.informatik.userinterface;

import edu.kit.informatik.ReadWrite;
import edu.kit.informatik.logic.MaterialManagement;
import edu.kit.informatik.userinterface.commands.Command;
import edu.kit.informatik.userinterface.commands.CommandFactory;

/**
 * Handling of user input and output. This is also the point where the exception handling happens.
 * A session can be started and stopped.
 *
 * @author Liam Wachter
 * @version 1.0
 */
public class Session {

    private boolean running = true;
    private MaterialManagement materialManagement;

    /**
     * After starting the session this method remains in a loop until the {@link this#terminate()} method is called.
     */
    void run() {
        materialManagement = new MaterialManagement();
        CommandFactory factory = new CommandFactory(this);
        while (running) {
            String input = ReadWrite.readLine();
            try {
                Command command = factory.getCommand(input);
                command.execute();
            } catch (InputException e) {
                ReadWrite.writeError(e.getMessage());
            }
        }
    }

    /**
     * Ends the session.
     */
    public void terminate() {
        running = false;
    }

    /**
     * Get the {@link MaterialManagement} this session is working on.
     *
     * @return the current material management that the user "interacts with".
     */
    public MaterialManagement getMaterialManagement() {
        return materialManagement;
    }
}
