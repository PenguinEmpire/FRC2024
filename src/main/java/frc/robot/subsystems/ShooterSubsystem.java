package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

//remove suppressor once done implementing
@SuppressWarnings("unused")
public class ShooterSubsystem { 
    private final CANSparkMax armMotor;
    private final CANSparkMax intakeMotor; 
    private final CANSparkMax shooterMotor;

    private final SparkPIDController armPID;
    private final SparkPIDController intakePID;
    private final SparkPIDController shooterSpeedPID;

    private final RelativeEncoder armEncoder;
    private final RelativeEncoder intakeEncoder;

    public ShooterSubsystem(int armSparkID, int intakeSparkID, int shooterSparkID) {
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        intakeMotor = new CANSparkMax(intakeSparkID, CANSparkMax.MotorType.kBrushless);
        shooterMotor = new CANSparkMax(shooterSparkID, CANSparkMax.MotorType.kBrushless);

        // PIDs for all motors
        // used for making not jerky
        armPID = armMotor.getPIDController();
        intakePID = intakeMotor.getPIDController();
        // used for changing velocity 
        shooterSpeedPID = shooterMotor.getPIDController();

        // used for setting the positions of the arms 
        armEncoder = armMotor.getEncoder();

        // used for setting the position of the intake/shooter
        intakeEncoder = intakeMotor.getEncoder();

    }
}

