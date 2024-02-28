package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

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

    private ControlInput controlInput;

    public ShooterSubsystem(int feederID, int shooterID, ControlInput controlInput) {
        arm = new Joint("shooterArm", 11, 0.7, 0, 0, 0, -0.30, 0.30, true);
        shooter = new Joint("shooterEnt", 20, 0.95, 0.0001, 0, 0.001, -0.27, 0.27, false);

        feederMotor = new CANSparkMax(feederID, CANSparkMax.MotorType.kBrushless);
        shooterMotor = new CANSparkMax(shooterID, CANSparkMax.MotorType.kBrushless);

        proximitySensor = new DigitalInput(0);

        SmartDashboard.putNumber("Shooter Speed", 1);
        SmartDashboard.putNumber("Intake Feeder Speed", 0.5);

        this.controlInput = controlInput;
    }

    @Override
    public void periodic() {
        intakeFeederSpeed = SmartDashboard.getNumber("Intake Feeder Speed", 0.5);
        shooterSpeed = SmartDashboard.getNumber("Shooter Speed", 1);
        arm.periodic();
        shooter.periodic();
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
                new SequentialCommandGroup(
                        new WaitCommand(2.0),
                        runFeeder().withTimeout(2)));
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
                        new WaitCommand(2.0),
                        runFeeder().withTimeout(2)));
    }

    public void setArmPosition(double pos) {
        arm.setPosition(pos);
    }

    public void setShooterPosition(double pos) {
        shooter.setPosition(pos);
    }

    public boolean isSafeMode() {
        return controlInput.isSafeMode();
    }
}
