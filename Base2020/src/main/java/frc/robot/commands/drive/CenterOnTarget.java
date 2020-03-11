/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

/**
 * An example command.  You can replace me with your own command.
 */
public class CenterOnTarget extends CommandBase {

    private Drive drivetrain;
    private NetworkTableInstance nt;
  public CenterOnTarget(Drive drive, NetworkTableInstance ntinst) {
    // Use requires() here to declare subsystem dependencies
    drivetrain = drive;
    nt = ntinst;

    addRequirements(drive);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    nt.getTable("Test").getEntry("Steer").setNumber(0);
  }

  // Called repeatedly when this Command is scheduled to run
  public void execute() 
  {
    /*if (!Robot.ntinst.getTable("Vision").getEntry("Contour Found").getBoolean(false)) {
        end();
        return;
    }*/

    double xCenter = nt.getTable("Vision").getEntry("XCenter").getDouble(0.0)+0.08;
    double steer = 0.8*Math.copySign(Math.pow(Math.abs(xCenter), 1), xCenter); //Can modify how it steers here
    if (Math.abs(steer)<0.27 && Math.abs(steer)>0.03) steer = Math.copySign(0.27, steer);
    nt.getTable("Test").getEntry("Steer").setNumber(steer);
    drivetrain.arcadeDrive(0, steer);

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return Math.abs(nt.getTable("Vision").getEntry("XCenter").getDouble(0.0)+0.08)<0.03;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    drivetrain.tankDriveVolts(0,0);
  }
}
