package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase{

    private final CANSparkMax moveMotor;
    private final CANSparkMax rollerMotor;
    private final SparkPIDController m_pidController;
    private final RelativeEncoder m_encoder;

    private double kP = 0.0;
    private double kI = 0.0;
    private double kD = 0.0;
    private double kIz = 0.0;
    private double kFF = 0.0;
    private double kMaxOutput = 0.0;
    private double kMinOutput = 0.0;

    private double m_targetPosition;
    private double m_currentPosition;

    private final String m_name = "I";

    public IntakeSubsystem(int moveSparkID, int rollerSparkID) {
        moveMotor = new CANSparkMax(moveSparkID, CANSparkMax.MotorType.kBrushless);
        rollerMotor = new CANSparkMax(rollerSparkID, CANSparkMax.MotorType.kBrushless);
        m_pidController = moveMotor.getPIDController();
        m_encoder = moveMotor.getEncoder();

        m_pidController.setP(kP);
        m_pidController.setI(kI);
        m_pidController.setD(kD);
        m_pidController.setIZone(kIz);
        m_pidController.setFF(kFF);
        m_pidController.setOutputRange(kMinOutput, kMaxOutput);
        moveMotor.setInverted(false);

        SmartDashboard.putNumber(m_name + "P Gain", kP);
        SmartDashboard.putNumber(m_name + "I Gain", kI);
        SmartDashboard.putNumber(m_name + "D Gain", kD);
        SmartDashboard.putNumber(m_name + "I Zone", kIz);
        SmartDashboard.putNumber(m_name + "Feed Forward", kFF);
        SmartDashboard.putNumber(m_name + "Max Output", kMaxOutput);
        SmartDashboard.putNumber(m_name + "Min Output", kMinOutput);

        SmartDashboard.putBoolean(m_name + "Manual Control", false);
        SmartDashboard.putNumber(m_name + "pos", m_encoder.getPosition());

        SmartDashboard.putNumber("Intake Speed", 0);
    }

    @Override
    public void periodic () {
        double p = SmartDashboard.getNumber(m_name + "P Gain", 0);
        double i = SmartDashboard.getNumber(m_name + "I Gain", 0);
        double d = SmartDashboard.getNumber(m_name + "D Gain", 0);
        double iz = SmartDashboard.getNumber(m_name + "I Zone", 0);
        double ff = SmartDashboard.getNumber(m_name + "Feed Forward", 0);
        double max = SmartDashboard.getNumber(m_name + "Max Output", 0);
        double min = SmartDashboard.getNumber(m_name + "Min Output", 0);
        double intakePos = SmartDashboard.getNumber(m_name + "pos", 0);

        if(p != kP) { 
            m_pidController.setP(p); 
            kP = p; 
        }
        if(i != kI) { 
            m_pidController.setI(i); 
            kI = i; 
        }
        if(d != kD) { 
            m_pidController.setD(d); 
            kD = d; 
        }
        if(iz != kIz) { 
            m_pidController.setIZone(iz); 
            kIz = iz; 
        }
        if(ff != kFF) { 
            m_pidController.setFF(ff); 
            kFF = ff; 
        }
        if((max != kMaxOutput) || (min != kMinOutput)) { 
            m_pidController.setOutputRange(min, max); 
            kMinOutput = min; 
            kMaxOutput = max; 
        }

        m_targetPosition = intakePos;
        m_currentPosition = getPosition();
        setPosition(m_targetPosition);

    }

    public void moveIntakeManual(boolean toggle, boolean reverse, double speed) {
        if (!SmartDashboard.getBoolean(m_name + "Manual Control", true)) {
            return;
        }
        moveMotor.set(toggle ? (reverse ? speed : -speed) : 0);
    }

    public Command runRollers() {
        double speed = SmartDashboard.getNumber("Intake Speed", 0);
        return Commands.startEnd (
            () -> {
                rollerMotor.set(speed);
                },
            () -> {
                rollerMotor.set(0);
                }
        );
    }

    public void setPosition(double position) {
        double intakePos = SmartDashboard.getNumber(m_name + "pos", 0);
        if (SmartDashboard.getBoolean(m_name + "Manual Control", true))
            return;
        m_pidController.setReference(intakePos, CANSparkMax.ControlType.kPosition);
    }

    public boolean hasReached(double tolerance) {
        return Math.abs(m_targetPosition - m_currentPosition) <= tolerance;
    }

    public double getPosition() {
        return m_encoder.getPosition();
    }

    // use for auto
    public Command rollerContinuous() {
        return null;
    }

}
