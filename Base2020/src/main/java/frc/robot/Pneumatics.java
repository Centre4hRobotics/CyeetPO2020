/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * General pneumatics for all subsystems, owns compressor and solenoids
 */
public class Pneumatics {

  private Compressor pcm;
  private DoubleSolenoid spinoid;

  public Pneumatics () {
      spinoid = new DoubleSolenoid(1,2);
      pcm = new Compressor();
      pcm.setClosedLoopControl(true);
      System.out.println("Compressor Enabled: " + pcm.enabled());
  }

  public void setSpinState (DoubleSolenoid.Value state) {
      spinoid.set(state);
  }

  public void setShootState (DoubleSolenoid.Value state) {
     spinoid.set(state);
  }
}
