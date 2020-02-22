
package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;


public class FeederFixed extends CommandBase{

  private Feeder feeder;
  private double speed;
    public FeederFixed (Feeder feederUsed, double speed) {
      feeder = feederUsed;
      this.speed = speed;
      addRequirements(feeder);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

      feeder.setPercentOutput(speed);
        
    }

    @Override
    public void end (boolean interrupted) {
      feeder.setPercentOutput(0.0);
    }

    @Override
    public boolean isFinished () {
        return false;
    }
}