// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.commands.PositionCommand.Position;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LightingSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.commands.AlignmentCommand;
import frc.robot.commands.PositionCommand;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.commands.autonomous.AutoMotions;
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
  // private AutoPaths autoPaths;
  // private AutoMotions autoMotions;

  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private String m_autoSelected;
  private static final String Bcenter4P = "Blue - Center 4 Piece";

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
    shooterSubsystem = new ShooterSubsystem(15, 13, controlInput);
    // autoPaths = new AutoPaths();
    // autoMotions = new AutoMotions(shooterSubsystem, intakeSubsystem);

    m_chooser.addOption("Blue - Center 4 Pieces", Bcenter4P);
    SmartDashboard.putData("Auto Choices", m_chooser);

    configureBindings();
  }

  private void configureBindings() {
    alignmentCommand = new AlignmentCommand(driveSubsystem);
    JoystickButton alignmentButton = new JoystickButton(controlInput.getLeftJoystick(), 5);
    alignmentButton.whileTrue(alignmentCommand);

    JoystickButton intakeRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 7);
    intakeRollers.whileTrue(intakeSubsystem.runRollers());

    JoystickButton feederRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 8);
    feederRollers.whileTrue(shooterSubsystem.runFeeder());

    JoystickButton reverseFeederRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 9);
    reverseFeederRollers.whileTrue(shooterSubsystem.reverseFeeder());

    JoystickButton shooterRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 10);
    shooterRollers.whileTrue(shooterSubsystem.runShooter());

    JoystickButton home = new JoystickButton(controlInput.getAccessoryJoystick(), 11);
    home.onTrue(new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.BASE));

    JoystickButton intakeOut = new JoystickButton(controlInput.getAccessoryJoystick(), 12);
    intakeOut.onTrue(new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT));

    JoystickButton start_mode = new JoystickButton(controlInput.getLeftJoystick(), 7);
    start_mode.onTrue(new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.START_POSITION));

    JoystickButton intakeMotion = new JoystickButton(controlInput.getAccessoryJoystick(), 3);
    intakeMotion.onTrue(new ParallelCommandGroup(
        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_OUT),
        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.ARM_GROUND_PICKUP),
        new SequentialCommandGroup(
            new WaitCommand(1.5),
            new ParallelCommandGroup(
                shooterSubsystem.runFeeder().until(shooterSubsystem::hasRing),
                intakeSubsystem.runRollers().until(shooterSubsystem::hasRing)),
            shooterSubsystem.runFeeder().withTimeout(0.25),
            new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_IN_PICKUP),
            new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.BASE),
            new WaitCommand(1),
            shooterSubsystem.reverseFeeder().until(shooterSubsystem::hasRing),
            shooterSubsystem.reverseFeeder().onlyWhile(shooterSubsystem::hasRing))));

    // might have to change this to runCloseShooterRoutine - depends on how much power we need
    JoystickButton wooferShooterMotion = new JoystickButton(controlInput.getAccessoryJoystick(), 5);
    wooferShooterMotion.onTrue(new SequentialCommandGroup(
        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.INTAKE_IN_SHOOT),
        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.SAFE_OR_SPEAKER),
        shooterSubsystem.runFarShooterRoutine()));

  }

  // might have to reverse the the .until and .onlyWhile for the reverse

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
    m_autoSelected = m_chooser.getSelected();
    // if (m_autoSelected == Bcenter4P) {
    // return autoPaths.blueCenterFourPiece(intakeSubsystem, shooterSubsystem,
    // autoMotions);
    // } else {
    return null;

  }
}
