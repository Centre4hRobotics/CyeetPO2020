
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ExtendClimber extends CommandBase{

  private Climber climber;
    public ExtendClimber (Climber climberUsed) {
      climber = climberUsed;
      addRequirements(climber);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

      climber.extend();
        
    }

    @Override
    public void end (boolean interrupted) {
      climber.pistonOff();
    }

    @Override
    public boolean isFinished () {
        return false;
    }
}