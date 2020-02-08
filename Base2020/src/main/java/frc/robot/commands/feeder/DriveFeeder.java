
package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;


public class DriveFeeder extends CommandBase{

  private Feeder feeder;
  private double speed;
    public DriveFeeder (Feeder feederUsed, double speed) {
      feeder = feederUsed;
      this.speed = speed;
      addRequirements(feeder);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

      feeder.setSpeed(speed);
        
    }

    @Override
    public void end (boolean interrupted) {
      feeder.setSpeed(0.0);
    }

    @Override
    public boolean isFinished () {
        return false;
    }
}