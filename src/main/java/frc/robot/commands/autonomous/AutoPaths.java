package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoPaths {
    public static Command BlueRightBack(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem){
        return new SequentialCommandGroup(
            
        )
    }
}
