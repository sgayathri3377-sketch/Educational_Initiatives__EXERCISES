package rocketsim.core;

/**
 * A dedicated background thread that can be paused and resumed.
 * FINAL VERSION: With pause/resume logic.
 */
public class SimulationThread extends Thread {
    private final RocketLaunchSimulator simulator;
    private final Object lock = new Object();
    private volatile boolean paused = false;
    private volatile boolean running = true;

    public SimulationThread(RocketLaunchSimulator simulator) {
        this.simulator = simulator;
        setDaemon(true); 
    }

    public void pauseSimulation() {
        paused = true;
    }

    public void resumeSimulation() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll(); // Wake up the thread if it's waiting.
        }
    }
    
    public void stopSimulation() {
        running = false;
        resumeSimulation(); // Ensure it's not stuck in a wait() call.
    }

    @Override
    public void run() {
        while (running && simulator.isMissionActive()) {
            synchronized (lock) {
                if (paused) {
                    try {
                        lock.wait(); // Wait until resumeSimulation is called.
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            if (!running) break;

            try {
                Thread.sleep(1000);
                // We still need to check if we were paused during the sleep.
                if (!paused) {
                    simulator.advanceSimulation(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

