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
public class ShooterVolts extends CommandBase {
    private double v;
    private Shooter shooter;
  public ShooterVolts(Shooter shoot, double volts) {
      shooter = shoot;
      this.v = volts;
    // Use requires() here to declare subsystem dependencies
    addRequirements(shoot);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() 
  {
    shooter.setVoltage(v);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
      shooter.setPercentOutput(0);
  }
}
