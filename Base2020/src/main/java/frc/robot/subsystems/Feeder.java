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
  private DigitalInput unknownSensor1, unknownSensor2;

  public Feeder() {
    feederMotor = new TalonSRX(CANIDs.kFeederCAN);
    /*unknownSensor1 = new DigitalInput(FeederConstants.kSensor1DIO);
    unknownSensor2 = new DigitalInput(FeederConstants.kSensor2DIO);*/
  }

  public void setSpeed (double speed)
  {
    //Currently backwards!!! Fixed.
      feederMotor.set(ControlMode.PercentOutput, -1*speed);
  }

  public boolean sensorTriggered (int sensor) {
    //returns true if sensor is triggered, false if it is clear
    /*if(sensor==1)
      return unknownSensor1.get();
    if(sensor==2)
      return unknownSensor2.get();*/
    return false;
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
