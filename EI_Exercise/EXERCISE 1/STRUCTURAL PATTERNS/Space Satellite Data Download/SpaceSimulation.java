// Target interface (Ground station expects this)
interface GroundStation {
    void downloadData(String format);
}

// Adaptee (NASA Satellite with different interface)
class NasaSatellite {
    public void fetchTelemetry(String protocol) {
        System.out.println("[NASA Satellite] Sending telemetry via: " + protocol);
    }
}

// Adapter (converts GroundStation request → Satellite protocol)
class SatelliteAdapter implements GroundStation {
    private NasaSatellite satellite;

    public SatelliteAdapter(NasaSatellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void downloadData(String format) {
        // Convert format request into protocol
        String protocol = "[Converted to Protocol] " + format;
        satellite.fetchTelemetry(protocol);
    }
}

// Client Simulation
public class SpaceSimulation {
    public static void main(String[] args) {
        // Ground station wants "JSON" data
        GroundStation station = new SatelliteAdapter(new NasaSatellite());

        // Adapter makes it compatible with NASA satellite
        station.downloadData("JSON");
    }
}


