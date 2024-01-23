package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

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

        armPID = armMotor.getPIDController();
        intakePID = intakeMotor.getPIDController();
        shooterSpeedPID = shooterMotor.getPIDController();

        armEncoder = armMotor.getEncoder();
        intakeEncoder = intakeMotor.getEncoder();
    }


    

}
