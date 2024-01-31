package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class ShooterSubsystem { 
    private final CANSparkMax armMotor;
    private final CANSparkMax intakeMotor; 
    private final CANSparkMax shooterMotor;

    private final SparkPIDController armPID;
    private final SparkPIDController intakePID;
    private final SparkPIDController shooterSpeedPID;

    private final RelativeEncoder armEncoder;
    private final RelativeEncoder intakeEncoder;

    private final String m_armName = "Arm";

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

    public void moveArmManual(boolean toggle, boolean reverse, double speed) {
        if (!SmartDashboard.getBoolean(m_armName + "Manual Control", true)) {
            return;
        }
        armMotor.set(toggle ? (reverse ? speed : -speed) : 0);
    }

    // for manual control
    // public Command moveArm(boolean reverse) {
    //     double speed = SmartDashboard.getNumber("Intake Speed", 0);
        
    //     return Commands.startEnd(
    //         () -> armMotor.moveArmManual(true, reverse, speed),
    //         () -> armModule.MoveManual(false, reverse, speed)
    //     );
    // }
}

