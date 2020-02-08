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
import frc.robot.Constants.ClimberConstants; 

public class Climber extends SubsystemBase {
  /**
   * Creates a new ExampleSubsystem.
   */

  private TalonSRX climberL;
  private TalonSRX climberR;

  public Climber() {
    climberL = new TalonSRX(ClimberConstants.kClimberLCAN);
    climberR = new TalonSRX(ClimberConstants.kClimberRCAN);

  }
  
  public void setRSpeed(double speed)
  {
    climberR.set(speed);
  }  

  public void setLSpeed(double speed)
  {
    climberL.set(speed);
  }  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
