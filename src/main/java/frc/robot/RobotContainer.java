// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.pathplanner.lib.path.PathPlannerTrajectory.State;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.commands.PositionCommand.Position;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LightingSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.commands.AlignmentCommand;
import frc.robot.commands.PositionCommand;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.commands.autonomous.AutoMotions;

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
  private ClimberSubsystem climberSubsystem;
  private AutoMotions autoMotions;

  private final SendableChooser<Command> autoChooser;
  private final SendableChooser<Double> chooseAlign = new SendableChooser<Double>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    controlInput = new ControlInput();
    lightingSubsystem = new LightingSubsystem(controlInput);
    visionSubsystem = new VisionSubsystem();
    driveSubsystem = new DriveSubsystem(visionSubsystem);
    intakeSubsystem = new IntakeSubsystem(9, 12);
    swerveDriveCommand = new SwerveDriveCommand(driveSubsystem, visionSubsystem, controlInput);
    shooterSubsystem = new ShooterSubsystem(15, 13, controlInput, visionSubsystem, lightingSubsystem);
    climberSubsystem = new ClimberSubsystem(25);
    autoMotions = new AutoMotions(shooterSubsystem, intakeSubsystem, climberSubsystem);

    NamedCommands.registerCommand("shootClose", autoMotions.shootingClosestAutoMotion());
    NamedCommands.registerCommand("shootMiddle", autoMotions.shootingMiddleAutoMotion());
    NamedCommands.registerCommand("shootFar", autoMotions.shootingFarAutoMotion());
    NamedCommands.registerCommand("intakeMotion", autoMotions.intakeAutoMotion());
    NamedCommands.registerCommand("intakeRollers", autoMotions.runIntake());
    NamedCommands.registerCommand("shooterRollers", autoMotions.runShooter());
    NamedCommands.registerCommand("feederRollers", autoMotions.runFeederWithTimeout());
    NamedCommands.registerCommand("shooterPos", autoMotions.setShooterAutoPos());
    NamedCommands.registerCommand("shootingRoutine", autoMotions.shootingAutoMotion());
    NamedCommands.registerCommand("outOfAutoPos",
        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
            PositionCommand.Position.OUT_OF_AUTO_POSITION));
    // NamedCommands.registerCommand("align", autoMotions.resetGyroAuto());

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    // might need to make it negative (direction) or add 180 (rotation)
    chooseAlign.addOption("None", 0.0);
    chooseAlign.addOption("ampShootAdjustment", -60.0);
    chooseAlign.addOption("90", 90.0);
    SmartDashboard.putData("Alignment Choice", chooseAlign);

    configureBindings();
  }

  private void configureBindings() {
    alignmentCommand = new AlignmentCommand(driveSubsystem);
    JoystickButton alignmentButton = new JoystickButton(controlInput.getLeftJoystick(), 5);
    alignmentButton.whileTrue(alignmentCommand);

    // emergency
    JoystickButton intakeRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 7);
    intakeRollers.whileTrue(intakeSubsystem.runRollers());

    // emergency
    JoystickButton feederRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 8);
    feederRollers.whileTrue(shooterSubsystem.runFeeder());

    // emergency
    JoystickButton reverseFeederRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 9);
    reverseFeederRollers.whileTrue(shooterSubsystem.reverseFeeder());

    // emergency
    JoystickButton shooterRollers = new JoystickButton(controlInput.getAccessoryJoystick(), 10);
    shooterRollers.whileTrue(shooterSubsystem.runShooter());

    JoystickButton home = new JoystickButton(controlInput.getAccessoryJoystick(), 11);
    home.onTrue(
        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.HOME));

    JoystickButton intakeOut = new JoystickButton(controlInput.getAccessoryJoystick(), 12);
    intakeOut.onTrue(
        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.INTAKE_OUT));

    JoystickButton start_mode = new JoystickButton(controlInput.getLeftJoystick(), 7);
    start_mode.onTrue(new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
        PositionCommand.Position.START_POSITION));

    // JoystickButton intakeMotion = new
    // JoystickButton(controlInput.getAccessoryJoystick(), 3);
    // intakeMotion.whileTrue(new ParallelCommandGroup(
    // new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
    // PositionCommand.Position.INTAKE_OUT),
    // new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
    // PositionCommand.Position.ARM_GROUND_PICKUP),
    // new SequentialCommandGroup(
    // new WaitCommand(0.5),
    // new ParallelCommandGroup(
    // shooterSubsystem.runFeeder().until(shooterSubsystem::hasRing),
    // intakeSubsystem.runRollers().until(shooterSubsystem::hasRing)),
    // new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
    // PositionCommand.Position.INTAKE_IN_PICKUP),
    // new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
    // PositionCommand.Position.BASE))));

    JoystickButton newIntakeMotion = new JoystickButton(controlInput.getAccessoryJoystick(), 3);
    newIntakeMotion.onTrue(new SequentialCommandGroup(
        new ParallelCommandGroup(
            new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
                PositionCommand.Position.INTAKE_OUT),
            new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
                PositionCommand.Position.ARM_GROUND_PICKUP),
            intakeSubsystem.runRollers().until(shooterSubsystem::hasRing),
            shooterSubsystem.runFeeder().until(shooterSubsystem::hasRing)),
        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
            PositionCommand.Position.INTAKE_IN_PICKUP),
        new ParallelCommandGroup(
            shooterSubsystem.startShooter(),
            shooterSubsystem.setContinuousRun(true),
            new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
                PositionCommand.Position.SAFE_OR_SPEAKER))));

    JoystickButton shootingMotion = new JoystickButton(controlInput.getAccessoryJoystick(), 5);
    shootingMotion.onTrue(new SequentialCommandGroup(
        new ParallelCommandGroup(
            new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
                PositionCommand.Position.SAFE_OR_SPEAKER),
            new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem,
                PositionCommand.Position.INTAKE_IN_SHOOT)),
        new WaitCommand(0.3),
        shooterSubsystem.runFeeder().withTimeout(1),
        shooterSubsystem.endShooter(),
        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.HOME)));

    JoystickButton manualOveride = new JoystickButton(controlInput.getAccessoryJoystick(), 2);
    manualOveride.onTrue(shooterSubsystem.setContinuousRun(false));

    JoystickButton ampArms = new JoystickButton(controlInput.getAccessoryJoystick(), 4);
    ampArms
        .onTrue(new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.AMP));

    JoystickButton ampShooting = new JoystickButton(controlInput.getAccessoryJoystick(), 6);
    ampShooting.onTrue(new SequentialCommandGroup(
        shooterSubsystem.runAmpShooterRoutine(),
        new PositionCommand(shooterSubsystem, intakeSubsystem, climberSubsystem, PositionCommand.Position.HOME)));

    JoystickButton climbUp = new JoystickButton(controlInput.getRightJoystick(), 11);
    climbUp.whileTrue(climberSubsystem.runClimberMotorUp());

    JoystickButton climbDown = new JoystickButton(controlInput.getRightJoystick(), 9);
    climbDown.whileTrue(climberSubsystem.runClimberMotorDown());
  }

  // might have to reverse the the .until and .onlyWhile for the reverse

  public void autoExit() {
    driveSubsystem.getNavX().setAngleAdjustment(chooseAlign.getSelected());
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
    return autoChooser.getSelected();
  }

}
