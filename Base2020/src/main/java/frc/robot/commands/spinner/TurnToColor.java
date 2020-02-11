/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.spinner;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Spinner;

/**
 * An example command.  You can replace me with your own command.
 */
public class TurnToColor extends CommandBase {
    private double spinSpeed;
    private Spinner spinner;

  public TurnToColor(Spinner spinner, double spin) {
    this.spinner = spinner;
    this.spinSpeed = spin;
    addRequirements(spinner);
    }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() 
  {
    // Color Finding Code

<<<<<<< HEAD

=======
>>>>>>> 5a8a49782e07cfe44cfa03394de92b84455c3bdb
    if (spinner.getCurrentColor().equals(spinner.getColorWanted()))
    {
      Timer.delay(0.05);
      end(false);
    }
    else {
      spinner.setSpeed(spinSpeed);
    }

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    spinner.setSpeed(0);
  }
}