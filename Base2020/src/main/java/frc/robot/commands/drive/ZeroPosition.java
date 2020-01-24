package frc.robot.commands.drive;

import java.util.Set;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drive;

public class ZeroPosition implements Command {
    private Drive dtrain;

    public ZeroPosition(Drive drivetrain) {
        this.dtrain = drivetrain;
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return true;
    }

    public void end(boolean interrupted) {
        dtrain.zeroHeading();
        dtrain.resetOdometry(new Pose2d());
    }

    @Override
    public Set<Subsystem> getRequirements() {
        // TODO Auto-generated method stub
        return Set.of(dtrain);
    }
}