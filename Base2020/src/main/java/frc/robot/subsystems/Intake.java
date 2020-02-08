/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Pneumatics;
import frc.robot.Constants.IntakeConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Intake extends SubsystemBase
{ 

  private VictorSPX intakeMotor;
  private Pneumatics p;

  public Intake(Pneumatics pneumatics) {
    this.p = pneumatics;
    intakeMotor = new VictorSPX(IntakeConstants.kIntakeCAN);
  }

  public void extend () {
    p.setIntakeState(true);
  }

  public void retract () {
    p.setIntakeState(false);
  }

  public void setSpeed (double speed)
  {
      intakeMotor.set(ControlMode.Velocity, speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
