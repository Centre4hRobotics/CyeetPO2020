/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * An example command.  You can replace me with your own command.
 */
public class ShooterExtend extends CommandBase {
  private Shooter shooter;
  public ShooterExtend(Shooter shooter) {
      this.shooter = shooter;
      addRequirements(shooter);

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() 
  {
    shooter.extend();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return true;
  }
}