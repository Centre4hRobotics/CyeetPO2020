
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ManualWinch extends CommandBase{

  private Climber climber;
  private double speed;
  private Joystick f1, f2;
    public ManualWinch (Climber climberUsed, Joystick function1, Joystick function2, double maxSpeed) {
      climber = climberUsed;
      this.speed = maxSpeed;
      f1 = function1;
      f2 = function2;
      addRequirements(climber);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

      climber.setLSpeed(f1.getY()*speed);
      climber.setRSpeed(f2.getY()*speed);
        
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