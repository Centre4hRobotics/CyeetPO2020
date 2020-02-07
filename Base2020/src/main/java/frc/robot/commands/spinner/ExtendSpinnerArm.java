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
public class ExtendSpinnerArm extends CommandBase {
  private Spinner spinner;
  public ExtendSpinnerArm(Spinner spinner) {
      this.spinner = spinner;
    // Use requires() here to declare subsystem dependencies
    addRequirements(spinner);

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    spinner.extend();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return true;
  }
}