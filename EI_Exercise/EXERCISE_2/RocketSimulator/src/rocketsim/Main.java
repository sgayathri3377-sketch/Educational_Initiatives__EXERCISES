package rocketsim;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rocketsim.command.*;
import rocketsim.core.RocketLaunchSimulator;
import rocketsim.core.RocketLaunchSimulator.MissionStatusObserver;
import rocketsim.core.SimulationThread;
import rocketsim.exception.MissionControlException;
import rocketsim.util.Logger;

/**
 * Main application class acting as the UserClient and CommandProcessor.
 * FINAL VERSION: With corrected welcome message formatting.
 */
public class Main implements MissionStatusObserver {
    private final RocketLaunchSimulator simulator;
    private final Scanner scanner;
    private volatile boolean missionEnded = false;
    private SimulationThread simulationThread;

    public Main() {
        this.simulator = new RocketLaunchSimulator();
        this.simulator.addObserver(this);
        this.scanner = new Scanner(System.in);
    }

    public void setSimulationThread(SimulationThread thread) {
        this.simulationThread = thread;
    }

    @Override
    public void updateStatus(String status) {
        // Use carriage return to clear the line where the prompt is waiting.
        System.out.print("\r"); 
        System.out.println("-> " + status);
        
        if (status.contains("--- MISSION")) {
            missionEnded = true;
            if (simulationThread != null) {
                simulationThread.stopSimulation();
            }
            // By closing the scanner, we interrupt the blocking scanner.nextLine() call
            // in the main thread, allowing the program to terminate automatically.
            scanner.close();
        }
    }

    private Command parseCommand(String input) throws MissionControlException {
        Pattern ffPattern = Pattern.compile("fast_forward\\s+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher ffMatcher = ffPattern.matcher(input);

        if (input.equalsIgnoreCase("start_checks")) {
            return new StartChecksCommand();
        } else if (input.equalsIgnoreCase("launch")) {
            return new LaunchCommand(this);
        } else if (ffMatcher.matches()) {
            try {
                int seconds = Integer.parseInt(ffMatcher.group(1));
                return new FastForwardCommand(seconds);
            } catch (NumberFormatException e) {
                throw new MissionControlException("Invalid number of seconds for fast_forward.");
            }
        } else {
            throw new MissionControlException("Invalid command. Available: start_checks, launch, fast_forward X, exit.");
        }
    }

    public void run() {
        // CORRECTED: Added the missing commands to the welcome message.
        System.out.println("\n--- Rocket Launch Simulator (LEO Profile) ---");
        System.out.println("Type 'start_checks' to begin pre-launch sequence.");
        System.out.println("Type 'launch' to lift off after checks.");
        System.out.println("Type 'fast_forward X' to skip time (e.g., 'fast_forward 10').");
        System.out.println("Type 'exit' to quit.");
        
        while (true) {
            if (missionEnded) {
                break; // Exit if the mission has ended from a previous loop iteration.
            } else if (simulationThread != null && simulationThread.isAlive()) {
                System.out.print("\n--> Press Enter to issue a command...");
            } else {
                System.out.print("\nCommand: ");
            }

            String input;
            try {
                input = scanner.nextLine().trim();
            } catch (Exception e) {
                // This exception is expected when the scanner is closed by the updateStatus method.
                // This is our signal to break the loop and terminate.
                break;
            }

            if (simulationThread != null && simulationThread.isAlive()) {
                simulationThread.pauseSimulation();
                System.out.print("\r(Simulation Paused) Command: " + input); 
                if (input.isEmpty()) {
                    input = scanner.nextLine().trim();
                }
            }

            if (input.equalsIgnoreCase("exit")) {
                Logger.getInstance().log("COMMAND", "exit received. Simulation terminated by user.");
                break; // Exit the main loop
            }

            try {
                Command command = parseCommand(input);
                command.execute(simulator);
            } catch (MissionControlException e) {
                System.err.println("\n!!! Mission Control Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\n!!! A critical error occurred: " + e.getMessage());
            } finally {
                if (simulationThread != null && simulationThread.isAlive()) {
                    simulationThread.resumeSimulation();
                }
            }
        }
        
        if (missionEnded) {
            System.out.println("\n--- MISSION ENDED ---");
            System.out.println("Check 'mission_log.txt' for the final log.");
        }
        System.out.println("\nSimulation terminated.");
    }

    public static void main(String[] args) {
        new Main().run();
    }
}