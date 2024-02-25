// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LightingSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.commands.AlignmentCommand;
import frc.robot.commands.PositionCommand;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.commands.autonomous.AutoPaths;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
@SuppressWarnings("unused")
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private DriveSubsystem driveSubsystem;
  private SwerveDriveCommand swerveDriveCommand;
  private AlignmentCommand alignmentCommand;
  private LightingSubsystem lightingSubsystem;
  private VisionSubsystem visionSubsystem;
  private IntakeSubsystem intakeSubsystem;
  private ControlInput controlInput;
  private ShooterSubsystem shooterSubsystem;

  public static SendableChooser<Command> autoChoice = new SendableChooser<>();

  private String test = "test";

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    controlInput = new ControlInput();
    driveSubsystem = new DriveSubsystem();
    lightingSubsystem = new LightingSubsystem(controlInput);
    visionSubsystem = new VisionSubsystem();
    intakeSubsystem = new IntakeSubsystem(9, 12);
    swerveDriveCommand = new SwerveDriveCommand(driveSubsystem, visionSubsystem, controlInput);
    // need to change the infraredSensorID
    shooterSubsystem = new ShooterSubsystem(15, 13, 16);

    configureBindings();
  }

  private void configureBindings() {

    JoystickButton intake_out = new JoystickButton(controlInput.getAccessoryJoystick(), 7);
    JoystickButton intake_in = new JoystickButton(controlInput.getAccessoryJoystick(), 8);

    // JoystickButton speaker = new
    // JoystickButton(controlInput.getAccessoryJoystick(), 8);
    // JoystickButton sourcepu = new
    // JoystickButton(controlInput.getAccessoryJoystick(), 9);
    // JoystickButton home = new JoystickButton(controlInput.getAccessoryJoystick(),
    // 10);
    // JoystickButton stage = new
    // JoystickButton(controlInput.getAccessoryJoystick(), 11);

    intake_out.onTrue(new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT));
    intake_in.onTrue(new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_IN));

    alignmentCommand = new AlignmentCommand(driveSubsystem);
    JoystickButton alignmentButton = new JoystickButton(controlInput.getLeftJoystick(), 5);
    alignmentButton.whileTrue(alignmentCommand);

    JoystickButton runRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 5);
    runRollers.whileTrue(shooterSubsystem.runIntakeRollers());

    JoystickButton outputRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 6);
    outputRollers.whileTrue(shooterSubsystem.runShooterRollers());

    JoystickButton intakeRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 4);
    intakeRollers.whileTrue(intakeSubsystem.runRollers());

    // JoystickButton intakeMotion = new
    // JoystickButton(controlInput.getAccessoryJoystick(), 3);
    // intakeMotion.onTrue(new ParallelCommandGroup(
    // new PositionCommand(shooterSubsystem, intakeSubsystem,
    // PositionCommand.Position.INTAKE_OUT), new WaitCommand(1),
    // shooterSubsystem.runIntakeRollers().withTimeout(5),
    // intakeSubsystem.runRollers().withTimeout(2)));

    // JoystickButton shooterMotion = new
    // JoystickButton(controlInput.getAccessoryJoystick(), 4);
    // shooterMotion.onTrue(shooterSubsystem.runBothRollers());
  }

  public void autoExit() {
    driveSubsystem.getNavX().setAngleAdjustment(0);
  }

  public void robotInit() {
    driveSubsystem.getNavX().setAngleAdjustment(0);
  }

  public void teleopInit() {
    swerveDriveCommand = new SwerveDriveCommand(driveSubsystem, visionSubsystem, controlInput);
    driveSubsystem.setDefaultCommand(swerveDriveCommand);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;

  }
}
