package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.OdometryState;
import frc.robot.commands.drive.RunTrajectory;
import frc.robot.commands.drive.Stop;
import frc.robot.subsystems.Drive;

public class RunTrajectoryOdoReset extends SequentialCommandGroup {
    public RunTrajectoryOdoReset (Drive drive, Trajectory trajectory) {
        super(
           new OdometryState(drive, true),
           new RunTrajectory(drive, trajectory),
           new Stop(drive),
           new OdometryState(drive, false)
        );
    }
}