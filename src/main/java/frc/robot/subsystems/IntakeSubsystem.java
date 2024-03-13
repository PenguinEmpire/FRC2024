package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.module.Joint;

public class IntakeSubsystem extends SubsystemBase {

    private final CANSparkMax rollerMotor;

    private Joint intake;
    private double intakeSpeed;

    public IntakeSubsystem(int moveSparkID, int rollerSparkID) {
        rollerMotor = new CANSparkMax(rollerSparkID, CANSparkMax.MotorType.kBrushless);
        // could change back to:
        // intake = new Joint("intake", moveSparkID, 0.6, 0.001,0.2, 0, 0, -0.25, 0.25,
        // true, null, 0, false);

        intake = new Joint("intake", moveSparkID, 0.2, 0, 0, 0, 0, -0.35, 0.35, true,
                new ArmFeedforward(0.1, 0.4, 0), 4.84, true);
        SmartDashboard.putNumber("Intake Speed", -0.75);
        
    }

    @Override
    public void periodic() {
        intakeSpeed = SmartDashboard.getNumber("Intake Speed", -0.75);
        intake.periodic();
    }

    public void setPosition(double pos) {
        intake.setPosition(pos);
    }

    public Command runRollers() {
        return Commands.runEnd(
                () -> rollerMotor.set(intakeSpeed),
                () -> rollerMotor.set(0));
    }

    public Command endRollers() {
        return Commands.runOnce(
                () -> rollerMotor.set(0.0));
    }

}