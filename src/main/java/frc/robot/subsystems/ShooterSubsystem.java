package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.module.Joint;

public class ShooterSubsystem extends SubsystemBase {

    private final CANSparkMax feederMotor;
    // used for output rollers
    private final CANSparkMax shooterMotor;

    private Joint arm;
    private Joint shooter;

    private double feederSpeed;
    private double shooterSpeed;


    public ShooterSubsystem(int feederID, int shooterID) {
        arm = new Joint("shooterArm", 11, 0.7, 0, 0, 0, -0.30, 0.30, true);
        shooter = new Joint("shooterEnt", 20, 0.95, 0.0001, 0, 0.001, -0.27, 0.27, false);

        feederMotor = new CANSparkMax(feederID, CANSparkMax.MotorType.kBrushless);
        shooterMotor = new CANSparkMax(shooterID, CANSparkMax.MotorType.kBrushless);

        SmartDashboard.putNumber("Shooter Speed", 0.75);
        SmartDashboard.putNumber("Feeder Speed", 1);
    }

    @Override
    public void periodic() {
        feederSpeed = SmartDashboard.getNumber("Feeder Speed", 0.75);
        shooterSpeed = SmartDashboard.getNumber("Shooter Speed", 1);
        arm.periodic();
        shooter.periodic();
    }


    //these methods are for legacy setting the intake speed, do not use these if possible
    // public Command setIntakeState(boolean enabled) {
    //     return Commands.runOnce(() -> feederMotor.set(enabled ? feederSpeed : 0));
    // }

    //   public Command setShooterState(boolean enabled) {
    //     return Commands.runOnce(() -> feederMotor.set(enabled ? shooterSpeed : 0));
    // }

    public Command runFeeder() {
        return Commands.runEnd(
                () -> feederMotor.set(feederSpeed),
                () -> feederMotor.set(0)
            );
    }

    public Command runShooter() {
        return Commands.runEnd(
                () -> shooterMotor.set(shooterSpeed),
                () -> shooterMotor.set(0)
            );

    }

    public Command runShooterRoutine() {
        return new ParallelCommandGroup(
            runShooter().withTimeout(5),
            new SequentialCommandGroup(
                new WaitCommand(2.0),
                runFeeder().withTimeout(2)
            )
        );
    }

    public void setArmPosition(double pos) {
        arm.setPosition(pos);
    }

    public void setShooterPosition(double pos) {
        shooter.setPosition(pos);
    }
}
