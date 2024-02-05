package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//remove suppressor once done implementing
@SuppressWarnings("unused")
public class ShooterSubsystem  { 
    private final CANSparkMax armMotor;
    private final CANSparkMax intakeMotor; 
    private final CANSparkMax shooterMotor;

    private final SparkPIDController armPID;
    private final SparkPIDController intakePID;
    private final SparkPIDController shooterSpeedPID;

    private final RelativeEncoder armEncoder;
    private final RelativeEncoder intakeEncoder;

    private double kP = 0.0;
    private double kI = 0.0;
    private double kD = 0.0;
    private double kIz = 0.0;
    private double kFF = 0.0;
    private double kMaxOutput = 0.0;
    private double kMinOutput = 0.0;


    public ShooterSubsystem(int armSparkID, int intakeSparkID, int shooterSparkID) {
        armMotor = new CANSparkMax(armSparkID, CANSparkMax.MotorType.kBrushless);
        intakeMotor = new CANSparkMax(intakeSparkID, CANSparkMax.MotorType.kBrushless);
        shooterMotor = new CANSparkMax(shooterSparkID, CANSparkMax.MotorType.kBrushless);
    
        SmartDashboard.putNumber("IntakeMotor",0);
        SmartDashboard.putNumber("shooterMotor",0);
        SmartDashboard.putNumber("armMotor",0);
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
    public void Intake(boolean toggle, boolean reverse) {
        double speed = SmartDashboard.getNumber("IntakeMotor",0);
        if(toggle) {
         intakeMotor.set(reverse ? speed : -speed);
            } else {
            intakeMotor.set(0);
            }
    // need to fix 
    //    armPID.set(kP);
    //    armPID.set(kI);
    //    armPID.set(kD);
    }
    public void Shooter( boolean toggle, boolean reverse) {
        double speed = SmartDashboard.getNumber("shooterMotor",0);
        if(toggle) {
         shooterMotor.set(reverse ? speed : - speed);
            } else {
            shooterMotor.set(0);
            }
     
    }
    public void arm(boolean toggle, boolean reverse) {
    double speed = SmartDashboard.getNumber("armMotor",0);
    if(toggle) {
        armMotor.set(reverse ? speed : -speed);
        } else {
        armMotor.set(0);
        }
     
    }
              
}

