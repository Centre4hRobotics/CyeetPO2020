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
  private NetworkTableEntry colorFound, colorWantedFound, colorWanted, confidence, colorOut;
  private SimpleWidget colorFoundWidget, colorWantedWidget;

  public Spinner (Pneumatics pcm) {

      motor = new TalonSRX(SpinnerConstants.kSpinCAN);
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
                        .withPosition(1,0);
      colorFound = colorFoundWidget.getEntry();

        //Position: X_____
        //          ______
      colorWantedWidget = colorTab.add("Color Wanted", false);
      colorWantedWidget.withProperties(Map.of("colorWhenFalse", "black"))
                        .withPosition(0,0);
      colorWanted = colorWantedWidget.getEntry();
    
      //Set positions for these three
      colorWantedFound = colorTab.add("Has Wanted Color Been Found", false).withProperties(Map.of("colorWhenFalse", "black", "colorWhenTrue", "white")).getEntry();
      confidence = colorTab.add("Confidence", "0").getEntry();
      colorOut = colorTab.add("Color: ", ".").getEntry();
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

  /*public void updateShuffleWanted (String wanted) {
      colorWantedWidget.withProperties(Map.of("colorWhenTrue", wanted));
      colorWanted.setBoolean(true);
  }

  public void updateShuffleFound (String found) {

  }*/

  //Transfer these to separate methods so it is not always necessary to update all of them. 
  //Alternatively, simply send in the raw color output and calculate the rest here.
  public void updateShuffle(boolean wantedFound, String colorFound, String wanted, double conf, Color colorOut)
  {
    colorWantedFound.setBoolean(wantedFound);
    colorFoundWidget.withProperties(Map.of("colorWhenTrue", colorFound));
    this.colorFound.setBoolean(true);
    colorWanted.setString(wanted);
    confidence.setString("" + conf);
    this.colorOut.setString("Blue: " + colorOut.blue + " Green: " + colorOut.green + " Red: " + colorOut.red);
  }

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
