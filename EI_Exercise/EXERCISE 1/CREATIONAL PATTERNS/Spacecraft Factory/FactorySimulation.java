import java.util.Scanner; // <-- Import MUST be at the top

// Abstract Product
abstract class Spacecraft {
    public abstract void launch();
}

// Concrete Products
class Shuttle extends Spacecraft {
    @Override
    public void launch() { System.out.println("Shuttle launched to low Earth orbit!"); }
}

class Probe extends Spacecraft {
    @Override
    public void launch() { System.out.println("Probe sent to deep space!"); }
}

class Satellite extends Spacecraft {
    @Override
    public void launch() { System.out.println("Satellite deployed into orbit!"); }
}

// Factory Class
class SpacecraftFactory {
    /**
     * Factory Method to create and return the requested Spacecraft object.
     */
    public static Spacecraft createSpacecraft(String type) {
        // The switch statement looks correct and uses modern Java syntax for return
        switch(type.toLowerCase()) {
            case "shuttle": return new Shuttle();
            case "probe": return new Probe();
            case "satellite": return new Satellite();
            default: throw new IllegalArgumentException("Unknown spacecraft type: " + type);
        }
    }
}

// Simulation (Public class must match file name)
public class FactorySimulation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Spacecraft Factory Simulation (Factory Method Pattern) ===");
        System.out.println("Available types: Shuttle, Probe, Satellite");

        while(true) {
            System.out.print("Enter spacecraft type to create (or 'exit'): ");
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("exit")) break;

            try {
                // The factory handles the complex object creation logic
                Spacecraft craft = SpacecraftFactory.createSpacecraft(input);
                craft.launch();
            } catch(IllegalArgumentException e) {
                // Catch the specific exception thrown by the factory
                System.out.println("Error: " + e.getMessage());
            } catch(Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        System.out.println("Simulation ended.");
        sc.close();
    }
}
