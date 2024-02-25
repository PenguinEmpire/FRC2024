package frc.robot.commands.autonomous;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoPaths {
    public static Command BlueRightBack(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem){
        return new SequentialCommandGroup(
            new SequentialCommandGroup (
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("Rstarting2firstRing")),
                new WaitCommand(1.0),
                intakeSubsystem.runRollersCommand().withTimeout(1.0),
                shooterSubsystem.runBothRollers().withTimeout(1.0)
            ),
            new ParallelCommandGroup (
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("rightBackRightRing"))
            )
        );
    }
}
