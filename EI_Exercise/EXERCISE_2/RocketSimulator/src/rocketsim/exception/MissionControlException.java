package rocketsim.exception;

/**
 * Custom exception for semantic errors within the simulation state.
 * E.g., trying to 'launch' before 'start_checks'.
 */
public class MissionControlException extends Exception {
    public MissionControlException(String message) {
        super(message);
    }
}
