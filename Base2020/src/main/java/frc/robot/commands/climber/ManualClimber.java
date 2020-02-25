
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;


public class ManualClimber extends CommandBase{

  private Climber climber;
  private double mx;
  private XboxController f2;
    public ManualClimber (Climber climberUsed, XboxController function2, double maxSpeed) {
      climber = climberUsed;
      this.mx = maxSpeed;
      f2 = function2;
      addRequirements(climber);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
      if (f2.getBumper(Hand.kLeft)) {
        double rs = f2.getY(Hand.kRight), ls = f2.getY(Hand.kLeft);
        climber.setLSpeed((Math.abs(ls)<0.2 ? 0:ls)*mx);
        climber.setRSpeed((Math.abs(rs)<0.2 ? 0:rs)*mx);
        /*if (f2.getStartButtonPressed()) climber.extend();
        if (f2.getBackButtonPressed()) climber.retract();*/
      } else {
        climber.setLSpeed(0);
        climber.setRSpeed(0);
      }
      
        
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