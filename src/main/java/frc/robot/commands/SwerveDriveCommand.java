// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.ControlInput;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class SwerveDriveCommand extends Command {
  private DriveSubsystem subsystem;
  private PIDController autoAlignPID;
  private ControlInput controlInput;
  private PIDController xPID;
  private PIDController yPID;

  // false = red;
  // true = blue;
  // private boolean isTeam = false;

  // pipeline 0: red
  // pipeline 1: blue

  // todo: add vision and lighting
  public SwerveDriveCommand(DriveSubsystem subsystem, ControlInput controlInput) {
    this.subsystem = subsystem;
    this.controlInput = controlInput;

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
    subsystem.drive(forward, strafe, clamp(rotation * 3.2, -DriveConstants.kMaxAngularSpeed, DriveConstants.kMaxAngularSpeed),
        true, false);

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
