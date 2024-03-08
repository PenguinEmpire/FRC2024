package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.ControlInput;
import frc.robot.module.Joint;

public class ShooterSubsystem extends SubsystemBase {

    private final CANSparkMax feederMotor;
    // used for output rollers
    private final CANSparkMax shooterMotor;

    private Joint arm;
    private Joint shooter;
    private DigitalInput proximitySensor;
    private double intakeFeederSpeed;
    private double shooterSpeed;

    private VisionSubsystem visionSubsystem;
    private LightingSubsystem lightingSubystem;

    private SparkPIDController shooterPIDController;
    private RelativeEncoder shooterEncoder;

    private ControlInput controlInput;

    public ShooterSubsystem(int feederID, int shooterID, ControlInput controlInput, VisionSubsystem vs, LightingSubsystem ls) {
        arm = new Joint("shooterArm", 11, 0.7, 0, 0, 0, 0, -0.25, 0.25, true, null, 0, false);
        shooter = new Joint("shooterEnt", 20, 0.95, 0.01, 0.2, 0, 0, -0.3, 0.3, false, null, 0, false);

        feederMotor = new CANSparkMax(feederID, CANSparkMax.MotorType.kBrushless);
        shooterMotor = new CANSparkMax(shooterID, CANSparkMax.MotorType.kBrushless);
        shooterMotor.setIdleMode(IdleMode.kBrake);
        shooterEncoder = shooterMotor.getEncoder();
        shooterPIDController = shooterMotor.getPIDController();
        
        proximitySensor = new DigitalInput(0);

        SmartDashboard.putNumber("Shooter Speed", 1);
        SmartDashboard.putNumber("Intake Feeder Speed", 0.8);

        this.lightingSubystem = ls;
        this.visionSubsystem = vs;

        this.controlInput = controlInput;
    }

    @Override
    public void periodic() {
        intakeFeederSpeed = SmartDashboard.getNumber("Intake Feeder Speed", 0.8);
        shooterSpeed = SmartDashboard.getNumber("Shooter Speed", 1);
        SmartDashboard.putBoolean("Has Ring", hasRing());
        SmartDashboard.putNumber("Shooter RPM",shooterEncoder.getVelocity());
        arm.periodic();
        shooter.periodic();
        lightingSubystem.setPulsing(hasRing());
    }

    public boolean hasRing() {
        return !proximitySensor.get();
    }

    // these methods are for legacy setting the intake speed, do not use these if
    // possible
    // public Command setIntakeState(boolean enabled) {
    // return Commands.runOnce(() -> feederMotor.set(enabled ? feederSpeed : 0));
    // }

    // public Command setShooterState(boolean enabled) {
    // return Commands.runOnce(() -> feederMotor.set(enabled ? shooterSpeed : 0));
    // }

    public Command runFeeder() {
        return Commands.runEnd(
                () -> feederMotor.set(intakeFeederSpeed),
                () -> feederMotor.set(0));
    }

    public Command reverseFeeder() {
        return Commands.runEnd(
                () -> feederMotor.set(-intakeFeederSpeed),
                () -> feederMotor.set(0));
    }

    public Command runShooter() {
        return Commands.runEnd(
                () -> shooterMotor.set(shooterSpeed),
                () -> shooterMotor.set(0));

    }

    // need to tune timings
    public Command runAmpShooterRoutine() {
        return new ParallelCommandGroup(
                runShooter().withTimeout(2),
                runFeeder().withTimeout(2));
    }

    /*
    for auto -
        close shooting (speaker): 3 seconds
        middle shooting (during auto): 4 seconds
        far shooting (during auto): 6 seconds
    */

    /*
    for teleop (probably but i want to have speaker and safe same time) -
        speaker: 3 seconds
        safe: 5 seconds
     */
    public Command runShooterRoutine(double runTime) {
        return new ParallelCommandGroup(
                runShooter().withTimeout(runTime),
                new SequentialCommandGroup(
                        new WaitCommand(runTime - 1),
                        runFeeder().withTimeout(0.5)));
    }

    public void setArmPosition(double pos) {
        arm.setPosition(pos);
    }

    public void setShooterPosition(double pos) {
        shooter.setPosition(pos);
    }


    public void setShooterVision() {
        if(visionSubsystem.hasTargets()) {
            double x = visionSubsystem.getY();
            double output = (-0.00015152 * Math.pow(x, 2)) + (0.0126955 * x) + 0.904188;
            setShooterPosition(output);
        }
    }

    public boolean isSafeMode() {
        return controlInput.isSafeMode();
    }
}

