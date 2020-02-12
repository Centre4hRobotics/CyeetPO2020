/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANIDs;
import frc.robot.Constants.FeederConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Feeder extends SubsystemBase
{ 

  private TalonSRX feederMotor;
  private DigitalInput unknownSensor;

  public Feeder() {
    feederMotor = new TalonSRX(CANIDs.kFeederCAN);
    unknownSensor = new DigitalInput(FeederConstants.kSensorDIO);
  }

  public void setSpeed (double speed)
  {
      feederMotor.set(ControlMode.Velocity, speed);
  }

  public boolean sensorTriggered () {
    //returns true if sensor is triggered, false if it is clear
    return unknownSensor.get();
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
