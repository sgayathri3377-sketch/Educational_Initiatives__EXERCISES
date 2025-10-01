package rocketsim.core;

import java.util.ArrayList; 
import java.util.List;
import rocketsim.director.MissionDirector;
import rocketsim.exception.ProfileLoadException;
import rocketsim.model.Rocket;
import rocketsim.state.PreLaunch;
import rocketsim.state.RocketState;
import rocketsim.util.Logger;

/**
 * The Context class, managing the state and the main simulation loop.
 * FINAL VERSION: Corrected redundant logging.
 */
public class RocketLaunchSimulator {
    private Rocket rocket;
    private RocketState currentState;
    private boolean checksComplete;
    
    private final List<MissionStatusObserver> observers = new ArrayList<>();

    public RocketLaunchSimulator() {
        try {
            MissionDirector director = new MissionDirector();
            this.rocket = director.buildRocket("leo");
        } catch (ProfileLoadException e) {
            throw new IllegalStateException("Failed to build rocket from profile: " + e.getMessage(), e);
        }
        
        this.currentState = new PreLaunch();
        this.checksComplete = false;
        Logger.getInstance().log("INIT", "Simulator and LEO Rocket Model initialized via MissionDirector.");
    }

    public void setState(RocketState newState) {
        this.currentState = newState;
    }

    public void initiatePreLaunchChecks() {
        Logger.getInstance().log("COMMAND", "Initiating Pre-Launch Checks...");
        if (currentState instanceof PreLaunch) {
            currentState.executeLogic(this);
        } else {
            Logger.getInstance().log("WARNING", "Pre-Launch checks can only be initiated in the Pre-Launch state.");
        }
    }

    public synchronized void advanceSimulation(int seconds) {
        for (int i = 0; i < seconds && rocket.isMissionActive(); i++) {
            currentState.executeLogic(this);

            if (seconds == 1 || (i == seconds - 1)) { 
                if (rocket.isMissionActive()) {
                    notifyObservers();
                }
            }
        }
    }

    public void handleMissionFailure(String reason) {
        if (!rocket.isMissionActive()) return;
        
        notifyObservers(); 
        
        rocket.setMissionActive(false);
        Logger.getInstance().log("FAILURE", "MISSION FAILED: " + reason);
        notifyObservers(String.format("!!! MISSION FAILED !!!\nReason: %s", reason));
        
        notifyObservers();
    }
    
    public void setInactive() {
        if (!rocket.isMissionActive()) return;

        notifyObservers();
        
        rocket.setMissionActive(false);

        notifyObservers();
    }

    public void addObserver(MissionStatusObserver observer) {
        observers.add(observer);
    }

    /**
     * This method is used by states to send one-off event messages to the console.
     * CORRECTED: No longer creates a redundant log entry.
     */
    public void postCustomMessage(String message) {
        // The internal logic (e.g., in AscentStage1) is responsible for logging.
        // This method's only job is to update the user view.
        notifyObservers(message);
    }

    private void notifyObservers() {
        String status = getCurrentStatusString();
        for (MissionStatusObserver observer : observers) {
            observer.updateStatus(status);
        }
    }
    
    private void notifyObservers(String finalMessage) {
        for (MissionStatusObserver observer : observers) {
            observer.updateStatus(finalMessage);
        }
    }

    private String getCurrentStatusString() {
        if (!rocket.isMissionActive()) {
            boolean hasReachedAltitude = rocket.getAltitudeKm() >= rocket.getMaxAltitudeKm();
            boolean hasReachedSpeed = rocket.getSpeedKmh() >= rocket.getMaxOrbitalSpeedKmh();
            
            return String.format("--- MISSION %s ---", (hasReachedAltitude && hasReachedSpeed) ? "SUCCESSFUL" : "FAILED");
        }
        
        return String.format(
            "Stage: %d, Fuel: %.1f%%, Altitude: %.1f km, Speed: %.0f km/h",
            rocket.getCurrentStage(),
            rocket.getFuelPercent(),
            rocket.getAltitudeKm(),
            rocket.getSpeedKmh()
        );
    }

    public Rocket getRocket() { return rocket; }
    
    public String getStageName() {
        return currentState.getStageName();
    }
    
    public boolean isChecksComplete() { return checksComplete; }
    public void setChecksComplete(boolean checksComplete) { this.checksComplete = checksComplete; }
    public boolean isMissionActive() { return rocket.isMissionActive(); }
    
    public interface MissionStatusObserver {
        void updateStatus(String status);
    }
}