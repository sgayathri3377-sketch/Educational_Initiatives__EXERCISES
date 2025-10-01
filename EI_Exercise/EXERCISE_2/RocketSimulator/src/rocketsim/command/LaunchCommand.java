package rocketsim.command;

import rocketsim.Main;
import rocketsim.core.RocketLaunchSimulator;
import rocketsim.core.SimulationThread;
import rocketsim.exception.MissionControlException;
import rocketsim.model.Rocket;
import rocketsim.state.AscentStage1;
import rocketsim.util.Logger;

/**
 * Concrete Command for launching the rocket.
 * FINAL VERSION: Passes the simulation thread back to the Main client.
 */
public class LaunchCommand implements Command {
    private final Main client;

    public LaunchCommand(Main client) {
        this.client = client;
    }

    @Override
    public void execute(RocketLaunchSimulator simulator) throws MissionControlException {
        Logger.getInstance().log("COMMAND", "launch received.");

        if (!simulator.isChecksComplete()) {
            throw new MissionControlException("Pre-launch checks not complete. Type 'start_checks' first.");
        }
        if (simulator.getRocket().getCurrentStage() != 0) {
            throw new MissionControlException("Launch already in progress or completed.");
        }

        Rocket rocket = simulator.getRocket();
        rocket.setCurrentStage(1);
        simulator.setState(new AscentStage1());
        Logger.getInstance().log("STATUS", "Launch initiated. Entering Stage 1.");
        simulator.postCustomMessage("Launch initiated. T-minus zero!");

        SimulationThread simulationThread = new SimulationThread(simulator);
        simulationThread.start();
        
        // Give the main client a reference to the thread so it can be paused/resumed.
        client.setSimulationThread(simulationThread);
    }
}

