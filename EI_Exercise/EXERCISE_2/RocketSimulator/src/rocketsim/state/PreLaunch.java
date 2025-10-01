package rocketsim.state;

import java.util.Random;
import rocketsim.core.RocketLaunchSimulator;

/**
 * Concrete State: Handles the pre-launch phase and system checks.
 * Failure Strategy: System Malfunction (0.5% chance).
 */
public class PreLaunch implements RocketState {
    private final Random random = new Random();

    @Override
    public void executeLogic(RocketLaunchSimulator simulator) {
        // This method is called by the 'start_checks' command.
        if (checkFailure(simulator)) {
            // Failure is handled in checkFailure, which calls simulator.handleMissionFailure
        } else {
            // On success, post the message to the user console via the simulator.
            simulator.postCustomMessage("All systems are 'Go' for launch.");
            simulator.setChecksComplete(true);
        }
    }

    @Override
    public boolean checkFailure(RocketLaunchSimulator simulator) {
        // Pre-Launch Failure Strategy: 0.6% chance of system malfunction
        if (random.nextDouble() < 0.006) { // 0.6% chance
            simulator.handleMissionFailure("System Malfunction detected during pre-launch checks.");
            return true;
        }
        return false;
    }

    @Override
    public String getStageName() {
        return "Pre-Launch";
    }
}

