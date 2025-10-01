package rocketsim.state;

import rocketsim.core.RocketLaunchSimulator;

/**
 * Interface for the State Pattern. Defines the behavior for different mission phases.
 * (Pre-Launch, Stage 1, Stage 2).
 */
public interface RocketState {
    /**
     * Executes the mission logic for one second, handles stage separation, success, or failure.
     * @param simulator The context object (RocketLaunchSimulator).
     */
    void executeLogic(RocketLaunchSimulator simulator);
    
    /**
     * Performs failure checks specific to the current state.
     * @param simulator The context object (RocketLaunchSimulator).
     * @return True if a catastrophic failure occurred, false otherwise.
     */
    boolean checkFailure(RocketLaunchSimulator simulator);

    /**
     * Returns the name of the current stage for display.
     */
    String getStageName();
}

