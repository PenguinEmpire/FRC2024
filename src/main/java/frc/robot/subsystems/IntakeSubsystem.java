package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
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
        intake = new Joint("intake", moveSparkID, 0.8, 0, 0, 0.005, -0.2, 0.2, true);
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
            () -> rollerMotor.set(0)
        );
    }

}