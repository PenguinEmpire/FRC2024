package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.module.Joint;


//remove suppressor once done implementing
@SuppressWarnings("unused")
public class ShooterSubsystem extends SubsystemBase{


    private Joint joint;
    public ShooterSubsystem() {
        // joint = new ShooterJoint("arm", 11, 0);
    }

    @Override
    public void periodic(){
        joint.periodic();
    }

    // public Command runShooterRollers(boolean reverse) {
    //     double speed = SmartDashboard.getNumber("shooterMotor", 0);
    //     return Commands.runEnd (
    //         (reverse) -> {
    //             intakeMotor.set(-speed);
    //         },
    //         () -> {
    //             intakeMotor.set(speed);
    //         }
    //     );

    // }

    // public void moveArm(boolean toggle, boolean reverse) {
    //     double speed = SmartDashboard.getNumber("armMotor", 0);
    //     if (toggle) {
    //         armMotor.set(reverse ? speed : -speed);
    //     } else {
    //         armMotor.set(0);
    //     }

    // }

}
