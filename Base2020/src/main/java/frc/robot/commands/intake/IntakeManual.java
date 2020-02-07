/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Robot;
import frc.robot.Constants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

/**
 * An example command.  You can replace me with your own command.
 */
public class IntakeManual extends CommandBase {

  private Intake intake;
  private XboxController xbc;
  private double speed;

  public IntakeManual (Intake intakeUsed, XboxController controller, double speed) {
    intake = intakeUsed;
    xbc = controller;
    this.speed = speed;
    addRequirements(intake);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() 
  {
    double speed = xbc.getTriggerAxis(Hand.kRight)*(xbc.getBumper(Hand.kRight) ? -1:1);
    intake.setSpeed(Math.abs(speed) < 0.1 ? 0:speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
      intake.setSpeed(0.0);
  }

}
