package rocketsim.builder;

import rocketsim.model.Rocket;

/**
 * Implements the Builder pattern for the Rocket object.
 * FINAL VERSION: Includes validation in the build() method.
 */
public class RocketBuilder {
    private double initialMassKg;
    private double fuelMassKg;
    private double stage1DryMassKg;
    private double stage1ThrustN;
    private double stage2ThrustN;
    private double burnRateKgS;
    private double stage2BurnRateKgS;
    private double maxOrbitalSpeedKmh;
    private double maxAltitudeKm;
    private double stageSeparationAltitudeM;

    public RocketBuilder setInitialMassKg(double initialMassKg) {
        this.initialMassKg = initialMassKg;
        return this;
    }

    public RocketBuilder setFuelMassKg(double fuelMassKg) {
        this.fuelMassKg = fuelMassKg;
        return this;
    }

    public RocketBuilder setStage1DryMassKg(double stage1DryMassKg) {
        this.stage1DryMassKg = stage1DryMassKg;
        return this;
    }

    public RocketBuilder setStage1ThrustN(double stage1ThrustN) {
        this.stage1ThrustN = stage1ThrustN;
        return this;
    }

    public RocketBuilder setStage2ThrustN(double stage2ThrustN) {
        this.stage2ThrustN = stage2ThrustN;
        return this;
    }

    public RocketBuilder setBurnRateKgS(double burnRateKgS) {
        this.burnRateKgS = burnRateKgS;
        return this;
    }

    public RocketBuilder setStage2BurnRateKgS(double stage2BurnRateKgS) {
        this.stage2BurnRateKgS = stage2BurnRateKgS;
        return this;
    }

    public RocketBuilder setMaxOrbitalSpeedKmh(double maxOrbitalSpeedKmh) {
        this.maxOrbitalSpeedKmh = maxOrbitalSpeedKmh;
        return this;
    }

    public RocketBuilder setMaxAltitudeKm(double maxAltitudeKm) {
        this.maxAltitudeKm = maxAltitudeKm;
        return this;
    }
    
    public RocketBuilder setStageSeparationAltitudeM(double stageSeparationAltitudeM) {
        this.stageSeparationAltitudeM = stageSeparationAltitudeM;
        return this;
    }

    /**
     * Constructs and returns the final Rocket object after validating parameters.
     * @throws IllegalStateException if any of the required parameters are invalid.
     */
    public Rocket build() {
        // --- ADDED: Validation Logic ---
        if (initialMassKg <= 0) {
            throw new IllegalStateException("Initial mass must be positive.");
        }
        if (fuelMassKg <= 0 || fuelMassKg >= initialMassKg) {
            throw new IllegalStateException("Fuel mass must be positive and less than the initial mass.");
        }
        if (stage1DryMassKg <= 0 || stage1DryMassKg >= initialMassKg) {
            throw new IllegalStateException("Stage 1 dry mass must be realistic.");
        }
        if (stage1ThrustN <= 0 || stage2ThrustN <= 0) {
            throw new IllegalStateException("Thrust for all stages must be positive.");
        }
        if (burnRateKgS <= 0 || stage2BurnRateKgS <= 0) {
            throw new IllegalStateException("Burn rates must be positive.");
        }
        if (maxAltitudeKm <= 0 || maxOrbitalSpeedKmh <= 0) {
            throw new IllegalStateException("Mission targets (altitude and speed) must be positive.");
        }
        // --- End of Validation ---

        return new Rocket(initialMassKg, fuelMassKg, stage1ThrustN, stage2ThrustN, burnRateKgS, 
                          stage2BurnRateKgS, maxOrbitalSpeedKmh, maxAltitudeKm, stage1DryMassKg, stageSeparationAltitudeM);
    }
}

