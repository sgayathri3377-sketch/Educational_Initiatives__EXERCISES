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

public class Main implements MissionStatusObserver {
    private RocketLaunchSimulator simulator;
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
        System.out.print("\r");
        System.out.println("-> " + status);

        if (status.contains("--- MISSION")) {
            missionEnded = true;
            if (simulationThread != null) {
                simulationThread.stopSimulation();
            }
            // Keep Scanner open for more input or reset
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
            int seconds = Integer.parseInt(ffMatcher.group(1));
            return new FastForwardCommand(seconds);
        } else if (input.equalsIgnoreCase("reset")) {
            return new ResetCommand(this);
        } else if (input.equalsIgnoreCase("exit")) {
            return null;
        } else {
            throw new MissionControlException("Invalid command. Available: start_checks, launch, fast_forward X, reset, exit.");
        }
    }

    public void resetSimulation() {
        missionEnded = false;
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.stopSimulation();
        }
        simulator = new RocketLaunchSimulator();
        simulator.addObserver(this);
        simulationThread = null;
        System.out.println("Simulation reset. You can start a new mission now.");
    }

    public void run() {
        System.out.println("\n--- Rocket Launch Simulator (LEO Profile) ---");
        System.out.println("Type 'start_checks' to begin pre-launch sequence.");
        System.out.println("Type 'launch' to lift off after checks.");
        System.out.println("Type 'fast_forward X' to skip time (e.g., 'fast_forward 10').");
        System.out.println("Type 'reset' to start a new mission after ending.");
        System.out.println("Type 'exit' to quit.");

        while (true) {
            if (missionEnded) {
                System.out.print("\nMission ended. Type 'reset' to start a new mission or 'exit' to quit: ");
            } else if (simulationThread != null && simulationThread.isAlive()) {
                System.out.print("\n--> Press Enter to issue a command...");
            } else {
                System.out.print("\nCommand: ");
            }

            String input;
            try {
                input = scanner.nextLine().trim();
            } catch (Exception e) {
                break;
            }

            if ("exit".equalsIgnoreCase(input)) {
                Logger.getInstance().log("COMMAND", "exit received. Simulation terminated by user.");
                break;
            }

            if (simulationThread != null && simulationThread.isAlive()) {
                simulationThread.pauseSimulation();
                System.out.print("\r(Simulation Paused) Command: " + input);
                if (input.isEmpty()) {
                    input = scanner.nextLine().trim();
                }
            }

            try {
                Command command = parseCommand(input);
                if (command != null) {
                    command.execute(simulator);
                }
                if ("reset".equalsIgnoreCase(input)) {
                    resetSimulation();
                }
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

        System.out.println("\nSimulation terminated.");
        scanner.close();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
