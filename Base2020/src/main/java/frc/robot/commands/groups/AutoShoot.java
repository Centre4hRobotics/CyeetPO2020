package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.drive.Stop;
import frc.robot.commands.feeder.FeederFixed;
import frc.robot.commands.shooter.*;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class AutoShoot extends SequentialCommandGroup {

    Shooter shooter;
    Feeder feeder;
    JoystickButton button;


    public AutoShoot (Drive drive, Shooter shoot, Feeder feed, boolean close, JoystickButton shootLever) {
        super(
            (close ? new ShooterRetract(shoot): new ShooterExtend(shoot)),
            new ParallelCommandGroup(
                //change to drive back or line up with vision and remove the timeout
                (close ? new Stop(drive): new Stop(drive)).withTimeout(1),
                new SequentialCommandGroup(
                    new FeederFixed(feed, -0.5).withTimeout(0.2),
                    //Make a shooting based on vision distance
                    (close ? new ShooterVelocityNoReset(shoot, 0.6):new ShooterVelocityNoReset(shoot, 0.0)).withTimeout(0.4)
                )
            ),
            new Stop(drive),
            new ParallelCommandGroup(
                (close ? new ShooterVelocity(shoot, 0.6):new ShooterVelocity(shoot, 0.0)),
                new FeederFixed(feed, 0.5)
            )
        );
        this.button = shootLever;
    }

    public void end (boolean interrupted) {
        shooter.setShootState(button.get());
        shooter.setVelocity(0.0);
        feeder.setPercentOutput(0.0);
    }
}