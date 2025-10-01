package rocketsim.command;

import rocketsim.core.RocketLaunchSimulator;
import rocketsim.exception.MissionControlException;
import rocketsim.util.Logger;

/**
 * Concrete Command for initiating pre-launch checks.
 */
public class StartChecksCommand implements Command {
    @Override
    public void execute(RocketLaunchSimulator simulator) throws MissionControlException {
        Logger.getInstance().log("COMMAND", "start_checks received.");

        if (simulator.isChecksComplete()) {
            throw new MissionControlException("Checks already completed. Type 'launch' to proceed.");
        }
        
        // CORRECTED: The command now calls the public method on the simulator,
        // which is responsible for handling the internal state logic.
        simulator.initiatePreLaunchChecks();
    }
}
