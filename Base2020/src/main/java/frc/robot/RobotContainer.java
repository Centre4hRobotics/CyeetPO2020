/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.Constants.*;
import frc.robot.commands.drive.*;
import frc.robot.commands.feeder.*;
import frc.robot.commands.spinner.*;
import frc.robot.commands.intake.*;
import frc.robot.commands.shooter.*;
import frc.robot.commands.climber.*;
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
  private final Shooter m_shooter;
  private final Spinner m_spinner;
  private final Intake m_intake;
  private final Feeder m_feeder;
  private final Climber m_climber;

  public static NetworkTableInstance ntinst;
  private final Pneumatics p_pneumatics;

  // The driver's controller
  private final XboxController c_driver;
  private final Joystick c_function1, c_function2;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    c_driver = new XboxController(OIConstants.kDriverControllerPort);
    c_function1 = new Joystick(OIConstants.kFunctionControllerPorts[0]);
    c_function2 = new Joystick(OIConstants.kFunctionControllerPorts[1]); 
    ntinst = NetworkTableInstance.getDefault();
    p_pneumatics = new Pneumatics();

    m_spinner = new Spinner(p_pneumatics);
    m_spinner.setDefaultCommand(null);
    
    m_shooter = new Shooter (p_pneumatics);
    m_shooter.setDefaultCommand(null);

    m_climber = new Climber (p_pneumatics);
    m_climber.setDefaultCommand(new ManualWinch(m_climber, c_function1, c_function2, 0.4));

    m_intake = new Intake (p_pneumatics);
    m_intake.setDefaultCommand(null);

    m_feeder = new Feeder ();
    m_feeder.setDefaultCommand(null);

    m_drive = new Drive();
    m_drive.setDefaultCommand(new ArcadeDrive(m_drive, c_driver, 1.0));

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
      //Drive commands
      new JoystickButton(c_driver, Button.kA.value).whenPressed (new ZeroPosition(m_drive));

      //Feeder commands
      new JoystickButton(c_function1, 1).whenPressed(new FeederFixed(m_feeder, 0.3));
      new JoystickButton(c_function1, 9).whenPressed(new FeederFixed(m_feeder, -0.3));

      //Spinner commands
      new JoystickButton(c_function1, 4).whenPressed(new SpinnerFixed(m_spinner, 0.3));
      new JoystickButton(c_function1, 5).whenPressed(new SpinnerFixed(m_spinner, -0.3));
      new JoystickButton(c_function1, 6).whenPressed(new SpinnerRetract(m_spinner));
      new JoystickButton(c_function1, 7).whenPressed(new SpinnerExtend(m_spinner));

      //Intake commands
      new JoystickButton(c_function2, 1).whenPressed(new IntakeFixed(m_intake, 0.3));
      new JoystickButton(c_function2, 2).whenPressed(new IntakeFixed(m_intake, -0.3));
      new JoystickButton(c_function1, 8).whenPressed(new IntakeRetract(m_intake));
      new JoystickButton(c_function2, 3).whenPressed(new IntakeExtend(m_intake));

      //Climber commands
      new JoystickButton(c_function1, 10).whenPressed(new ClimberPistonRetract(m_climber));
      new JoystickButton(c_function2, 9).whenPressed(new ClimberPistonOff(m_climber));
      new JoystickButton(c_function2, 10).whenPressed(new ClimberPistonExtend(m_climber));

      //Shooter commands
      new JoystickButton(c_function2, 4).whenPressed(new ShooterExtend(m_shooter));
      new JoystickButton(c_function2, 5).whenPressed(new ShooterRetract(m_shooter));
      new JoystickButton(c_function2, 6).whenPressed(new ShooterFixed(m_shooter, 0.6));
      new JoystickButton(c_function2, 7).whenPressed(new ShooterFixed(m_shooter, 0.9));
      new JoystickButton(c_function2, 8).whenPressed(new ShooterFixed(m_shooter, 1.0));
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
