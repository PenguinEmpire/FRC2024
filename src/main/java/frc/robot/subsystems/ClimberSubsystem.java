package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private CANSparkMax climberMotor;
    private RelativeEncoder climberEncoder;
    private SparkPIDController climberPID;

    private double armP = 1;
    private double armI = 0.0;
    private double armD = 0.0;

    // if motor is CIM motor, set motor type to brushed
    // if motor is neo motor, set motor type to brushless

    // up: 7.95 
    // down: -15.16

    public ClimberSubsystem(int sparkID) {
        climberMotor = new CANSparkMax(sparkID, CANSparkMax.MotorType.kBrushless);
        climberMotor.setInverted(true);
        climberEncoder = climberMotor.getEncoder();
        climberPID = climberMotor.getPIDController();

        climberPID.setP(armP);
        climberPID.setI(armI);
        climberPID.setD(armD);

        climberPID.setOutputRange(-0.8, 0.8);

        SmartDashboard.putNumber("Climber P", 1);
        SmartDashboard.putNumber("Climber I", 0.0);
        SmartDashboard.putNumber("Climber D", 0.0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Climber Encoder Pos", climberEncoder.getPosition());

        double pValue = SmartDashboard.getNumber("Climber P", 1);
        if (pValue != armP) {
            climberPID.setP(pValue);
        }

        double iValue = SmartDashboard.getNumber("Climber I", 0.0);
        if (iValue != armI) {
            climberPID.setI(iValue);
        }

        double dValue = SmartDashboard.getNumber("Climber D", 0.0);
        if (dValue != armD) {
            climberPID.setD(dValue);
        }
    }

    public Command runClimberMotorDown() {
        return Commands.runEnd(
                () -> {
                    climberMotor.set(-0.8);
                },
                () -> climberMotor.set(0));
    }

    public Command runClimberMotorUp() {
        return Commands.runEnd(
                () -> climberMotor.set(0.1),
                () -> climberMotor.set(0));
    }

    public void setClimberPosition(double position) {
        climberPID.setReference(position, ControlType.kPosition);
        SmartDashboard.putNumber("Climber Reference", position);
    }

}
