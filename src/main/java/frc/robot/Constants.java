// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  //too lazy to do enum for drive constants
  // first four constants are from swervedrivespecialties github

  // this is all the old swerve code (can bring back if needed) 
  // public static class Drive {
  //   public static final double DISTANCE_PER_ROT = 1.0/8.16;
  //   public static final double WHEEL_DIAMETER = 4.064;
  //   public static final double DRIVE_REDUCTION = (8.33)/(1.0);
  //   public static final double DRIVE_CONVERSION = WHEEL_DIAMETER * Math.PI / DRIVE_REDUCTION;
    
  //   public static final double MAX_ANGULAR_VELOCITY = Math.PI;
  //   public static final double ROT_POSITION_CONVERSION_FACTOR = 2 * Math.PI / (18.0 / 1.0);
    
  //   public static final int DRIVE_CURRENT_LIMIT = 35;
  //   public static final int TURN_CURRENT_LIMIT = 20;
  //   public static final int SECONDARY_CURRENT_OFFSET = 5;
    
  // }

  // public static class Turn {
  //   public static final double P = 1.5;
  //   public static final double I = 0;
  //   public static final double D = 0.5;
  // }

  // public static enum SwerveModules {
  //   FRONTLEFT(2,4,3, new Translation2d(0.381, 0.381), -(0.0431 + 0.39216 + 3.14159265 - (2 * 3.14159265))),
  //   FRONTRIGHT(3,2,1, new Translation2d(0.381, -0.381), -(1.57568)),
  //   BACKLEFT(0,8,7, new Translation2d(-0.381, 0.381), -(-0.97338 + 6.2831)),
  //   BACKRIGHT(1,6,5, new Translation2d(-.381, -0.381), -(0.278867));
  //   //analog absolute encoder port (analog in on roborio)
  //   private final int encoderPort;

  //   //neo drive motor id on can loop
  //   private final int driveMotorID;

  //   //neo turn motor id on can loop
  //   private final int turnMotorID;

  //   //module location in 2d space
  //   private final Translation2d moduleLocation;

  //   //absolute encoder offset in radians
  //   private final double encoderOffset;
    
  //   SwerveModules(int encoderPort, int driveID, int turnID, Translation2d location, double encoderOffset) {
  //     this.encoderPort = encoderPort;
  //     this.driveMotorID = driveID;
  //     this.turnMotorID = turnID;
  //     this.moduleLocation = location;
  //     this.encoderOffset = encoderOffset;
  //   }

  //   public int getEncoderPort() {
  //     return this.encoderPort;
  //   }

  //   public int getDriveMotorID() {
  //     return this.driveMotorID;
  //   }

  //   public int getTurnMotorID() {
  //     return this.turnMotorID;
  //   }

  //   public Translation2d getModuleLocation() {
  //     return this.moduleLocation;
  //   } 

  //   public double getEncoderOffset() {
  //     return this.encoderOffset;
  //   }

  // }

  public static final class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 4.7;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    public static final double kDirectionSlewRate = 1.2; // radians per second
    public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
    public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

    // Chassis configuration (changed)
    public static final double kTrackWidth = Units.inchesToMeters(30.0);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(30);
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = 0;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = 0;
    public static final double kBackRightChassisAngularOffset = 0;

    // SPARK MAX CAN IDs (NEED TO CHANGE)
    // good
    public static final int kFrontLeftTurningCanId = 1;
    public static final int kFrontRightTurningCanId = 3;
    public static final int kRearLeftTurningCanId = 5;
    public static final int kRearRightTurningCanId = 7;
    
    public static final int kFrontLeftDrivingCanId = 2;
    public static final int kFrontRightDrivingCanId = 4;
    public static final int kRearLeftDrivingCanId = 6;
    public static final int kRearRightDrivingCanId = 8;


    // if navx is funky, change to true
    public static final boolean kGyroReversed = false;
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T, 13T, or 14T.
    // This changes the drive speed of the module (a pinion gear with more teeth will result in a
    // robot that drives faster).
    // need to check
    public static final int kDrivingMotorPinionTeeth = 12;

    // Invert the turning encoder, since the output shaft rotates in the opposite direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = false;
    public static final boolean kTurningMotorInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.1016;
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
    // if auto is screwed, look over the drive reduction
    public static final double kDrivingMotorReduction = (45.0 * 22 * 34) / (kDrivingMotorPinionTeeth * 15 * 24);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;

    public static final double kDrivingEncoderPositionFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction); // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    // need to configure
    public static final double kDrivingP = 0;
    public static final double kDrivingI = 0;
    public static final double kDrivingD = 0;
    public static final double kDrivingFF = (1 / kDriveWheelFreeSpeedRps);
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;

    // could turn p back to 0.04
    public static final double kTurningP = 0.3;
    public static final double kTurningI = 0;
    public static final double kTurningD = 0;
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 40; // amps
    public static final int kTurningMotorCurrentLimit = 30; // amps
  }

  public static final class NeoMotorConstants {
    public static final double kFreeSpeedRpm = 5676;
  }

  
  public static class Vision {
    public static final int GOAL_HEIGHT_INCHES = 122;

    // need to change these two
    public static final int DISTANCE_FROM_LIMELIGHT_TO_GOAL_INCHES = 0;
    public static final int  LIMELIGHT_MOUNT_ANGLE_DEGREES = 15;
    
  }

}
