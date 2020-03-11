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
  private CANEncoder encoder;
  private ShuffleboardTab tab;
  private ArrayList<NetworkTableEntry> entries;
  private Pneumatics p;
  private double vel;

  public Shooter (Pneumatics pcm) {
    this.p = pcm;
    motor1 = new CANSparkMax(CANIDs.kSM1CAN, MotorType.kBrushless);
    motor2 = new CANSparkMax(CANIDs.kSM2CAN, MotorType.kBrushless);
    motor1.setInverted(false);
    motor2.follow(motor1, true);

    shooterPID = motor1.getPIDController();
    shooterPID.setP(ShooterConstants.kP);
    shooterPID.setI(ShooterConstants.kI);
    shooterPID.setD(ShooterConstants.kD);
    shooterPID.setIZone(ShooterConstants.kIz);
    shooterPID.setFF(ShooterConstants.kFF);
    shooterPID.setOutputRange(ShooterConstants.kMinOutput, ShooterConstants.kMaxOutput);
    vel = 0;

    encoder = motor1.getEncoder();

    tab = Shuffleboard.getTab("Shooter");
    entries = new ArrayList<NetworkTableEntry> (5);
    entries.add(tab.add("PosEncoder", 0).getEntry());
    entries.add(tab.add("VelEncoder", 0).getEntry());
    entries.add(tab.add("P Gain", ShooterConstants.kP).getEntry());
    entries.add(tab.add("I Gain", ShooterConstants.kI).getEntry());
    entries.add(tab.add("D Gain", ShooterConstants.kD).getEntry());
  }

  @Override
  public void periodic () {
    updateShuffleboard();
  }

  private void updateShuffleboard () {
    entries.get(0).setNumber(getEncoderPosition());
    entries.get(1).setNumber(getEncoderVelocity());
  }

  public void updatePIDGains() {
    shooterPID.setP(entries.get(2).getDouble(ShooterConstants.kP));
    shooterPID.setI(entries.get(3).getDouble(ShooterConstants.kI));
    shooterPID.setP(entries.get(4).getDouble(ShooterConstants.kD));
  }

  public void extend() {
    p.setShootState(true);
  }
  public void retract() {
    p.setShootState(false);
  }

  public void setShootState (boolean state) {
    p.setShootState(state);
  }

  public boolean getShootState () {
    return p.getShootState();
  }

  public double getEncoderPosition () {
    return encoder.getPosition()*-1;
  }

  public double getEncoderVelocity () {
    return encoder.getVelocity()*-1;
  }

  //Can make it so setting individual motors also happens, but seems dangerous
  public void setPercentOutput (/*int motor, */double speed) {
    motor1.set(-1*speed);
  }

  public void setVoltage (double volts) {
    motor1.setVoltage(-1*volts);
  }

  public void setVelocity (double rpm) {
    shooterPID.setReference(-1*rpm, ControlType.kVelocity);
    System.out.println("RPM: " + rpm);
    vel = -1*rpm;
  }

  public double getVelocity() {
    return vel;
  }
}
