package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private CANSparkMax climberMotor;

    // if motor is CIM motor, set motor type to brushed
    // if motor is neo motor, set motor type to brushless

    public ClimberSubsystem (int sparkID) {
        climberMotor = new CANSparkMax(sparkID, MotorType.kBrushed);
    }

    public Command runClimberMotor() {
        return Commands.runEnd(
            () -> climberMotor.set(0.5),
            () -> climberMotor.set(0)
        );
    }

}
