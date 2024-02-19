package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.module.Joint;

public class IntakeSubsystem extends SubsystemBase{

    private final CANSparkMax rollerMotor;

    private double kP = 0.0;
    private double kI = 0.0;
    private double kD = 0.0;
    private double kIz = 0.0;
    private double kFF = 0.0;
    private double kMaxOutput = 0.0;
    private double kMinOutput = 0.0;

    private double m_targetPosition;
    private double m_currentPosition;
    private Joint intake; 

    public IntakeSubsystem(int moveSparkID, int rollerSparkID) {
        rollerMotor = new CANSparkMax(rollerSparkID, CANSparkMax.MotorType.kBrushless);
        intake = new Joint("intake", moveSparkID, 0.1, 0, 0, 0, -0.3, 0.3);
    }

    @Override
    public void periodic () {
      intake.periodic();
    }

    public Command runRollers() {
        double speed = SmartDashboard.getNumber("Intake Speed", 0);
        return Commands.runEnd (
            () -> {
                rollerMotor.set(speed);
                },
            () -> {
                rollerMotor.set(0);
                }
        );
    }

}
