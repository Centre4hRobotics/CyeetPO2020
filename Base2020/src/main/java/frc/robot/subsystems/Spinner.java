/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import frc.robot.Pneumatics;
import frc.robot.Constants.CANIDs;
import frc.robot.Constants.SpinnerConstants;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Spinner extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private TalonSRX motor;
  private Pneumatics p;
  private ColorSensorV3 colorSensor;
  private ColorMatch colorMatcher;
  private ShuffleboardTab colorTab;
  private NetworkTableEntry colorFound, colorWantedString, colorWanted, confidence, colorOut, colorFoundString /*, colorWantedFound*/;
  private SimpleWidget colorFoundWidget, colorWantedWidget;

  public Spinner (Pneumatics pcm) {

      motor = new TalonSRX(CANIDs.kSpinCAN);
      this.p = pcm;
      
      // Rev Color sensor V3
      colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
      
      //Takes a list of Color Val and will compare them to know Vals
      colorMatcher = new ColorMatch();

      //Gives Colors to Colors Matcher

      colorMatcher.addColorMatch(getBlue());
      colorMatcher.addColorMatch(getGreen());
      colorMatcher.addColorMatch(getRed());
      colorMatcher.addColorMatch(getYellow());

      // Outputs

      //Reminder: set positions of widgets here so we don't have to save shuffleboard layouts. 

      colorTab = Shuffleboard.getTab("Spinner");

        //Position: _X____
        //          ______
      colorFoundWidget = colorTab.add("Color Seen", false);
      colorFoundWidget.withProperties(Map.of("colorWhenFalse", "black"))
                        .withPosition(1,0).withSize(1, 1);
      colorFound = colorFoundWidget.getEntry();

        //Position: X_____
        //          ______
      colorWantedWidget = colorTab.add("Color Wanted", false);
      colorWantedWidget.withProperties(Map.of("colorWhenFalse", "black"))
                        .withPosition(0,0).withSize(1, 1);
      colorWanted = colorWantedWidget.getEntry();
    
      //Set positions for these three
      //colorWantedStringFound = colorTab.add("Has Wanted Color Been Found", false).withProperties(Map.of("colorWhenFalse", "black", "colorWhenTrue", "white")).getEntry();
      confidence = colorTab.add("Confidence", "0").withPosition(2,0).withSize(2, 1).getEntry(); //2wide
      colorOut = colorTab.add("Color: ", ".").withPosition(0,1).withSize(3, 1).getEntry(); //3wide
      colorFoundString = colorTab.add("Color seen String", "black").withPosition(3,1).withSize(2, 1).getEntry(); //2wide
      colorWantedString = colorTab.add("Color wanted string", "black").withPosition(5,1).withSize(2, 1).getEntry(); //2wide
  }

  public void setSpeed (double speed) {
      motor.set(ControlMode.Velocity, speed);
  }

  public void extend () {
      p.setSpinState(true);
  }

  public void retract () {
      p.setSpinState(false);
  }

  //Transfer these to separate methods so it is not always necessary to update all of them. 
  //Alternatively, simply send in the raw color output and calculate the rest here.
  private void updateShuffle(String colorFound, String wanted, double conf, Color colorOut)
  {
    colorFoundWidget.withProperties(Map.of("colorWhenTrue", colorFound));
    this.colorFound.setBoolean(true);
    colorWantedWidget.withProperties(Map.of("colorWhenTrue", wanted));
    this.colorWanted.setBoolean(true);
    colorWantedString.setString(wanted);
    confidence.setString("" + conf);
    this.colorOut.setString("Blue: " + colorOut.blue + " Green: " + colorOut.green + " Red: " + colorOut.red);
    colorFoundString.setString(colorFound);
  }

  public void calculateShuffle (String wanted, Color found)
  {
    found = getColorSensor().getColor();
    ColorMatchResult colorResult = getColorMatcher().matchClosestColor(found);
    String colorOutput =  getColorString(colorResult);
   // boolean wantedFound = colorOutput != "black" && colorOutput.equals(wanted);
    updateShuffle(colorOutput, wanted, colorResult.confidence, found);
  }

  public String getCurrentColor()
  {
    return colorFoundString.getString("black");
  }

  public String getColorWanted ()
  {
    return colorWantedString.getString("black");
  }

  //maybe add network tables for colorFound and wanted

  //Color Getters

  public ColorMatch getColorMatcher()
  {
    return colorMatcher;
  }

  public ColorSensorV3 getColorSensor()
  {
    return colorSensor;
  }

  public Color getRed()
  {
    return SpinnerConstants.kRedTarget;
  }

  public Color getBlue()
  {
    return SpinnerConstants.kBlueTarget;
  }

  public Color getYellow()
  {
    return SpinnerConstants.kYellowTarget;
  }

  public Color getGreen()
  {
    return SpinnerConstants.kGreenTarget;
  }

  /**
   * 
   * @param colorInput color match result
   * @return color name
   */

  public String getColorString(ColorMatchResult colorInput)
  {
    
    String colorNameOutput = "black";

    if(colorInput.confidence > .96)
    {

      if(colorInput.color == getRed())
      {
        colorNameOutput = "red";
      }
      else if(colorInput.color == getYellow())
      {
        colorNameOutput = "yellow";
      }
      else if(colorInput.color == getGreen())
      {
        colorNameOutput = "green";
      }
      else if(colorInput.color == getBlue())
      {
        colorNameOutput = "blue";
      }
      
    }

    return colorNameOutput;

  }
}
