package rocketsim.model;

import rocketsim.util.Logger;

/**
 * Rocket Model class, representing the physical state and properties of the vehicle.
 * FINAL VERSION: Implements a simplified 1D (vertical) physics model as per the problem statement.
 */
public class Rocket {
    // --- Constants ---
    private static final double GRAVITATIONAL_CONSTANT = 6.67430e-11;
    private static final double EARTH_MASS_KG = 5.972e24;
    private static final double EARTH_RADIUS_M = 6371000;
    private static final double AIR_DENSITY_SEA_LEVEL_KG_M3 = 1.225;
    private static final double DRAG_COEFFICIENT = 0.5;
    private static final double ROCKET_CROSS_SECTIONAL_AREA_M2 = 10.5;
    private static final double SCALE_HEIGHT_M = 8500.0;

    // --- Immutable Properties ---
    private final double dryMassKg;
    private final double fuelMassKg;
    private final double maxOrbitalSpeedKmh;
    private final double maxAltitudeKm;
    private final double stage1ThrustN;
    private final double stage2ThrustN;
    private final double stage1BurnRateKgS;
    private final double stage2BurnRateKgS;
    private final double stage1DryMassKg;
    private final double stageSeparationAltitudeM;

    // --- Mutable Properties ---
    private double currentMassKg;
    private double currentFuelKg;
    private double altitudeM;
    private double speedMs; // This now represents only vertical speed.
    private int currentStage;
    private int timeElapsedSeconds;
    private double currentBurnRateKgS;
    private boolean missionActive;
    private boolean fuelLeakActive;

    public Rocket(double initialMassKg, double fuelMassKg, double stage1ThrustN, double stage2ThrustN,
                  double stage1BurnRateKgS, double stage2BurnRateKgS, double maxOrbitalSpeedKmh, double maxAltitudeKm, 
                  double stage1DryMassKg, double stageSeparationAltitudeM) {
        
        this.dryMassKg = initialMassKg - fuelMassKg;
        this.fuelMassKg = fuelMassKg;
        this.stage1ThrustN = stage1ThrustN;
        this.stage2ThrustN = stage2ThrustN;
        this.stage1BurnRateKgS = stage1BurnRateKgS;
        this.stage2BurnRateKgS = stage2BurnRateKgS;
        this.maxOrbitalSpeedKmh = maxOrbitalSpeedKmh;
        this.maxAltitudeKm = maxAltitudeKm;
        this.stage1DryMassKg = stage1DryMassKg;
        this.stageSeparationAltitudeM = stageSeparationAltitudeM;
        
        this.currentMassKg = initialMassKg;
        this.currentFuelKg = fuelMassKg;
        this.altitudeM = 0.0;
        this.speedMs = 0.0;
        this.currentStage = 0;
        this.currentBurnRateKgS = stage1BurnRateKgS;
        this.missionActive = true;
        this.fuelLeakActive = false;
    }

    public void simulateSecond() {
        if (!missionActive || currentStage == 0) return;

        double fuelToConsume = Math.min(currentFuelKg, currentBurnRateKgS);
        currentFuelKg -= fuelToConsume;
        
        if (currentStage == 1) {
            currentMassKg = this.dryMassKg + this.stage1DryMassKg + currentFuelKg;
        } else {
            currentMassKg = this.dryMassKg + currentFuelKg;
        }

        double distanceFromCenterOfEarth = EARTH_RADIUS_M + altitudeM;
        double gravityAtAltitude = (GRAVITATIONAL_CONSTANT * EARTH_MASS_KG) / (distanceFromCenterOfEarth * distanceFromCenterOfEarth);
        double forceOfGravity = currentMassKg * gravityAtAltitude;

        double currentThrust = (currentStage == 1) ? stage1ThrustN : stage2ThrustN;
        
        double airDensity = AIR_DENSITY_SEA_LEVEL_KG_M3 * Math.exp(-altitudeM / SCALE_HEIGHT_M);
        double dragForce = 0.5 * airDensity * (speedMs * speedMs) * DRAG_COEFFICIENT * ROCKET_CROSS_SECTIONAL_AREA_M2;

        double netForce = currentThrust - forceOfGravity - dragForce;
        
        if (netForce < 0 && altitudeM <= 0) {
            timeElapsedSeconds++;
            return; 
        }

        double acceleration = netForce / currentMassKg;
        
        speedMs += acceleration;
        
        if (altitudeM + speedMs < 0) {
            altitudeM = 0;
            speedMs = 0;
        } else {
            altitudeM += speedMs;
        }

        timeElapsedSeconds++;

        if (currentStage == 1 && altitudeM >= this.stageSeparationAltitudeM) {
            separateStage();
        }

        if (currentFuelKg <= 0) {
            missionActive = false;
        }
    }

    public void separateStage() {
        if (currentStage == 1) {
            Logger.getInstance().log("STAGE_SEP", "Stage 1 complete. Separating stage. Entering Stage 2.");
            currentStage = 2;
            this.currentBurnRateKgS = this.stage2BurnRateKgS;
        }
    }

    // --- Getters and Setters ---
    public double getFuelPercent() { 
        return (fuelMassKg > 0) ? (currentFuelKg / fuelMassKg) * 100.0 : 0.0;
    }
    public double getAltitudeKm() { return altitudeM / 1000.0; }
    public double getSpeedKmh() { return speedMs * 3.6; }
    public int getCurrentStage() { return currentStage; }
    public int getTimeElapsedSeconds() { return timeElapsedSeconds; }
    public boolean isMissionActive() { return missionActive; }
    public double getMaxOrbitalSpeedKmh() { return maxOrbitalSpeedKmh; }
    public double getMaxAltitudeKm() { return maxAltitudeKm; }
    public boolean isFuelLeakActive() { return fuelLeakActive; }

    public void setCurrentStage(int currentStage) { this.currentStage = currentStage; }
    public void setMissionActive(boolean missionActive) { this.missionActive = missionActive; }
    
    public void activateFuelLeak() {
        if (!fuelLeakActive) {
            fuelLeakActive = true;
            currentBurnRateKgS *= 2;
        }
    }
}

