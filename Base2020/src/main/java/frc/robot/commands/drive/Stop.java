package frc.robot.commands.drive;

import java.util.Set;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Drive;

public class Stop implements Command {
    private Drive dtrain;
    public Stop(Drive drivetrain) {
        this.dtrain = drivetrain;
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return true;
    }

    public void end(boolean interrupted) {
        //System.out.print("Stop complete");
        dtrain.tankDriveVolts(0,0);
    }

    @Override
    public Set<Subsystem> getRequirements() {
        // TODO Auto-generated method stub
        return Set.of(dtrain);
    }
}