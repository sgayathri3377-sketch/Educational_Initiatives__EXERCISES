import java.util.Scanner;

// Strategy interface
interface Propulsion { void move(); }

// Concrete Strategies
class ChemicalEngine implements Propulsion {
    public void move() { System.out.println("[Chemical Engine] Accelerating with thrusters."); }
}
class IonEngine implements Propulsion {
    public void move() { System.out.println("[Ion Engine] Steady travel with ion propulsion."); }
}
class WarpDrive implements Propulsion {
    public void move() { System.out.println("[Warp Drive] Engaging faster-than-light travel!"); }
}

// Context
class Spacecraft {
    private Propulsion propulsion;

    public void setPropulsion(Propulsion p) {
        this.propulsion = p;
        System.out.println("[Mission Control] Propulsion switched to " + p.getClass().getSimpleName());
    }

    public void travel() {
        if (propulsion == null) System.out.println("[Error] No propulsion selected!");
        else propulsion.move();
    }
}

// Simulation
public class StrategySimulation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Spacecraft ship = new Spacecraft();

        System.out.println("=== Spacecraft Propulsion Simulation ===");
        System.out.println("Type 'exit' to stop.");

        while (true) {
            System.out.println("Choose propulsion: 1-Chemical, 2-Ion, 3-Warp");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            switch(input) {
                case "1": ship.setPropulsion(new ChemicalEngine()); break;
                case "2": ship.setPropulsion(new IonEngine()); break;
                case "3": ship.setPropulsion(new WarpDrive()); break;
                default: System.out.println("Invalid choice!"); continue;
            }

            ship.travel();
            System.out.println();
        }

        System.out.println("Simulation ended.");
        sc.close();
    }
}
