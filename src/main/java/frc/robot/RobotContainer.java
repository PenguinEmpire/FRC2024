// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.ControlInput;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LightingSubsystem;
import frc.robot.commands.AlignmentCommand;
import frc.robot.subsystems.VisionSubsystem;

import frc.robot.commands.Auto;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */ 

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private DriveSubsystem m_driveSubsystem;
  private SwerveDriveCommand m_swerveDriveCommand;
  private AlignmentCommand alignmentCommand;
  private LightingSubsystem m_lightingSubsystem;
  private VisionSubsystem m_visionSubsystem;
  private IntakeSubsystem m_intakeSubsystem;
  private Auto m_auto;

  private ControlInput m_controlInput;

  public SendableChooser<String> autoChooser;
  public final String kRoutine1;
  public final String kRoutine2;
  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    m_controlInput = new ControlInput();
    m_driveSubsystem = new DriveSubsystem();
    m_lightingSubsystem = new LightingSubsystem(m_controlInput);
    m_visionSubsystem = new VisionSubsystem(m_controlInput);
    // set new IDs
    m_intakeSubsystem = new IntakeSubsystem(11, 12);
    m_auto = new Auto();

    autoChooser = new SendableChooser<>();
    kRoutine1 = "Routine 1";
    kRoutine2 = "Routine 2";

    autoChooser.setDefaultOption(kRoutine1, kRoutine1);
    autoChooser.addOption(kRoutine2, kRoutine2);

    SmartDashboard.putData(autoChooser);

    configureBindings();
  }

  private void configureBindings() {

    JoystickButton amp = new JoystickButton(m_controlInput.getAccessoryJoystick(), 7);
    JoystickButton speaker = new JoystickButton(m_controlInput.getAccessoryJoystick(), 8);
    JoystickButton sourcepu = new JoystickButton(m_controlInput.getAccessoryJoystick(), 9);
    JoystickButton home = new JoystickButton(m_controlInput.getAccessoryJoystick(), 10);
    JoystickButton stage = new JoystickButton(m_controlInput.getAccessoryJoystick(), 11);

    alignmentCommand = new AlignmentCommand(m_driveSubsystem, m_controlInput);
    JoystickButton alignmentButton  = new JoystickButton(m_controlInput.getLeftJoystick(),5);
    alignmentButton.whileTrue(alignmentCommand);

  }

  public void autoExit() {
    m_driveSubsystem.getNavX().setAngleAdjustment(180);
  }

  public void robotInit(){
    m_driveSubsystem.getNavX().setAngleAdjustment(0);
  }
  
  public void teleopInit() {
    m_swerveDriveCommand = new SwerveDriveCommand(m_driveSubsystem, m_controlInput, m_lightingSubsystem, m_visionSubsystem);
    m_driveSubsystem.setDefaultCommand(m_swerveDriveCommand);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    Object choice = autoChooser.getSelected();

    if (choice == kRoutine1){
      System.out.println("Running 1");
      return m_auto.firstRoutine(m_driveSubsystem);
    } else if (choice == kRoutine2) {
      System.out.println("Running 2");
      return m_auto.secondRoutine(m_driveSubsystem);
    } else {
      return null;
    }
  }
}
