package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.module.Joint;

public class IntakeSubsystem extends SubsystemBase{

    private final CANSparkMax rollerMotor;

    private Joint intake; 

    public IntakeSubsystem(int moveSparkID, int rollerSparkID) {
        rollerMotor = new CANSparkMax(rollerSparkID, CANSparkMax.MotorType.kBrushless);
        intake = new Joint("intake", moveSparkID, 0.75, 0, 0, 0.013, -0.17, 0.17, true);
    }

    @Override
    public void periodic () {
      intake.periodic();
    }

    public void setPosition(double pos) {
        intake.setPosition(pos);
   }

    // for manual control, create a sequential command group that first runs the intake rollers for a
    // certain amount of time, and then run the output rollers for a certain amount of time, and then
    // bind that method to one button

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

    public Command rollerContinuous() {
        double speed = 0.5;
        return Commands.runEnd(
            () -> {
                rollerMotor.set(speed);
            },
            () -> {
                rollerMotor.set(0);
            }
        );
    }

}