/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

/**
 * An example command.  You can replace me with your own command.
 */
public class TurnToAngle extends CommandBase {

    private Drive drivetrain;
    private double targetAngle;
  public TurnToAngle(Drive drive, double angle) {
    // Use requires() here to declare subsystem dependencies
    drivetrain = drive;
    targetAngle = angle;
    addRequirements(drive);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  public void execute() 
  {
    //angleDiff is in the range [-1,1]
    double angleDiff = (targetAngle - drivetrain.getYaw())/180;
    System.out.println("TrueDiff: " + (targetAngle - drivetrain.getYaw()));
    double steer = Math.copySign(Math.pow(Math.abs(angleDiff), 1), angleDiff); //Can modify how it steers here
    if (Math.abs(steer)<0.35) steer = Math.copySign(0.35, steer);
    System.out.println("Target: " + targetAngle + ", angleDiff: " + angleDiff + ", steer: " + steer);
    drivetrain.arcadeDrive(0, steer);

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return Math.abs(targetAngle-drivetrain.getYaw())<2;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    drivetrain.tankDriveVolts(0,0);
  }
}
