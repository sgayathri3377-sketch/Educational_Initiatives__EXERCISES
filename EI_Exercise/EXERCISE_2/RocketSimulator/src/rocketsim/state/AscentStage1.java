package rocketsim.state;

import java.util.Random;
import rocketsim.core.RocketLaunchSimulator;
import rocketsim.model.Rocket;

/**
 * Concrete State: Handles the ascent in Stage 1.
 * Failure Strategy: Engine Flameout (0.1% chance per second).
 */
public class AscentStage1 implements RocketState {
    private final Random random = new Random();

    @Override
    public void executeLogic(RocketLaunchSimulator simulator) {
        Rocket rocket = simulator.getRocket();

        rocket.simulateSecond();

        if (checkFailure(simulator)) return;

        // Check if the rocket model has initiated stage separation
        if (rocket.getCurrentStage() == 2) { 
            // Post the stage separation message to the user console
            simulator.postCustomMessage("Stage 1 complete. Separating stage. Entering Stage 2.");
            simulator.setState(new AscentStage2());
            return;
        }

        // Universal failure check: Rocket became non-operational (e.g., out of fuel)
        if (!rocket.isMissionActive()) {
            simulator.handleMissionFailure("Stage 1 Mission Failed: Rocket became non-operational.");
        }
    }

    @Override
    public boolean checkFailure(RocketLaunchSimulator simulator) {
        // Stage 1 Failure Strategy: Engine Flameout (0.1% chance every second)
        if (random.nextDouble() < 0.001) { // 0.1% chance
            simulator.handleMissionFailure("Catastrophic Engine Flameout in Stage 1.");
            return true;
        }
        return false;
    }

    @Override
    public String getStageName() {
        return "Stage 1";
    }
}

