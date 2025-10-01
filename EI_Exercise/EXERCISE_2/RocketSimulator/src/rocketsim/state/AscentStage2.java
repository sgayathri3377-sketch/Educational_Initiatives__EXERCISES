package rocketsim.state;

import java.util.Random;
import rocketsim.core.RocketLaunchSimulator;
import rocketsim.model.Rocket;
import rocketsim.util.Logger;

/**
 * Concrete State: Handles the ascent and orbital insertion in Stage 2.
 * Failure Strategy: Fuel Leak (0.5% chance per second).
 */
public class AscentStage2 implements RocketState {
    private final Random random = new Random();

    @Override
    public void executeLogic(RocketLaunchSimulator simulator) {
        Rocket rocket = simulator.getRocket();
        
        rocket.simulateSecond();

        checkFailure(simulator); // Fuel leak is non-fatal here, just increases burn rate

        // Check for Mission Success
        boolean isSuccess = rocket.getAltitudeKm() >= rocket.getMaxAltitudeKm() &&
                            rocket.getSpeedKmh() >= rocket.getMaxOrbitalSpeedKmh();

        if (isSuccess) {
            Logger.getInstance().log("SUCCESS", "Orbit achieved! Mission Successful.");
            simulator.setInactive();
            return;
        }

        // Universal failure check: Rocket became non-operational (e.g., out of fuel)
        if (!rocket.isMissionActive()) {
            simulator.handleMissionFailure("Stage 2 Mission Failed: Rocket became non-operational.");
        }
    }

    @Override
    public boolean checkFailure(RocketLaunchSimulator simulator) {
        Rocket rocket = simulator.getRocket();
        
        // --- Failure Strategy: Fuel Leak ---
        if (random.nextDouble() < 0.005 && !rocket.isFuelLeakActive()) { // 0.5% chance
            rocket.activateFuelLeak();
            // CORRECTED: Post the warning message to the user console.
            simulator.postCustomMessage("WARNING: Fuel Leak Detected! Fuel consumption rate has increased.");
        }
        
        // This failure is non-catastrophic, so we always return false.
        return false;
    }

    @Override
    public String getStageName() {
        return "Stage 2";
    }
}