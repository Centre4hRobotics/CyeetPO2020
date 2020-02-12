
package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class IntakeExtend extends CommandBase{

  private Intake intake;
    public IntakeExtend (Intake intakeUsed) {
      intake = intakeUsed;
      addRequirements(intakeUsed);
    }

    @Override
    public void execute() {

      intake.extend();
        
    }

    @Override
    public boolean isFinished () {
        return true;
    }
}