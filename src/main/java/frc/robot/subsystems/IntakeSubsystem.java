package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeSubsystem {

    private final CANSparkMax moveMotor;
    private final CANSparkMax rollerMotor;
    private final SparkPIDController m_pidController;
    private final RelativeEncoder m_encoder;

    private final double kP = 0.0;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kIz = 0.0;
    private final double kFF = 0.0;
    private final double kMaxOutput = 0.0;
    private final double kMinOutput = 0.0;

    private final String m_name = "I";

    public IntakeSubsystem(int moveSparkID, int rollerSparkID ) {
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

            SmartDashboard.putNumber("IntakePos", m_encoder.getPosition());
            SmartDashboard.putNumber(m_name + "P Gain", 0);
            SmartDashboard.putNumber(m_name + "I Gain", 0);
            SmartDashboard.putNumber(m_name + "D Gain", 0);
            SmartDashboard.putNumber(m_name + "I Zone", 0);
            SmartDashboard.putNumber(m_name + "Feed Forward", 0);
            SmartDashboard.putNumber(m_name + "Max Output", 0);
            SmartDashboard.putNumber(m_name + "Min Output", 0);
            SmartDashboard.putBoolean(m_name + "Manual Control", false);
        }

    public void moveIntakeManual(boolean toggle, boolean reverse, double speed) {
        if (!SmartDashboard.getBoolean(m_name + "Manual Control", true)) {
            return;
        }
        moveMotor.set(toggle ? (reverse ? speed : -speed) : 0);
    }

    public void runRollers() {

    }

    public void setPosition() {

    }

    public boolean hasReached() {
        return false;
    }

    public double getPosition() {
        return m_encoder.getPosition();
    }

    public Command rollerContinuous() {
        return null;
    }

}
