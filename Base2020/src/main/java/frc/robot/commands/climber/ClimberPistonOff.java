
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ClimberPistonOff extends CommandBase{

  private Climber climber;
    public ClimberPistonOff (Climber climberUsed) {
      climber = climberUsed;
      addRequirements(climber);
    }

    @Override
    public void execute() {

      climber.pistonOff();
        
    }

    @Override
    public boolean isFinished () {
        return true;
    }
}