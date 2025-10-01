package rocketsim.command;

import rocketsim.core.RocketLaunchSimulator;
import rocketsim.exception.MissionControlException;

/**
 * Interface for the Command Pattern.
 * All user actions are encapsulated as Command objects.
 */
public interface Command {
    /**
     * Executes the specific action on the simulator.
     * @param simulator The receiver object.
     * @throws MissionControlException For semantic errors (e.g., launching before checks).
     */
    void execute(RocketLaunchSimulator simulator) throws MissionControlException;
}
