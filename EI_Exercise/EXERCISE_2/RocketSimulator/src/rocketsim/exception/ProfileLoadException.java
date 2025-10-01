package rocketsim.exception;

/**
 * Custom exception for errors that occur while loading a rocket profile.
 */
public class ProfileLoadException extends Exception {
    public ProfileLoadException(String message) {
        super(message);
    }

    public ProfileLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
