package rocketsim.director;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import rocketsim.builder.RocketBuilder;
import rocketsim.exception.ProfileLoadException;
import rocketsim.model.Rocket;

/**
 * The Mission Director class is now responsible for loading rocket profiles
 * from .properties files and using the RocketBuilder to construct the vehicle.
 */
public class MissionDirector {

    /**
     * Builds a rocket based on the specified profile name.
     * @param profileName The name of the profile to load (e.g., "leo").
     * @return A fully configured Rocket object.
     * @throws ProfileLoadException if the profile file cannot be found, read, or is invalid.
     */
    public Rocket buildRocket(String profileName) throws ProfileLoadException {
        Properties props = loadProfile(profileName);
        RocketBuilder builder = new RocketBuilder();

        try {
            return builder
                .setInitialMassKg(getDouble(props, "initialMassKg"))
                .setFuelMassKg(getDouble(props, "fuelMassKg"))
                .setStage1DryMassKg(getDouble(props, "stage1DryMassKg"))
                .setStage1ThrustN(getDouble(props, "stage1ThrustN"))
                .setStage2ThrustN(getDouble(props, "stage2ThrustN"))
                .setBurnRateKgS(getDouble(props, "stage1BurnRateKgS"))
                .setStage2BurnRateKgS(getDouble(props, "stage2BurnRateKgS"))
                .setMaxAltitudeKm(getDouble(props, "maxAltitudeKm"))
                .setMaxOrbitalSpeedKmh(getDouble(props, "maxSpeedKmh"))
                .setStageSeparationAltitudeM(getDouble(props, "stageSeparationAltitudeM"))
                .build();
        } catch (NullPointerException | NumberFormatException e) {
            // This catches errors if a key is missing or is not a valid number.
            throw new ProfileLoadException("Profile file '" + profileName + ".properties' is corrupted or missing a key.", e);
        }
    }

    /**
     * Loads a .properties file from the classpath.
     */
    private Properties loadProfile(String profileName) throws ProfileLoadException {
        String fileName = profileName + ".properties";
        Properties props = new Properties();
        
        // try-with-resources ensures the InputStream is closed automatically.
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new ProfileLoadException("Cannot find profile file: " + fileName);
            }
            props.load(input);
        } catch (IOException e) {
            throw new ProfileLoadException("Error reading profile file: " + fileName, e);
        }
        return props;
    }

    /**
     * Helper method to parse a property as a double.
     */
    private double getDouble(Properties props, String key) {
        return Double.parseDouble(props.getProperty(key));
    }
}

