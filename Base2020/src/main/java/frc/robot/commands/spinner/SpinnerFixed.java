/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.spinner;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Spinner;

/**
 * An example command.  You can replace me with your own command.
 */
public class SpinnerFixed extends CommandBase {
  private Spinner spinner;
  private double speed;
  public SpinnerFixed(Spinner spinner, double vel) {
      this.spinner = spinner;
      speed = vel;
    // Use requires() here to declare subsystem dependencies
    addRequirements(spinner);

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    spinner.setSpeed(speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    spinner.setSpeed(0);
  }
}