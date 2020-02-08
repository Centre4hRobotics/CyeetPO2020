
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class RetractClimber extends CommandBase{

  private Climber climber;
    public RetractClimber (Climber climberUsed) {
      climber = climberUsed;
      addRequirements(climber);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

      climber.retract();
        
    }

    @Override
    public void end (boolean interrupted) {
      
    }

    @Override
    public boolean isFinished () {
        return true;
    }
}