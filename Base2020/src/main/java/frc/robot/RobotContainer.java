/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;

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
  private UsbCamera w_driverCam;

  public static NetworkTableInstance ntinst;
  private final Pneumatics p_pneumatics;

  //private SendableChooser<String> autoselect;

  // The driver's controller
  private final XboxController c_driver, c_function2;
  private final Joystick c_function1;

  //private SendableChooser<Command> autoselect;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    c_driver = new XboxController(OIConstants.kDriverControllerPort);
    c_function1 = new Joystick(OIConstants.kFunctionControllerPorts[0]);
    c_function2 = new XboxController(OIConstants.kFunctionControllerPorts[1]); 
    ntinst = NetworkTableInstance.getDefault();
    p_pneumatics = new Pneumatics();

    m_spinner = null;//new Spinner(p_pneumatics);
    //m_spinner.setDefaultCommand(null);
    
    m_shooter = new Shooter (p_pneumatics);
    //m_shooter.setDefaultCommand(new ShooterVolts(m_shooter, 0.0));

    m_climber = new Climber (p_pneumatics);
    m_climber.setDefaultCommand(new ManualClimber(m_climber, c_function2, 1));

    m_intake = new Intake (p_pneumatics);
    //m_intake.setDefaultCommand(new IntakeFixed(m_intake, 0.0));

    m_feeder = new Feeder ();
    m_feeder.setDefaultCommand(new FeederManual(m_feeder, c_function1, 0.5, 0.3));

    m_drive = new Drive();
    m_drive.setDefaultCommand(new ArcadeDrive(m_drive, c_driver, 1.0));

    configureButtonBindings();
    autoChooserInit();
    //shuffleboardInit();
  }

  public void autoChooserInit() {
    String[] autoselector = {"AutoLine", "CenterUpThenTrench", "RightAutoVision", "CenterUp", 
    "RightAutoTTA", "CenterUpWait5", "RightAutoTTAStraight", "RightAutoTTAStraightFast"};
    SmartDashboard.putStringArray("Auto List", autoselector);
    /*autoselect = new SendableChooser<String>();
    autoselect.addOption("AutoLine", "AutoLine");
    autoselect.addOption("CenterUpThenTrench", "CenterUpThenTrench");
    autoselect.addOption("RightBack", "RightAuto");
    autoselect.addOption("CenterUp", "CenterSimple");*/
    //SmartDashboard.putData("Auto Selector", autoselect);
    //Shuffleboard.getTab("Auto").add(autoselect);
  }

  public void robotInit () {
    m_drive.zeroHeading();
    m_drive.resetOdometry(new Pose2d());
    m_climber.retract();
  }

  public void teleopInit() {
    m_climber.retract();
  }

  public void cameraStreamInit () {
    w_driverCam = CameraServer.getInstance().startAutomaticCapture();
    w_driverCam.setResolution(640, 480);
    w_driverCam.setFPS(10);
    /*CvSink cvSink = CameraServer.getInstance().getVideo();
    CvSource output = CameraServer.getInstance().putVideo("Drive", 640, 480);*/
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
      //Drive commands
      new JoystickButton(c_driver, Button.kBack.value).whenPressed (new ZeroPosition(m_drive));
      new JoystickButton(c_driver, Button.kA.value).whileHeld (new CenterOnTarget(m_drive, ntinst));
      new JoystickButton(c_driver, Button.kX.value).whileHeld (
        new SequentialCommandGroup(
          new ZeroPosition(m_drive),
          new RunTrajectory(m_drive, Trajectories.feederStationToIntakeExtend),
          new Stop(m_drive),
          new IntakeExtend(m_intake),
          new IntakeFixed(m_intake, 0.0).withTimeout(0.5),
          new IntakeFixed(m_intake, 1)
        )
      );
      new JoystickButton(c_driver, Button.kStart.value).whileHeld(new TurnToAngle(m_drive, 90));
      new JoystickButton(c_driver, Button.kBumperLeft.value).whileHeld(new TurnToAngle(m_drive, -45));
      /*new JoystickButton(c_driver, Button.kStart.value).whileHeld(new UpdatePID(m_shooter));
      new JoystickButton(c_driver, Button.kA.value).whileHeld(new ShooterPercentOutput(m_shooter, 0.25));
      new JoystickButton(c_driver, Button.kB.value).whileHeld(new ShooterPercentOutput(m_shooter, 0.5));
      new JoystickButton(c_driver, Button.kX.value).whileHeld(new ShooterPercentOutput(m_shooter, 0.75));
      new JoystickButton(c_driver, Button.kY.value).whileHeld(new ShooterPercentOutput(m_shooter, 1.0));*/
 
      //Feeder commands
      /*new JoystickButton(c_function1, 1).whileHeld(new FeederFixed(m_feeder, 0.3));
      new JoystickButton(c_function1, 9).whileHeld(new FeederFixed(m_feeder, -0.3));*/

      //Spinner commands
      /*new JoystickButton(c_function1, 7).whileHeld(new SpinnerFixed(m_spinner, 0.2));
      new JoystickButton(c_function1, 8).whileHeld(new SpinnerFixed(m_spinner, -0.2));
      new JoystickButton(c_function1, 11).whenPressed(new SpinnerExtend(m_spinner))
          .whenReleased(new SpinnerRetract(m_spinner));*/

      //Intake commands
      new JoystickButton(c_function1, 6).whileHeld(new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed));
      new JoystickButton(c_function1, 5).whileHeld(new IntakeFixed(m_intake, -0.6)); 
      new JoystickButton(c_function1, 9).whenPressed(new IntakeRetract(m_intake))
          .whenReleased(new IntakeExtend(m_intake));

      //Climber commands
      /*new JoystickButton(c_function1, 10).whenPressed(new ClimberPistonRetract(m_climber));
      new JoystickButton(c_function2, 9).whenPressed(new ClimberPistonOff(m_climber));
      new JoystickButton(c_function2, 10).whenPressed(new ClimberPistonExtend(m_climber));*/

      //Shooter commands
      //Spin up while running path!!!
      new JoystickButton(c_function1, 1).whileHeld (
        new SequentialCommandGroup(
          new ZeroPosition(m_drive),
          new ParallelDeadlineGroup(
            new RunTrajectory(m_drive, Trajectories.straightBackToShootThrees),
            new FeederFixed(m_feeder, -0.2)
          ),
          new Stop(m_drive),
          new ShooterRetract(m_shooter),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts).withTimeout(0.3),
          new ParallelCommandGroup( 
            new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts),
            new FeederFixed (m_feeder, 0.3)
        )));
      new JoystickButton(c_function1, 10).whenPressed(new ShooterExtend(m_shooter))
          .whenReleased(new ShooterRetract(m_shooter));
      new JoystickButton(c_function1, 2).whileHeld(
        new ShooterVolts(m_shooter, 9.5/*ShootSpeedConstants.shortShotVolts*/)
        /*new FeederFixed(m_feeder, -0.3).withTimeout(0.4)
          .andThen(new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts))*/
      );
      //Top left is 4, bottom left is 3
      new JoystickButton(c_function1, 4).whileHeld(
        new ShooterVolts(m_shooter, ShootSpeedConstants.frontTrenchVolts)
        /*new FeederFixed(m_feeder, -0.3).withTimeout(0.4)
          .andThen(new ShooterVolts(m_shooter, ShootSpeedConstants.frontTrenchVolts))*/
      );
      new JoystickButton(c_function1, 3).whileHeld(
        new ShooterVolts(m_shooter, 9)//ShootSpeedConstants.farTrenchVolts)
        /*new FeederFixed(m_feeder, -0.3).withTimeout(0.4)
          .andThen(new ShooterVolts(m_shooter, ShootSpeedConstants.farTrenchVolts))*/
      );
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    String selected = SmartDashboard.getString("Auto Selector", "None");//"rightauto";
    System.out.println(selected);
    if (selected.equalsIgnoreCase("CenterUpThenTrench")) {
      return new SequentialCommandGroup(
        new ParallelDeadlineGroup (
          new RunTrajectory(m_drive, Trajectories.straightToShootThrees),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts)
        ),
        new Stop(m_drive),
        new ShooterRetract(m_shooter),
        new ParallelCommandGroup( 
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts),
          new FeederFixed (m_feeder, 0.5)
        ).withTimeout(3),
        new IntakeExtend(m_intake),
        new ParallelDeadlineGroup (
          new RunTrajectory(m_drive, Trajectories.fromCloseToRightAutoToTrench),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ),
        new Stop(m_drive),
        new ShooterExtend(m_shooter),
        new ParallelDeadlineGroup(
          new CenterOnTarget(m_drive, ntinst),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed),
          new FeederFixed(m_feeder, 0.5)
        )
      );
    }
    //Back into trench, score 4, back again, score 2 - using turn to angle instead of vision
    if (selected.equalsIgnoreCase("RightAutoTTA")) {
      return new SequentialCommandGroup(
        new IntakeExtend(m_intake),
        new ShooterExtend(m_shooter),
        new ParallelDeadlineGroup (
          new SequentialCommandGroup(
            new RunTrajectory(m_drive, Trajectories.fromRightAutoToShortTrench),
            new Stop(m_drive),
            new TurnToAngle(m_drive, PathConstants.angleFromShortTrench)
          ),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new SequentialCommandGroup(
            new IntakeFixed(m_intake, 0.0).withTimeout(0.6),
            new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
          )
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new FeederFixed(m_feeder, 0.4),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ).withTimeout(4),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new RunTrajectory(m_drive, Trajectories.fromShortTrenchToFarTrench),
            new Stop(m_drive),
            new TurnToAngle(m_drive, PathConstants.angleFromFarTrench)
          ),
          new ShooterVolts(m_shooter, ShootSpeedConstants.farTrenchVolts),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.farTrenchVolts),
          new FeederFixed(m_feeder, 0.4),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        )
      );
    }
    if (selected.equalsIgnoreCase("RightAutoTTAStraight")) {
      return new SequentialCommandGroup(
        new IntakeExtend(m_intake),
        new ShooterExtend(m_shooter),
        new ParallelDeadlineGroup (
          new SequentialCommandGroup(
            new RunTrajectory(m_drive, Trajectories.fromRightAutoToShortTrench),
            new Stop(m_drive),
            new TurnToAngle(m_drive, PathConstants.angleFromShortTrench)
          ),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new SequentialCommandGroup(
            new IntakeFixed(m_intake, 0.0).withTimeout(0.6),
            new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
          )
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new FeederFixed(m_feeder, 0.4),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ).withTimeout(4),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new TurnToAngle(m_drive, 0),
            new RunTrajectory(m_drive, Trajectories.fromShortTrenchToFarTrench),
            new Stop(m_drive),
            new TurnToAngle(m_drive, PathConstants.angleFromFarTrench)
          ),
          new ShooterVolts(m_shooter, ShootSpeedConstants.farTrenchVolts),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.farTrenchVolts),
          new FeederFixed(m_feeder, 0.4),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        )
      );
    }
    if (selected.equalsIgnoreCase("RightAutoTTAStraightFast")) {
      return new SequentialCommandGroup(
        new IntakeExtend(m_intake),
        new ShooterExtend(m_shooter),
        new ParallelDeadlineGroup (
          new SequentialCommandGroup(
            new RunTrajectory(m_drive, Trajectories.fromRightAutoToShortTrench),
            new Stop(m_drive),
            new TurnToAngle(m_drive, PathConstants.angleFromShortTrench)
          ),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new SequentialCommandGroup(
            new IntakeFixed(m_intake, 0.0).withTimeout(0.6),
            new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
          )
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new FeederFixed(m_feeder, 0.5),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ).withTimeout(3),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new TurnToAngle(m_drive, 0),
            new RunTrajectory(m_drive, Trajectories.fromShortTrenchToFarTrench),
            new Stop(m_drive)
          ),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ),
        new ParallelDeadlineGroup (
          new SequentialCommandGroup(
            new RunTrajectory(m_drive, Trajectories.fromFarTrenchToShortTrench),
            new Stop(m_drive),
            new TurnToAngle(m_drive, PathConstants.angleFromShortTrench)
          ),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts)
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new FeederFixed(m_feeder, 0.6),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        )
      );
    }
    if (selected.equalsIgnoreCase("RightAutoVision")) {
      return new SequentialCommandGroup(
        new IntakeExtend(m_intake),
        new ShooterExtend(m_shooter),
        new ParallelDeadlineGroup (
          new SequentialCommandGroup(
            new RunTrajectory(m_drive, Trajectories.fromRightAutoToShortTrench),
            new Stop(m_drive),
            new CenterOnTarget(m_drive, ntinst)
          ),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new SequentialCommandGroup(
            new IntakeFixed(m_intake, 0.0).withTimeout(0.6),
            new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
          )
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortTrenchAutoVolts),
          new FeederFixed(m_feeder, 0.4),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ).withTimeout(4),
        new ParallelDeadlineGroup(
          new SequentialCommandGroup(
            new RunTrajectory(m_drive, Trajectories.fromShortTrenchToFarTrench),
            new Stop(m_drive),
            new CenterOnTarget(m_drive, ntinst)
          ),
          new ShooterVolts(m_shooter, ShootSpeedConstants.farTrenchVolts),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        ),
        new ParallelCommandGroup(
          new ShooterVolts(m_shooter, ShootSpeedConstants.farTrenchVolts),
          new FeederFixed(m_feeder, 0.4),
          new IntakeFixed(m_intake, IntakeConstants.kIntakeSpeed)
        )
      );
    }
    if (selected.equalsIgnoreCase("CenterUp")) {
      return new SequentialCommandGroup(
        new ParallelDeadlineGroup (
          new RunTrajectory(m_drive, Trajectories.straightToShootThrees),
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts)
        ),
        new Stop(m_drive),
        new ShooterRetract(m_shooter),
        new ParallelCommandGroup( 
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts),
          new FeederFixed (m_feeder, 0.3)
        ).withTimeout(5)
      );
    }
    if (selected.equalsIgnoreCase("CenterUpWait5")) {
      return new SequentialCommandGroup(
        new RunTrajectory(m_drive, Trajectories.straightToShootThrees),
        new Stop(m_drive),
        new ShooterRetract(m_shooter),
        new ShooterVolts(m_shooter, 0.0).withTimeout(4),
        new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts).withTimeout(1),
        new ParallelCommandGroup( 
          new ShooterVolts(m_shooter, ShootSpeedConstants.shortShotVolts),
          new FeederFixed (m_feeder, 0.3)
        ).withTimeout(5)
      );
    }
    if (selected.equalsIgnoreCase("AutoLine")) {
      return new DriveStraight(m_drive, 0.4).withTimeout(0.2);
    }
    return new Stop(m_drive);

    
    /*new RunTrajectory(m_drive, Trajectories.diamond)
              //.andThen(new RunTrajectory(m_drive, Trajectories.turnBackAroundTrajectory))
              .andThen(new Stop(m_drive));*/
  }
}
