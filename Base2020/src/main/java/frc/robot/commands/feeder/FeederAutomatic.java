
package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;


public class FeederAutomatic extends CommandBase{

  private Feeder feeder;
  private double speed;
    public FeederAutomatic (Feeder feederUsed, double speed) {
      feeder = feederUsed;
      this.speed = speed;
      addRequirements(feeder);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
      if(feeder.sensorTriggered(1)&&!feeder.sensorTriggered(2)) 
      {
        feeder.setPercentOutput(speed);
      } else {
        feeder.setPercentOutput(0);
      }
        
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