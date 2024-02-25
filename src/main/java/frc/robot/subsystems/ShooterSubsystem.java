package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.module.Joint;

//remove suppressor once done implementing
@SuppressWarnings("unused")
public class ShooterSubsystem extends SubsystemBase {

    private final CANSparkMax intakeMotor;
    // used for output rollers
    private final CANSparkMax outputMotor;

    private Joint arm;
    private Joint shooter;

    private double feederSpeed;
    private double shooterSpeed;

    private final DigitalInput infraredSensor;

    public ShooterSubsystem(int intakeSparkID, int ouputSparkID, int infraredSensorID) {
        arm = new Joint("shooterArm", 11, 0.7, 0, 0, 0, -0.30, 0.30, true);
        shooter = new Joint("shooterEnt", 20, 0.95, 0.0001, 0, 0.001, -0.27, 0.27, false);

        intakeMotor = new CANSparkMax(intakeSparkID, CANSparkMax.MotorType.kBrushless);
        outputMotor = new CANSparkMax(ouputSparkID, CANSparkMax.MotorType.kBrushless);
        infraredSensor = new DigitalInput(infraredSensorID);

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

    // for manual control, create a sequential command group that first runs the
    // intake rollers for a
    // certain amount of time, and then run the output rollers for a certain amount
    // of time, and then
    // bind that method to one button (runEnd should be used)

    // public Command stopIntakeRollers() {
    //     double speed = SmartDashboard.getNumber("Feeder Speed", 0);
    //     return Commands.runEnd(
    //             () -> {
    //                 // need to tune and change the value
    //                 if (infraredSensor.get()) {
    //                     intakeMotor.set(0);
    //                 } else {
    //                     intakeMotor.set(speed);
    //                 }
    //             },
    //             () -> intakeMotor.set(0));
    // }

    public Command runIntakeRollers() {
        return Commands.startEnd(
                () -> {
                    intakeMotor.set(feederSpeed);
                },
                () -> {
                    intakeMotor.set(0);
                });

    }

    public Command runShooterRollers() {
        return Commands.startEnd(
                () -> {
                    outputMotor.set(shooterSpeed);
                },
                () -> {
                    outputMotor.set(0);
                });

    }

    public Command runBothRollers() {
        return new ParallelCommandGroup(
            runShooterRollers().withTimeout(5),
            new SequentialCommandGroup(
                new WaitCommand(2.0),
                runIntakeRollers().withTimeout(2)
            )
        );
    }

    public void setArmPosition(double pos) {
        arm.setPosition(pos);
    }

    public void setShooterPosition(double pos) {
        shooter.setPosition(pos);
    }

    // auto
    public Command rollersContinuous() {
        double speed = 0.5;
        return Commands.runEnd(
                () -> {
                    intakeMotor.set(speed);
                    outputMotor.set(speed);
                },
                () -> {
                    intakeMotor.set(0);
                    outputMotor.set(0);
                });

    }

}
