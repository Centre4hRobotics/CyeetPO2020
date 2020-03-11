package frc.robot.commands.drive;

import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drive;

public class OdometryState implements Command {
    private Drive dtrain;
    private boolean oState;

    public OdometryState(Drive drivetrain, boolean state) {
        this.dtrain = drivetrain;
        oState = state;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    public void end(boolean interrupted) {
        if (oState) {
            dtrain.enableOdometry();
        } else {
            dtrain.disableOdometry();
        }
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Set.of(dtrain);
    }
}