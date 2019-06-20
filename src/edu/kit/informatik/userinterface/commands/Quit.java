package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.userinterface.Session;

import java.util.regex.Pattern;

/**
 * Command to terminate the program.
 * It indirectly deals with user input.
 * Call {@link this#setArguments(String)} before calling {@link this#execute()}.
 *
 * @author Liam Wachter
 * @version 1.0
 */
class Quit extends Command {

    /**
     * The syntax of the command as a regex. (Not part of the command interface but handy to have
     * in a command.)
     */
    private static final Pattern PATTERN = Pattern.compile("quit");
    private Session session;


    /**
     * Package private to avoid direct initialisation without using the {@link CommandFactory}.
     */
    Quit() {
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
        assert (session != null);
        session.terminate();
    }

    // at this point a command really needs the session
    @Override
    void setSession(final Session session) {
        this.session = session;
    }

    @Override
    void setArguments(String argument) {
    }

}
