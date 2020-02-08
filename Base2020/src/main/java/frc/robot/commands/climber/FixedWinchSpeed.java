
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class FixedWinchSpeed extends CommandBase{

  private Climber climber;
  private double speed;
    public FixedWinchSpeed (Climber climberUsed, double speed) {
      climber = climberUsed;
      this.speed = speed;
      addRequirements(climber);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

      climber.setRSpeed(speed);
      climber.setLSpeed(speed);
        
    }

    @Override
    public void end (boolean interrupted) {
      climber.setRSpeed(0.0);
      climber.setLSpeed(0.0);
    }

    @Override
    public boolean isFinished () {
        return false;
    }
}