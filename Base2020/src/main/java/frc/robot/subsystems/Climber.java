/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants.CANIDs; 
//import frc.robot.Constants.ClimberConstants;
import frc.robot.Pneumatics; 
import edu.wpi.first.wpilibj.DoubleSolenoid;


public class Climber extends SubsystemBase {
  /**
   * Creates a new ExampleSubsystem.
   */

  private TalonSRX climberL;
  private TalonSRX climberR;
  private Pneumatics p;

  public Climber(Pneumatics pneumatics) {
    climberL = new TalonSRX(CANIDs.kClimberLCAN);
    climberR = new TalonSRX(CANIDs.kClimberRCAN);
    p = pneumatics;
  }
  
  public void setRSpeed(double speed)
  {
    climberR.set(ControlMode.PercentOutput, speed);
  }  

  public void setLSpeed(double speed)
  {
    climberL.set(ControlMode.PercentOutput, speed);
  }  

  public void extend()
  {
  p.setClimbState(DoubleSolenoid.Value.kForward);
  }

  public void retract()
  {
  p.setClimbState(DoubleSolenoid.Value.kReverse);
  }

  public void pistonOff()
  {
  p.setClimbState(DoubleSolenoid.Value.kOff);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
