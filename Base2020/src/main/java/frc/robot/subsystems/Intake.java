/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Pneumatics;
import frc.robot.Constants.CANIDs;
//import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.IntakeConstants;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake extends SubsystemBase
{ 

  private CANSparkMax intakeMotor;
  private CANPIDController intakePID;
  private Pneumatics p;
  private double vel;

  public Intake(Pneumatics pneumatics) {
    this.p = pneumatics;
    intakeMotor = new CANSparkMax(CANIDs.kIntakeCAN, MotorType.kBrushless);
    intakePID = intakeMotor.getPIDController();

    intakePID.setP(IntakeConstants.kP);
    intakePID.setI(IntakeConstants.kI);
    intakePID.setD(IntakeConstants.kD);
    intakePID.setIZone(IntakeConstants.kIz);
    intakePID.setFF(IntakeConstants.kFF);
    intakePID.setOutputRange(IntakeConstants.kMinOutput, IntakeConstants.kMaxOutput);
    vel = 0;
  }

  public void extend () {
    p.setIntakeState(true);
  }

  public void retract () {
    p.setIntakeState(false);
  }

  public void setPercentOutput (double speed)
  {
    if (!p.getIntakeState()) {
      intakeMotor.set(0);
      return;
    }
    //Positive is intake, negative is outtake
      intakeMotor.set(-1*speed);
  }

  public void setVelocity(double vel) {
    this.vel = vel;
    intakePID.setReference(vel, ControlType.kVelocity);
  }

  public double getVelocity () {
    return vel;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
