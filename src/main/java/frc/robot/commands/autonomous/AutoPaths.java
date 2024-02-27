package frc.robot.commands.autonomous;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.PositionCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoPaths {
    public static Command blueCenterFourPiece(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem,
            AutoMotions autoMotions) {
        return new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.OUT_OF_AUTO_POSITION),
                autoMotions.shootingClosestAutoMotion(),
                new ParallelCommandGroup(
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("speakerToLeft")),
                        autoMotions.intakeAutoMotion()),
                autoMotions.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("leftToMiddle")),
                        autoMotions.intakeAutoMotion()),
                autoMotions.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("middleToRight")),
                        autoMotions.intakeAutoMotion()),
                autoMotions.shootingMiddleAutoMotion()

        );
    }

    public static Command blueAmpFourPiece(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem,
            AutoMotions autoMotions) {
        return new SequentialCommandGroup(
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("wallToSpeaker")),
                autoMotions.shootingClosestAutoMotion(),
                new ParallelCommandGroup(
                        autoMotions.intakeAutoMotion(),
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("rightSpeakertoRight"))),
                autoMotions.shootingMiddleAutoMotion()

        );
    }
}
