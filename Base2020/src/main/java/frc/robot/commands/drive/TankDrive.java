
package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;


public class TankDrive extends CommandBase{
    private Drive dtrain;
    private XboxController xbc;

    public TankDrive (Drive drivetrain, XboxController m_driverController) {
        this.xbc = m_driverController;
        this.dtrain = drivetrain;
        addRequirements(dtrain);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double lVolts = xbc.getY(Hand.kLeft), rVolts = xbc.getY(Hand.kRight);
        dtrain.tankDriveVolts(Math.abs(lVolts) < 0.1 ? 0:lVolts, Math.abs(rVolts) < 0.1 ? 0:rVolts);
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