package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.module.ShooterJoint;

//remove suppressor once done implementing
@SuppressWarnings("unused")
public class ShooterSubsystem extends SubsystemBase {
    // used for intake rollers
    private final CANSparkMax intakeMotor;
    // used for output rollers
    private final CANSparkMax outputMotor;
    // used for turning the shooter
    private final CANSparkMax shooterEntMotor;

    private final SparkPIDController intakePID;
    private final SparkPIDController outputSpeedPID;
    private final SparkPIDController shooterEntPID;

    private final RelativeEncoder shooterEntEnocder;

    private final DigitalInput infraredSensor;

    private String sName = "Shooter";

    private ShooterJoint joint;

    public ShooterSubsystem(int intakeSparkID, int ouputSparkID, int shooterEntID, int infraredSensorID) {
        intakeMotor = new CANSparkMax(intakeSparkID, CANSparkMax.MotorType.kBrushless);
        outputMotor = new CANSparkMax(ouputSparkID, CANSparkMax.MotorType.kBrushless);
        shooterEntMotor = new CANSparkMax(shooterEntID, CANSparkMax.MotorType.kBrushless);
        infraredSensor = new DigitalInput(0);
        // PIDs for all motors
        // used for making not jerky
        intakePID = intakeMotor.getPIDController();
        shooterEntPID = shooterEntMotor.getPIDController();

        // used for changing velocity
        outputSpeedPID = outputMotor.getPIDController();

        // used for setting the position of the intake/shooter
        shooterEntEnocder = shooterEntMotor.getEncoder();

        joint = new ShooterJoint ("joint", 11, 0);
    }

    @Override
    public void periodic() {
        joint.periodic();
    }
    public Command stopIntakeRollers(){
     double speed = SmartDashboard.getNumber("Intake Speed", 0);
     return Commands.runEnd(
            () -> {
            // need to tune and change the value     
                if (infraredSensor.get()) {
                    intakeMotor.set(0);
                } else {
                    intakeMotor.set(speed);
                }
            },
            () -> intakeMotor.set(0)
    );}

    
    public Command runIntakeRollers() {
        double speed = SmartDashboard.getNumber("Intake Speed", 0);
        return Commands.runEnd(
                () -> {
                    intakeMotor.set(speed);
                },
                () -> {
                    intakeMotor.set(0);
                });
        
    }

    public Command runShooterRollers(boolean reverse) {
        double speed = SmartDashboard.getNumber("shooterMotor", 0);
        return Commands.runEnd(
                () -> {
                    intakeMotor.set(-speed);
                },
                () -> {
                    intakeMotor.set(speed);
                });

    }

    public void setShooterPosition(double position){
        SmartDashboard.putNumber(sName + "pos", position);
        if (SmartDashboard.getBoolean(sName + "Manual Control", true))
            return;
        shooterEntPID.setReference(position, ControlType.kPosition);
    }

    // auto
    public Command rollersContinuous(){
        double speed = 0.5;
        return Commands.runEnd(
            () -> {
                intakeMotor.set(speed);
                outputMotor.set(speed);
            },
            () -> {
                intakeMotor.set(0);
                outputMotor.set(0);
            }
        );

    }

    

}
