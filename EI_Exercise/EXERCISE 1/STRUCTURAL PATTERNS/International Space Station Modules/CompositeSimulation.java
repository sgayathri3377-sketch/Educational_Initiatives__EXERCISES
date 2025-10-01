import java.util.ArrayList;
import java.util.List;

/**
 * Component Interface: Defines the common operations for both simple and complex objects.
 */
interface SpaceStationComponent {
    // FIX: Changed signature to take NO arguments, assuming status() is a reporter method.
    String status(); 
}

/**
 * Leaf: Represents simple objects (individual modules).
 */
class SolarPanelModule implements SpaceStationComponent {
    private String name;

    public SolarPanelModule(String name) {
        this.name = name;
    }

    @Override
    public String status() {
        return " - Module " + name + " is fully operational.";
    }
}

/**
 * Composite: Represents complex objects (groups of modules or the whole station).
 * Contains Leaf objects (modules) and other Composite objects (groups).
 */
class CompositeModule implements SpaceStationComponent {
    private List<SpaceStationComponent> children = new ArrayList<>();
    private String name;

    public CompositeModule(String name) {
        this.name = name;
    }

    public void add(SpaceStationComponent component) {
        children.add(component);
    }

    public void remove(SpaceStationComponent component) {
        children.remove(component);
    }

    @Override
    public String status() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[Group: ").append(name).append(" Status Report]\n");

        for (SpaceStationComponent component : children) {
            // This is the call that was causing the error, 
            // now fixed to call status() with no arguments.
            sb.append(component.status()).append("\n");
        }
        return sb.toString();
    }
}


// Simulation
public class CompositeSimulation {
    public static void main(String[] args) {
        // --- 1. Create Leafs (Individual Modules) ---
        SolarPanelModule panelA = new SolarPanelModule("Solar Panel Array A");
        SolarPanelModule panelB = new SolarPanelModule("Solar Panel Array B");

        // --- 2. Create Composites (Groups) ---
        CompositeModule powerSystem = new CompositeModule("Power System");
        CompositeModule crewQuarters = new CompositeModule("Crew Quarters");
        CompositeModule ISS = new CompositeModule("International Space Station");

        // --- 3. Build the structure (Tree) ---
        powerSystem.add(panelA);
        powerSystem.add(panelB);
        
        ISS.add(powerSystem);
        // Add a simple module directly to the main composite
        ISS.add(crewQuarters); 

        // --- 4. Operate on the structure ---
        // Calling status() on the composite runs the operation recursively on all components.
        System.out.println("--- Global ISS Status Check ---");
        System.out.println(ISS.status());
    }
}
