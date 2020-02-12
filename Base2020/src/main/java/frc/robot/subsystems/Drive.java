/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.HashMap;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.CANIDs;

public class Drive extends SubsystemBase {


  enum Encoder {
    LEFT, RIGHT;
  }

  private final CANSparkMax mLeftMaster = new CANSparkMax(CANIDs.kLMCAN, MotorType.kBrushless);
  private final CANSparkMax mLeftSlave = new CANSparkMax(CANIDs.kLFCAN, MotorType.kBrushless);
  private final CANSparkMax mRightMaster = new CANSparkMax(CANIDs.kRMCAN, MotorType.kBrushless);
  private final CANSparkMax mRightSlave = new CANSparkMax(CANIDs.kRFCAN, MotorType.kBrushless);

  


  // The motors on the left side of the drive.
  /*private final SpeedControllerGroup m_leftMotors =
      new SpeedControllerGroup(new PWMVictorSPX(DriveConstants.kLeftMotor1Port),
                               new PWMVictorSPX(DriveConstants.kLeftMotor2Port));

  // The motors on the right side of the drive.
  private final SpeedControllerGroup m_rightMotors =
      new SpeedControllerGroup(new PWMVictorSPX(DriveConstants.kRightMotor1Port),
                               new PWMVictorSPX(DriveConstants.kRightMotor2Port));*/

  // The robot's drive

  private final CANEncoder m_leftEncoder = mLeftMaster.getEncoder();
  private final CANEncoder m_rightEncoder = mRightMaster.getEncoder();
  // The left-side drive encoder
  /*private final Encoder m_leftEncoder =
      new Encoder(DriveConstants.kLeftEncoderPorts[0], DriveConstants.kLeftEncoderPorts[1],
                  DriveConstants.kLeftEncoderReversed);


  // The right-side drive encoder
  private final Encoder m_rightEncoder =
      new Encoder(DriveConstants.kRightEncoderPorts[0], DriveConstants.kRightEncoderPorts[1],
                  DriveConstants.kRightEncoderReversed);*/

  // The gyro sensor
  private final AHRS navx;

  // Odometry class for tracking robot pose
  private final DifferentialDriveOdometry m_odometry;

  private ShuffleboardTab tab;
  private HashMap<String, NetworkTableEntry> entries;

  private final String[] intEntryNames = 
    {"LeftOutput", "RightOutput", "LeftVoltage", "RightVoltage", "LeftEncoder", "RightEncoder",
    "LeftWheelSpeed", "RightWheelSpeed", "NavX Yaw", "LeftInput", "RightInput"};

  /**
   * Creates a new Drive.
   */
  public Drive() {
    // Sets the distance per pulse for the encoders
    mRightSlave.follow(mRightMaster);
    mLeftSlave.follow(mLeftMaster);
    m_leftEncoder.setPositionConversionFactor(DriveConstants.kDistancePerRev);
    m_rightEncoder.setPositionConversionFactor(DriveConstants.kDistancePerRev);
    m_leftEncoder.setVelocityConversionFactor(DriveConstants.kDistancePerRev/60);
    m_rightEncoder.setVelocityConversionFactor(DriveConstants.kDistancePerRev/60);

    mLeftMaster.setInverted(false);
    mRightMaster.setInverted(true);

    navx = new AHRS(Port.kMXP);
    zeroHeading();
    resetEncoders();
    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
    if (DriveConstants.testMode) {
        this.tab = Shuffleboard.getTab("Test");
        initShuffleboardMap();
    }
     
  }

  //make hashmap hold widget instead of entry, get entry when needed
  private void initShuffleboardMap() {
    entries = new HashMap<String, NetworkTableEntry> (12);
    for (int i = 0; i<intEntryNames.length; i++) {
        entries.put(intEntryNames[i], tab.add(intEntryNames[i], 0).getEntry());
    }
    entries.put("Pose", tab.add("Pose", getPose().toString()).getEntry());
  }

  private void updateShuffleboardMap() {
    entries.get("LeftOutput").setNumber(mLeftMaster.getAppliedOutput());
    entries.get("RightOutput").setNumber(mRightMaster.getAppliedOutput());
    entries.get("LeftVoltage").setNumber(mLeftMaster.getBusVoltage());
    entries.get("RightVoltage").setNumber(mRightMaster.getBusVoltage());
    entries.get("LeftEncoder").setNumber(getEncoderPosition(Encoder.LEFT));
    entries.get("RightEncoder").setNumber(getEncoderPosition(Encoder.RIGHT));
    entries.get("Pose").setString(getPose().toString());
    entries.get("RightWheelSpeed").setNumber(getWheelSpeeds().rightMetersPerSecond);
    entries.get("LeftWheelSpeed").setNumber(getWheelSpeeds().leftMetersPerSecond);
    entries.get("NavX Yaw").setNumber(navx.getYaw());
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(Rotation2d.fromDegrees(getHeading()), getEncoderPosition(Encoder.LEFT),
                      getEncoderPosition(Encoder.RIGHT));
    if (DriveConstants.testMode) {
        updateShuffleboardMap();
    }
  }

  /**
   * Allows the robot to drive with two sliding inputs (aka one joystick).
   * 
   * @param xSpeed The desired motion along the robot's axis.
   * @param zRotation The desired rotation about the robot's axis.
   * @param maxOutput The maximum desired output.
   */

  public void arcadeDrive(double xSpeed, double zRotation, double maxOutput) {
        xSpeed = MathUtil.clamp(xSpeed, -1.0, 1.0);
        xSpeed = Math.abs(xSpeed)<0.2 ? 0:xSpeed;

        zRotation = MathUtil.clamp(zRotation, -1.0, 1.0);
        zRotation = Math.abs(zRotation)<0.2 ? 0:zRotation;

        if (DriveConstants.squareRotInput) {
        zRotation = Math.copySign(zRotation * zRotation, zRotation);
        }
        if (DriveConstants.squareSpeedInput) {
          xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
        }

        double leftMotorOutput;
        double rightMotorOutput;

        double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if (xSpeed >= 0.0) {
        // First quadrant, else second quadrant
        if (zRotation >= 0.0) {
            leftMotorOutput = maxInput;
            rightMotorOutput = xSpeed - zRotation;
        } else {
            leftMotorOutput = xSpeed + zRotation;
            rightMotorOutput = maxInput;
        }
        } else {
        // Third quadrant, else fourth quadrant
        if (zRotation >= 0.0) {
            leftMotorOutput = xSpeed + zRotation;
            rightMotorOutput = maxInput;
        } else {
            leftMotorOutput = maxInput;
            rightMotorOutput = xSpeed - zRotation;
        }
        }
        entries.get("LeftInput").setNumber(MathUtil.clamp(leftMotorOutput, -1.0, 1.0) * maxOutput);
        entries.get("RightInput").setNumber(MathUtil.clamp(rightMotorOutput, -1.0, 1.0) * maxOutput);
        mLeftMaster.set(MathUtil.clamp(leftMotorOutput, -1.0, 1.0) * maxOutput);
        mRightMaster.set(MathUtil.clamp(rightMotorOutput, -1.0, 1.0) * maxOutput);
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Returns the current position of a specified encoder.
   * 
   * @param encoder The specified encoder.
   * @return The encoder position.
   */
  public double getEncoderPosition (Encoder encoder) {
    return encoder == Encoder.LEFT ? m_leftEncoder.getPosition():m_rightEncoder.getPosition();
  }

  /**
   * Returns the current velocity of a specified encoder.
   * 
   * @param encoder The specified encoder.
   * @return The encoder velocity.
   */
  public double getEncoderVelocity (Encoder encoder)
  {
    return encoder == Encoder.LEFT ? m_leftEncoder.getVelocity():m_rightEncoder.getVelocity();
  }

  /**
   * Returns the current wheel speeds of the robot.
   *
   * @return The current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(getEncoderVelocity(Encoder.LEFT), getEncoderVelocity(Encoder.RIGHT));
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
  }

  /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts  the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    mLeftMaster.setVoltage(leftVolts);
    mRightMaster.setVoltage(rightVolts);
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    m_leftEncoder.setPosition(0);
    m_rightEncoder.setPosition(0);
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    return (getEncoderPosition(Encoder.LEFT) + getEncoderPosition(Encoder.RIGHT)) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   *
   * @return the left drive encoder
   */
  public CANEncoder getLeftEncoder() {
    return m_leftEncoder;
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public CANEncoder getRightEncoder() {
    return m_rightEncoder;
  }

  /**
   * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   /
  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput);
  }

  /**
   * Zeroes the heading of the robot.
   */
  public void zeroHeading() {
    navx.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from 180 to 180
   */
  public double getHeading() {
    return Math.IEEEremainder(navx.getAngle(), 360) * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return navx.getRate() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
  }
}


