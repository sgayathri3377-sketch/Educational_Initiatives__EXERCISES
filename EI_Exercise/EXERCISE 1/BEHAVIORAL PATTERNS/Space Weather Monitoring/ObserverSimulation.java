import java.util.*;

// Subject
class SpaceWeatherStation {
    private List<Module> modules = new ArrayList<>();
    private String solarFlare;

    public void subscribe(Module module) { modules.add(module); }
    public void unsubscribe(Module module) { modules.remove(module); }

    public void setSolarFlare(String flare) {
        this.solarFlare = flare;
        System.out.println("\n[Mission Control] Solar Flare Detected: " + flare);
        notifyModules();
    }

    private void notifyModules() {
        for (Module m : modules) m.update(solarFlare);
    }
}

// Observer interface
interface Module { void update(String flare); }

// Concrete Observers
class NavigationModule implements Module {
    public void update(String flare) {
        System.out.println("[Navigation] Adjusting trajectory for " + flare);
    }
}

class CommunicationModule implements Module {
    public void update(String flare) {
        System.out.println("[Communication] Shielding comms for " + flare);
    }
}

class ShieldModule implements Module {
    public void update(String flare) {
        System.out.println("[Shield] Activating shields for " + flare);
    }
}

// Simulation
public class ObserverSimulation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SpaceWeatherStation station = new SpaceWeatherStation();

        // Subscribe modules
        station.subscribe(new NavigationModule());
        station.subscribe(new CommunicationModule());
        station.subscribe(new ShieldModule());

        System.out.println("=== Space Weather Monitoring ===");
        System.out.println("Type 'exit' to stop.");

        while (true) {
            System.out.print("Enter solar flare intensity (Low/Medium/High): ");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("exit")) break;
            station.setSolarFlare(input);
        }

        System.out.println("Simulation ended.");
        sc.close();
    }
}
