package rocketsim.command;

import rocketsim.core.RocketLaunchSimulator;
import rocketsim.command.Command;

public class ResetCommand implements Command {
    private final rocketsim.Main mainApp;

    public ResetCommand(rocketsim.Main mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void execute(RocketLaunchSimulator simulator) {
        mainApp.resetSimulation();
    }
}
