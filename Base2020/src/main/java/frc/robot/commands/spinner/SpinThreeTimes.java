/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.spinner;

import com.revrobotics.ColorMatchResult;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Spinner;

public class SpinThreeTimes extends CommandBase {
  
  //private final String kNONE = "None";

  private final double spinSpeed;
  private int spinCount;
  private String startingColor = "black";
  private String ColorOutput = "black";
  private Spinner spinner;
  private NetworkTableInstance ntinst;

  public SpinThreeTimes(Spinner spinner, NetworkTableInstance nt, double spin) {
    this.spinner = spinner;
    ntinst = nt;
    spinSpeed = spin;
    addRequirements(spinner);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {

    spinCount=0;
    //Might need to move this reset if we want to hold the value across separate runs
    ntinst.getTable("Spin Three").getEntry("Starting Color").setString("black"); 
    ntinst.getTable("Spin Three").getEntry("Spin Count").setNumber(0); 

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {

    Color foundColor = spinner.getColorSensor().getColor();

    ColorMatchResult ColorResult = spinner.getColorMatcher().matchClosestColor(foundColor);

    startingColor = ntinst.getTable("Spin Three").getEntry("Starting Color").getString("black");
    
    // Finds starting color
    
    
    if(startingColor.equals("black"))
    {
      startingColor = spinner.getColorString(ColorResult);
    }

    ntinst.getTable("Spin Three").getEntry("Starting Color").setString(startingColor); 

    //gets color outputs

    if(startingColor != "black")
    {

      ColorOutput = spinner.getColorString(ColorResult);

      //Spin Logic

      spinCount = (int)ntinst.getTable("Spin Three").getEntry("Spin Count").getDouble(0); 


      if(startingColor.equals(ColorOutput))
      {
        spinCount++; 
        Timer.delay(0.50);
      }

      ntinst.getTable("Spin Three").getEntry("Spin Count").setNumber(spinCount);

      if(spinCount >= 7)
      {
        Timer.delay(0.05);
        spinner.setSpeed(0);
        end(false);
      } 
      else
      {
        spinner.setSpeed(spinSpeed); 
      }
    }
  
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
