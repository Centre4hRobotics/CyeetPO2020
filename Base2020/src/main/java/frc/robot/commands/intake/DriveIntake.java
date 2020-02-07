
package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class DriveIntake extends CommandBase{

  private Intake intake;
  private double speed;
    public DriveIntake (Intake intakeUsed, double speed) {
      intake = intakeUsed;
      this.speed = speed;
      addRequirements(intake);
    }


    @Override
    public void initialize() {    

    }

    @Override
    public void execute() {

      intake.setSpeed(speed);
        
    }

    @Override
    public void end (boolean interrupted) {
        
    }

    @Override
    public boolean isFinished () {
        return false;
    }
}