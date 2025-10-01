/**
 * Singleton Pattern: MissionControl
 * Uses the Initialization-on-demand Holder Idiom for lazy and thread-safe creation.
 */
public class SingletonSimulation {

    public static void main(String[] args) {
        System.out.println("=== Singleton Pattern: Mission Control ===");

        // Get the first instance
        MissionControl mc1 = MissionControl.getInstance();
        System.out.println("MC1 instance created.");

        // Get the second instance
        MissionControl mc2 = MissionControl.getInstance();
        System.out.println("MC2 instance requested.");

        // Update status using mc1
        mc1.updateMissionStatus("Launch Prep");
        
        // Retrieve status using mc2 (should show the update from mc1)
        System.out.println("[Main] MC2 reads status: " + mc2.getMissionStatus());

        // Confirm both references point to the same instance
        System.out.println("\n--- Verification ---");
        boolean isSame = (mc1 == mc2);
        System.out.println("mc1 == mc2? " + isSame);
        System.out.println(isSame ? "Success: Both references point to the SAME Singleton instance." : "Error: Different instances created.");
    }
}

class MissionControl {
    private String missionStatus;

    // 1. Private Constructor prevents direct instantiation
    private MissionControl() {
        missionStatus = "Idle";
        System.out.println("[Mission Control] Instance initialized (Status: Idle).");
    }

    // 2. Private static nested class holds the Singleton instance
    private static class Holder {
        // The instance is created only when getInstance() is called for the first time
        private static final MissionControl INSTANCE = new MissionControl();
    }

    // 3. Public static method provides global access
    public static MissionControl getInstance() {
        return Holder.INSTANCE;
    }

    // Thread-safe update method
    public synchronized void updateMissionStatus(String status) {
        this.missionStatus = status;
        System.out.println("[Mission Control] Mission status updated: " + status);
    }

    // Thread-safe getter
    public synchronized String getMissionStatus() {
        return missionStatus;
    }
}
