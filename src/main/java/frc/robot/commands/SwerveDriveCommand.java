// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.ControlInput;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class SwerveDriveCommand extends Command {
  private DriveSubsystem subsystem;
  private VisionSubsystem visionSubsystem;
  private PIDController rotationPID;
  private ControlInput controlInput;
  private PIDController forwardPID;
  private PIDController strafePID;

  // false = red;
  // true = blue;
  // private boolean isTeam = false;

  // pipeline 0: red
  // pipeline 1: blue

  // todo: add vision and lighting
  public SwerveDriveCommand(DriveSubsystem subsystem, VisionSubsystem vs,ControlInput controlInput) {
    this.subsystem = subsystem;
    this.visionSubsystem = vs;
    this.controlInput = controlInput;
    

    addRequirements(subsystem);
    setName("SwerveDrive");

    rotationPID = new PIDController(0, 0, 0);
    forwardPID = new PIDController(0, 0, 0);
    strafePID = new PIDController(0, 0, 0);

    SmartDashboard.putBoolean("Red/Blue Pickup (r: true/: false)", false);
  }

  @Override
  public void initialize() {
    rotationPID.enableContinuousInput(0, 360);
    rotationPID.reset();
    forwardPID.reset();
    strafePID.reset();
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

    // need to add pipeline filtering again
    SmartDashboard.putNumber("Gyro Yaw", subsystem.getNavX().getYaw());
    SmartDashboard.putNumber("Gyro Angle", subsystem.getNavX().getAngle());
    SmartDashboard.putNumber("Gyro Heading", subsystem.getHeading());

    if (getInput().getLeftJoystick().getTrigger() && visionSubsystem.hasTargets()) {
      double distanceHorizFromTarget = visionSubsystem.getX();
      final double strafePIDVal = clamp(strafePID.calculate(distanceHorizFromTarget), -0.5, 0.5);

      // if the targets exist and the distance is accurate but the robot still goes away from the target, invert this.
      boolean pidInvert = false;
      subsystem.drive(forward, pidInvert ? -1 : 1 * strafePIDVal, 0, false, false);
      
    } else {
      subsystem.drive(forward, strafe, clamp(rotation * 3.2,
          -DriveConstants.kMaxAngularSpeed, DriveConstants.kMaxAngularSpeed),
          true, false);
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
