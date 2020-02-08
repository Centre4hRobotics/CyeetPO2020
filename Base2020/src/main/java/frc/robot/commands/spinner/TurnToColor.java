/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.spinner;

import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Spinner;

/**
 * An example command.  You can replace me with your own command.
 */
public class TurnToColor extends CommandBase {
    private double spinSpeed;
    private String colorWanted;
    private Spinner spinner;

  public TurnToColor(Spinner spinner, double spin, String Color) {
    this.spinner = spinner;
    this.spinSpeed = spin;
    this.colorWanted = Color;
    addRequirements(spinner);
    }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() 
  {
    // Color Finding Code

    Color foundColor = spinner.getColorSensor().getColor();
    ColorMatchResult colorResult = spinner.getColorMatcher().matchClosestColor(foundColor);
    String colorOutput = "black";
    colorOutput = spinner.getColorString(colorResult);
    boolean hasColorBeenFound = colorOutput != "black" && colorOutput.equals(colorWanted);

    if (hasColorBeenFound)
    {
      Timer.delay(0.05);
      end(false);
    }
    else {
      spinner.setSpeed(spinSpeed);
    }


    spinner.updateShuffle(hasColorBeenFound,colorOutput,colorWanted, colorResult.confidence, foundColor);

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    spinner.setSpeed(0);
  }
}