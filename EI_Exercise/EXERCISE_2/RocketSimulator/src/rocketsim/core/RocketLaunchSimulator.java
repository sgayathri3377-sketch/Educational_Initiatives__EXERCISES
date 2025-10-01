package rocketsim.core;

import java.util.ArrayList;
import java.util.List;
import rocketsim.director.MissionDirector;
import rocketsim.exception.ProfileLoadException;
import rocketsim.model.Rocket;
import rocketsim.state.PreLaunch;
import rocketsim.state.RocketState;
import rocketsim.util.Logger;

public class RocketLaunchSimulator {
    private Rocket rocket;
    private RocketState currentState;
    private boolean checksComplete;

    // Track last failure reason and if failure reported
    private String lastFailureReason = null;
    private boolean missionFailureReported = false;

    // Track last status sent to avoid duplicate observer calls
    private String lastStatusSent = "";

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
        this.missionFailureReported = false;
        this.lastStatusSent = "";
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
        for (int i = 0; i < seconds; i++) {
            if (!rocket.isMissionActive()) break;

            currentState.executeLogic(this);
            notifyObservers();
        }
    }

    public void handleMissionFailure(String reason) {
        if (missionFailureReported) return;

        missionFailureReported = true;
        lastFailureReason = reason;
        rocket.setMissionActive(false);

        Logger.getInstance().log("FAILURE", "MISSION FAILED: " + reason);
        // Do not notify observers here; advanceSimulation already calls it.
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

    public void postCustomMessage(String message) {
        notifyObservers(message);
    }

    private void notifyObservers() {
        String status = getCurrentStatusString();
        if (!status.equals(lastStatusSent)) {
            lastStatusSent = status;
            for (MissionStatusObserver observer : observers) {
                observer.updateStatus(status);
            }
        }
    }

    private void notifyObservers(String finalMessage) {
        if (!finalMessage.equals(lastStatusSent)) {
            lastStatusSent = finalMessage;
            for (MissionStatusObserver observer : observers) {
                observer.updateStatus(finalMessage);
            }
        }
    }

    private String getCurrentStatusString() {
        if (!rocket.isMissionActive()) {
            boolean hasReachedAltitude = rocket.getAltitudeKm() >= rocket.getMaxAltitudeKm();
            boolean hasReachedSpeed = rocket.getSpeedKmh() >= rocket.getMaxOrbitalSpeedKmh();

            if (hasReachedAltitude && hasReachedSpeed) {
                return "--- MISSION SUCCESSFUL ---";
            }
            String failureMsg = "--- MISSION FAILED ---";
            if (lastFailureReason != null && !lastFailureReason.isEmpty()) {
                failureMsg += "\nReason: " + lastFailureReason;
            }
            return failureMsg;
        }
        return String.format("Stage: %d, Fuel: %.1f%%, Altitude: %.1f km, Speed: %.0f km/h",
                rocket.getCurrentStage(), rocket.getFuelPercent(), rocket.getAltitudeKm(), rocket.getSpeedKmh());
    }

    public Rocket getRocket() { return rocket; }
    public String getStageName() { return currentState.getStageName(); }
    public boolean isChecksComplete() { return checksComplete; }
    public void setChecksComplete(boolean checksComplete) { this.checksComplete = checksComplete; }
    public boolean isMissionActive() { return rocket.isMissionActive(); }

    public interface MissionStatusObserver {
        void updateStatus(String status);
    }
}
