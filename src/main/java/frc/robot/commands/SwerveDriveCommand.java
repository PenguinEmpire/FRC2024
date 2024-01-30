// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.ControlInput;
import frc.robot.Constants.Drive;
import frc.robot.Constants.Vision;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LightingSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class SwerveDriveCommand extends Command {
  private DriveSubsystem subsystem;
  private PIDController autoAlignPID;
  private ControlInput controlInput;
  private LightingSubsystem lightingSubsystem;
  private VisionSubsystem visionSubsystem;
  private double targetRot;
  private PIDController xPID;
  private PIDController yPID;

  // false = red;
  // true = blue;
  private boolean isTeam = false;

  // pipeline 0: red
  // pipeline 1: blue

  private boolean m_wasTargeting;

  // todo: add vision and lighting
  public SwerveDriveCommand(DriveSubsystem subsystem, ControlInput controlInput, LightingSubsystem LightingSubsystem,
      VisionSubsystem visionSubsystem) {
    this.subsystem = subsystem;
    this.controlInput = controlInput;
    this.lightingSubsystem = lightingSubsystem;
    this.visionSubsystem = visionSubsystem;
    this.targetRot = subsystem.getAngle();

    addRequirements(subsystem);
    setName("SwerveDrive");

    autoAlignPID = new PIDController(0.0154, 0, 0);
    xPID = new PIDController(0.02, 0, 0);
    yPID = new PIDController(0.02, 0, 0);

    SmartDashboard.putBoolean("Red/Blue Pickup (r: true/: false)", false);

  }

  @Override
  public void initialize() {
    autoAlignPID.enableContinuousInput(0, 360);
    xPID.reset();
    yPID.reset();
    m_wasTargeting = false;
  }

  @Override
  public void execute() {
    double forward = -getInput().getLeftJoystick().getRawAxis(1);
    double pow = 2;
    forward = linearDeadband(forward);
    forward = Math.copySign(Math.pow(forward, pow), forward);

    double strafe = -getInput().getLeftJoystick().getRawAxis(0);
    strafe = linearDeadband(strafe);

    strafe = Math.copySign(Math.pow(strafe, pow), strafe);

    double rotation = getInput().getRightJoystick().getRawAxis(2);

    rotation = Math.copySign(Math.pow(rotation, pow), rotation);
    rotation = linearDeadband(rotation);

    double angleValue = Math.abs(subsystem.getAngle());

    // this needs to change (idk how to)
    boolean isFacingOtherSide = angleValue > 90 && angleValue < 270;

    if (isFacingOtherSide) {
      visionSubsystem.setCustomPipeline(true);
      boolean isTeam = SmartDashboard.getBoolean("Red/Blue Pickup (r: true/: false)", false);
      if (isTeam) {
        visionSubsystem.setPipeline(1);
        System.out.println("1");
      } else {
        visionSubsystem.setPipeline(0);
        System.out.println("0");
      }
    } else  {
    visionSubsystem.setCustomPipeline(false);
    }

    if (getInput().getLeftJoystick().getTrigger()) {
      // debug
      double angleToGoalDegrees = Vision.LIMELIGHT_MOUNT_ANGLE_DEGREES + visionSubsystem.getPitch();
      double angleToGoalRadians = angleToGoalDegrees * (Math.PI / 180.0);

      double distanceFromLimelightToGoalInches = (Vision.GOAL_HEIGHT_INCHES - Vision.LIMELIGHT_MOUNT_ANGLE_DEGREES)
          / Math.tan(angleToGoalRadians);
      if (SmartDashboard.getBoolean("Enable Debug", false))
        SmartDashboard.putNumber("target dist", distanceFromLimelightToGoalInches);
      // debug

      double test = 0;
      if (isFacingOtherSide) {
        test = 180;
        forward *= -1;
      }

      double turnValue = -autoAlignPID.calculate(subsystem.getAngle(), test);


      System.out.println("Has targets: " + visionSubsystem.hasTargets());

      if (!visionSubsystem.hasTargets()) {
        if (m_wasTargeting) {
          System.out.println("targeting");
          subsystem.drive(
              forward,
              0,
              turnValue,
              false,
              false);
        }
        return;
      }
      m_wasTargeting = true;
      // lightingSubsystem.setTemporaryColor(0, 255, 0);

      strafe = -clamp(yPID.calculate(visionSubsystem.getYaw(), 0), -0.3, 0.3);

      subsystem.drive(
          forward,
          strafe,
          clamp(turnValue, -0.4, 0.4),
          false,
          false);
    }
    subsystem.drive(forward, strafe, clamp(rotation * 0.8, -Drive.MAX_ANGULAR_VELOCITY, Drive.MAX_ANGULAR_VELOCITY),
        true, false);
    autoAlignPID.reset();
    yPID.reset();

    if (getInput().getLeftJoystick().getTriggerReleased()){
      m_wasTargeting = false;
    }

  }

  private double linearDeadband(double input) {
    final double deadband = 0.095;
    if (Math.abs(input) < deadband) {
      return 0;
    } else {
      return input - deadband;
    }
  }

  public ControlInput getInput() {
    return this.controlInput;
  }

  public double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  public void limelightDrive() {

  }
}
