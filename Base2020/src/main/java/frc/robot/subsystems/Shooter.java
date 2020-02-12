/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Pneumatics;
import frc.robot.Constants.CANIDs;
import frc.robot.Constants.ShooterConstants;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Shooter extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private CANSparkMax motor1, motor2;
  private CANPIDController shooterPID;
  private CANEncoder encoder1, encoder2;
  private ShuffleboardTab tab;
  private ArrayList<NetworkTableEntry> entries;
  private Pneumatics p;
  private double vel;

  public Shooter (Pneumatics pcm) {
    this.p = pcm;
    motor1 = new CANSparkMax(CANIDs.kSM1CAN, MotorType.kBrushless);
    motor2 = new CANSparkMax(CANIDs.kSM2CAN, MotorType.kBrushless);

    /*shooterPID = motor1.getPIDController();
    shooterPID.setP(ShooterConstants.kP);
    shooterPID.setI(ShooterConstants.kI);
    shooterPID.setD(ShooterConstants.kD);
    shooterPID.setIZone(ShooterConstants.kIz);
    shooterPID.setFF(ShooterConstants.kFF);
    shooterPID.setOutputRange(ShooterConstants.kMinOutput, ShooterConstants.kMaxOutput);
    vel = 0;*/

    encoder1 = motor1.getEncoder();
    encoder2 = motor2.getEncoder();
    motor1.setInverted(false);
    motor2.setInverted(false);

    tab = Shuffleboard.getTab("Shooter");
    entries = new ArrayList<NetworkTableEntry> (4);
    entries.add(tab.add("PosEncoder1", 0).getEntry());
    entries.add(tab.add("PosEncoder2", 0).getEntry());
    entries.add(tab.add("VelEncoder1", 0).getEntry());
    entries.add(tab.add("VelEncoder2", 0).getEntry());
  }

  @Override
  public void periodic () {
    updateShuffleboard();
  }

  private void updateShuffleboard () {
    entries.get(0).setNumber(getEncoderPosition(0));
    entries.get(1).setNumber(getEncoderPosition(1));
    entries.get(2).setNumber(getEncoderVelocity(0));
    entries.get(3).setNumber(getEncoderVelocity(1));
  }

  public void extend() {
    p.setShootState(true);
  }
  public void retract() {
    p.setShootState(false);
  }

  public double getEncoderPosition (int encoder) {
    if (encoder == 0) {
      return encoder1.getPosition();
    } else if (encoder == 1) {
      return encoder2.getPosition();
    }
    return 0;
  }

  public double getEncoderVelocity (int encoder) {
    if (encoder == 0) {
      return encoder1.getVelocity();
    } else if (encoder == 1) {
      return encoder2.getVelocity();
    }
    return 0;
  }

  //Can make it so setting individual motors also happens, but seems dangerous
  public void setSpeed (/*int motor, */double speed) {
    motor1.set(-1*speed);
    motor2.set(speed);
  }

  /*public void setVelocity (double velocity) {
    shooterPID.setReference(velocity, ControlType.kVelocity);
    vel = velocity;
  }

  public double getVelocity() {
    return vel;
  }*/
}
