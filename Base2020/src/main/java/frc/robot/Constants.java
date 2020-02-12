/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.util.Color;

public final class Constants {
  public static final class DriveConstants {
    public static final boolean testMode = true;

    //Drive controller constants
    public static final boolean squareSpeedInput = false;
    public static final boolean squareRotInput = true;
    public static final double maxArcadeOutput = 1.0;

    //Encoders reversed
    public static final boolean kLeftEncoderReversed = false;
    public static final boolean kRightEncoderReversed = true;

    //Drive characteristics
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

  public static final class ClimberConstants {}

  public static final class FeederConstants {
    public static final int kSensorDIO = 1;   //We don't know what this is yet
  }

  public static final class IntakeConstants {}

  public static final class SpinnerConstants {
    //Target Colors on spinner
    public static final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    public static final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    public static final Color kRedTarget = ColorMatch.makeColor(0.535, 0.337, 0.128);
    public static final Color kYellowTarget = ColorMatch.makeColor(0.321, 0.553, 0.125);
  }

  public static final class ShooterConstants {}

  public static final class CANIDs {
    //Drive Motor SparkMAX CAN IDs (L/R, Master/Follower)
    public static final int kRMCAN = 3;
    public static final int kRFCAN = 4;
    public static final int kLMCAN = 1;
    public static final int kLFCAN = 2;

    //k Shooter Motor (1/2) CAN IDs SparkMAX
    public static final int kSM1CAN = 5;
    public static final int kSM2CAN = 6;

    //Intake TalonSRX CAN ID
    public static final int kIntakeCAN = 11;

    //Indexer TalonSRX CAN ID
    public static final int kFeederCAN = 12;

    //Climber TalonSRX CAN IDs (L/R)
    public static final int kClimberLCAN = 13;
    public static final int kClimberRCAN = 14;

    //Spinner TalonSRX CANID
    public static final int kSpinCAN = 15;
  }

  public static final class PneumaticConstants {
    //Climb DoubleSolenoid channels
    public static final int[] kClimbChannels = {0,1};

    //Spinner Solenoid channel
    public static final int kSpinChannel = 2;

    //Shooter Solenoid channel
    public static final int kShootChannel = 3;

    //Intake Solenoid channel
    public static final int kIntakeChannel = 4;
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final int[] kFunctionControllerPorts = {1,2};
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 1.5;
    public static final double kMaxAccelerationMetersPerSecondSquared = 1.5;

    // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
  }
}
