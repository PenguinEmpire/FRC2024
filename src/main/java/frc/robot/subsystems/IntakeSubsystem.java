package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

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
        intake = new Joint("intake", moveSparkID, 0.75, 0, 0, 0.013, -0.17, 0.17, true);
        intakeSpeed = SmartDashboard.getNumber("Intake Speed", 0);
    }

    @Override
    public void periodic() {
        intake.periodic();
    }

    public void setPosition(double pos) {
        intake.setPosition(pos);
    }

    public Command runRollers() {
        return Commands.runEnd(
            () -> rollerMotor.set(intakeSpeed),
            () -> rollerMotor.set(0)
        );
    }

}