
package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;


public class DriveStraight extends CommandBase{
    private Drive dtrain;
    private double speed;

    public DriveStraight (Drive drivetrain, double vel) {
        this.dtrain = drivetrain;
        this.speed = vel;
        addRequirements(dtrain);
    }

    @Override
    public void execute() { 
        dtrain.arcadeDrive(speed, 0);
    }

    @Override
    public void end (boolean interrupted) {
        dtrain.tankDriveVolts(0, 0);
    }

    @Override
    public boolean isFinished () {
        return false;
    }
}