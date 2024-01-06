// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;

import java.util.ArrayList;
import java.util.List;

public final class Constants {

  public static final class Vision {
    // how many degrees back is your limelight rotated from perfectly vertical?
    public static final double LIMELIGHT_MOUNT_ANGLE_DEGREES = 15.0;
    public static final double photonMountAngleDegrees = 15.0;

    // distance from the center of the Limelight lens to the floor (in inches)
    public static final double LIMELIGHT_LENS_HEIGHT_INCHES = 7.717;
    public static final double photonLensHeightInches = 7.717;

    // distance from the target to the floor
    public static final double GOAL_HEIGHT_INCHES = 15.0;
  }

  public static final class Drive {
    public static final double DISTANCE_PER_Rotation = 1/8.16;
    public static final double DRIVE_CONVERSION = 4.572/110; //8.33 / 1.0; // (gear ratio)

    public enum Module {
      FRONTLEFT(
          2,
          4,
          3, 
          new Translation2d(0.381, 0.381), 
          -0.880729
      ),
      FRONTRIGHT(
          3,
          2,
          1, 
          new Translation2d(0.381, -0.381), 
          -1.57568
      ),
      BACKLEFT(
          0,
          8,
          7, 
          new Translation2d(-0.381, +0.381), 
          0.97338
      ),
      BACKRIGHT(
          1,
          6,
          5, 
          new Translation2d(-0.381, -0.381), 
          -0.278867
      );
      private int analogEncoderPort, driveMotorCanID, turnMotorCanID;
      private Translation2d location;
      private double turnEncoderOffset;
      Module(
          int analogEncoderPort,
          int driveMotorCanID, 
          int turnMotorCanID, 
          Translation2d location, 
          double turnEncoderOffset) {
              this.analogEncoderPort = analogEncoderPort;
              this.driveMotorCanID = driveMotorCanID;
              this.turnMotorCanID = turnMotorCanID;
              this.location = location;
              this.turnEncoderOffset = turnEncoderOffset;
      }

      public int getAnalogEncoderPort() { return analogEncoderPort; }
      
      public int getDriveMotorCanID() { return driveMotorCanID; }

      public int getTurnMotorCanID() { return turnMotorCanID; }

      public Translation2d getLocation() { return location; }

      public double getTurnEncoderOffset() { return turnEncoderOffset; }
      
      public static Translation2d[] getLocations() {
          final List<Translation2d> locations = new ArrayList<>();
          for (Module swerveModule : Module.values()) {
              locations.add(swerveModule.getLocation());
          }
          return (Translation2d[]) locations.toArray();
      }
  }

  public static final double K_MAX_ANGULAR_VELOCITY = Math.PI;

  }
}
