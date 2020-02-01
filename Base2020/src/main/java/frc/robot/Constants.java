/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public final class Constants {
  public static final class DriveConstants {
    public static final boolean testMode = true;

    public static final int kRMCAN = 3;
    public static final int kRFCAN = 4;
    public static final int kLMCAN = 1;
    public static final int kLFCAN = 2;

    public static final boolean squareSpeedInput = false;
    public static final boolean squareRotInput = true;
    public static final double maxArcadeOutput = 1.0;

    /*public static final int kLeftMotor1Port = 0;
    public static final int kLeftMotor2Port = 1;
    public static final int kRightMotor1Port = 2;
    public static final int kRightMotor2Port = 3;

    public static final int[] kLeftEncoderPorts = new int[]{0, 1};
    public static final int[] kRightEncoderPorts = new int[]{2, 3};*/
    public static final boolean kLeftEncoderReversed = false;
    public static final boolean kRightEncoderReversed = true;

    public static final double kTrackwidthMeters = 0.555;
    public static final DifferentialDriveKinematics kDriveKinematics =
        new DifferentialDriveKinematics(kTrackwidthMeters);

    public static final double kGearRatio = 10.71;
    public static final double kWheelDiameterMeters = 0.148;//0.152;
    public static final double kDistancePerRev =
        // Assumes the encoders are directly  mounted on the wheel shafts
        (kWheelDiameterMeters * Math.PI) / (double) kGearRatio;

    public static final boolean kGyroReversed = true;

    // These are example values only - DO NOT USE THESE FOR YOUR OWN ROBOT!
    // These characterization values MUST be determined either experimentally or theoretically
    // for *your* robot's drive.
    // The Robot Characterization Toolsuite provides a convenient tool for obtaining these
    // values for your robot.
    public static final double ksVolts = 0.104;
    public static final double kvVoltSecondsPerMeter = 2.92;
    public static final double kaVoltSecondsSquaredPerMeter = 0.328;

    // Example value only - as above, this must be tuned for your drive!
    public static final double kPDriveVel = 0.0;//0.166;//23.0;
    public static final double kDDriveVel = 0.0;
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 1.5;
    public static final double kMaxAccelerationMetersPerSecondSquared = 1.5;

    // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
  }
}
