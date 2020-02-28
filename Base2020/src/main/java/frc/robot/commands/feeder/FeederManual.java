
package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Feeder;


public class FeederManual extends CommandBase{

  private Feeder feeder;
  private double fast, slow;
  private Joystick f1;
    public FeederManual (Feeder feederUsed, Joystick function1, double fastSpeed, double slowSpeed) {
      feeder = feederUsed;
      this.fast = fastSpeed;
      slow = slowSpeed;
      f1 = function1;
      addRequirements(feeder);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
      double speed = f1.getX();
      if (Math.abs(speed)<0.5) {
        feeder.setPercentOutput(-1*slow*f1.getY());
      } else {
        feeder.setPercentOutput(-1*fast*speed);
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