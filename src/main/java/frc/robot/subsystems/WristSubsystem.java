package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class WristSubsystem { 
    private final CANSparkMax armMotor;
    private final CANSparkMax intakeMotor; 
    private final CANSparkMax shooterMotor;

    private final SparkPIDController armPID;
    private final SparkPIDController intakePID;

    private final RelativeEncoder armEncoder;
    private final RelativeEncoder intakeEncoder;

    public WristSubsystem(int armSparkID, int intakeSparkID, int shooterSparkID) {
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        intakeMotor = new CANSparkMax(intakeSparkID, CANSparkMax.MotorType.kBrushless);
        shooterMotor = new CANSparkMax(shooterSparkID, CANSparkMax.MotorType.kBrushless);

        
        
    }

}
