/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Constants.PneumaticConstants;

/**
 * General pneumatics for all subsystems, owns compressor and solenoids
 */
public class Pneumatics {

  private Compressor pcm;
  private DoubleSolenoid climboid;
  private Solenoid intakoid, spinoid, shootoid;

  public Pneumatics () {
      climboid = new DoubleSolenoid(PneumaticConstants.kClimbChannels[0], PneumaticConstants.kClimbChannels[1]);
      intakoid = new Solenoid(PneumaticConstants.kIntakeChannel);
      spinoid = new Solenoid(PneumaticConstants.kSpinChannel);
      shootoid = new Solenoid(PneumaticConstants.kShootChannel);
      pcm = new Compressor();
      pcm.setClosedLoopControl(true);
      System.out.println("Compressor Enabled: " + pcm.enabled());
  }

  public void setSpinState (boolean state) {
    spinoid.set(state);
  }

  public void setShootState (boolean state) {
    shootoid.set(state);
  }

  public void setIntakeState (boolean state) {
    intakoid.set(state);
  }

  public void setClimbState (DoubleSolenoid.Value state) {
    climboid.set(state);
  }
}
