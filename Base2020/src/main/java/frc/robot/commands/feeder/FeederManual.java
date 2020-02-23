
package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;


public class FeederManual extends CommandBase{

  private Feeder feeder;
  private double mx;
  private Joystick f1;
    public FeederManual (Feeder feederUsed, Joystick function1, double maxSpeed) {
      feeder = feederUsed;
      this.mx = maxSpeed;
      f1 = function1;
      addRequirements(feeder);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
      feeder.setPercentOutput(mx*f1.getY());
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