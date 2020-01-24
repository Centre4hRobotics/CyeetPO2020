
package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;


public class ArcadeDrive extends CommandBase{
    private Drive dtrain;
    private XboxController xbc;
    private double maxOutput;

    public ArcadeDrive (Drive drivetrain, XboxController m_driverController, double maxOutput) {
        this.xbc = m_driverController;
        this.dtrain = drivetrain;
        this.maxOutput = maxOutput;
        addRequirements(dtrain);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double xSpeed = -1*xbc.getY(Hand.kLeft), zRotation = xbc.getX(Hand.kLeft);
        dtrain.arcadeDrive(xSpeed, zRotation, maxOutput);
    }

    @Override
    public void end (boolean interrupted) {
        dtrain.tankDriveVolts(0, 0);
    }

    @Override
    public boolean isFinished () {
        return false;
    }
}