package rocketsim.command;

import rocketsim.core.RocketLaunchSimulator;
import rocketsim.exception.MissionControlException;
import rocketsim.util.Logger;

/**
 * Concrete Command for fast-forwarding the simulation.
 * FINAL VERSION: Executes a blocking loop and relies on the simulator
 * to only display the final result.
 */
public class FastForwardCommand implements Command {
    private final int seconds;

    public FastForwardCommand(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void execute(RocketLaunchSimulator simulator) throws MissionControlException {
        Logger.getInstance().log("COMMAND", String.format("fast_forward %d received.", seconds));
        
        if (simulator.getRocket().getCurrentStage() == 0) {
            throw new MissionControlException("Cannot fast_forward before launch.");
        }
        if (seconds <= 0) {
            throw new MissionControlException("Fast forward duration must be a positive number.");
        }

        // This single call will run the simulation loop for 'seconds' steps.
        // The simulator itself is responsible for only showing the final status.
        simulator.advanceSimulation(seconds);
    }
}
