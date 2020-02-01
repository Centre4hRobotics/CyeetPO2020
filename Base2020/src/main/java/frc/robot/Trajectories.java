package frc.robot;

import java.util.List;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.trajectory.*;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.Constants.*;

public final class Trajectories {
    
    private static DifferentialDriveVoltageConstraint autoVoltageConstraint =
    new DifferentialDriveVoltageConstraint(
        new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                   DriveConstants.kvVoltSecondsPerMeter,
                                   DriveConstants.kaVoltSecondsSquaredPerMeter),
        DriveConstants.kDriveKinematics,
        10);

// Create config for trajectory
    private static TrajectoryConfig config =
    new TrajectoryConfig(AutoConstants.kMaxSpeedMetersPerSecond,
                         AutoConstants.kMaxAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(DriveConstants.kDriveKinematics)
        // Apply the voltage constraint
        .addConstraint(autoVoltageConstraint);

    public static Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)),
        List.of(
            new Translation2d(3, -0.5)
        ),
        new Pose2d(6, 0, new Rotation2d(0)),
            // Pass config
        config.setReversed(false)
    );
    
    public static Trajectory straightBackTrajectory = TrajectoryGenerator.generateTrajectory(
        new Pose2d(6, 0, new Rotation2d(0)),
        List.of(
            new Translation2d(3, -0.5)
        ),
        new Pose2d(0, 0, new Rotation2d(0)),
            // Pass config
        config.setReversed(true) 
    );

    public static Trajectory diamond = TrajectoryGenerator.generateTrajectory(
    new Pose2d(0,0,new Rotation2d(0)),
    List.of(
        new Translation2d(1,0.5),
        new Translation2d(2,0),
        new Translation2d(1,-0.5)
    ),
    new Pose2d(0,0,new Rotation2d(90)),
    config.setReversed(false) 
    );
}