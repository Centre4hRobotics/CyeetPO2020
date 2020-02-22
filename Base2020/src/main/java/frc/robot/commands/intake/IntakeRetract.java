
package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class IntakeRetract extends CommandBase{

  private Intake intake;
    public IntakeRetract (Intake intakeUsed) {
      intake = intakeUsed;
      addRequirements(intakeUsed);
    }

    @Override
    public void execute() {

      intake.retract();
        
    }

    @Override
    public boolean isFinished () {
        return true;
    }
}