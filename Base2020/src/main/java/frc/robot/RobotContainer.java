/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.Constants.*;
import frc.robot.commands.drive.*;
import frc.robot.subsystems.*;
import frc.robot.Pneumatics;

import static edu.wpi.first.wpilibj.XboxController.Button;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final Drive m_drive;
  private final Pneumatics p_pneumatics;
  private final Shooter m_shooter;
  private final Spinner m_spinner;
  public static NetworkTableInstance ntinst;

  // The driver's controller
  private final XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    ntinst = NetworkTableInstance.getDefault();
    p_pneumatics = new Pneumatics();

    m_spinner = new Spinner(p_pneumatics);
    m_spinner.setDefaultCommand(null);
    
    m_shooter = new Shooter (p_pneumatics);
    m_shooter.setDefaultCommand(null);

    m_drive = new Drive();
    m_drive.setDefaultCommand(new ArcadeDrive(m_drive, m_driverController, 1.0));

    configureButtonBindings();
  }

  public void robotInit () {
    m_drive.zeroHeading();
    m_drive.resetOdometry(new Pose2d());
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    // Drive at half speed when the right bumper is held
      new JoystickButton(m_driverController, Button.kA.value)
        .whenPressed (new ZeroPosition(m_drive));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new RunTrajectory(m_drive, Trajectories.diamond)
              //.andThen(new RunTrajectory(m_drive, Trajectories.turnBackAroundTrajectory))
              .andThen(new Stop(m_drive));
  }
}
