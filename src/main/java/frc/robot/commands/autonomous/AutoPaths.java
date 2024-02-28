package frc.robot.commands.autonomous;

import java.nio.file.Path;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
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
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("BspeakerToLeft")),
                        autoMotions.intakeAutoMotion()),
                autoMotions.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("BleftToMiddle")),
                        autoMotions.intakeAutoMotion()),
                autoMotions.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("BmiddleToRight")),
                        autoMotions.intakeAutoMotion()),
                autoMotions.shootingMiddleAutoMotion()

        );
    }

    public static Command blueAmpFourPiece(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem,
            AutoMotions autoMotions) {
        return new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.OUT_OF_AUTO_POSITION),
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("BwallToSpeaker")),
                autoMotions.shootingClosestAutoMotion(),
                new ParallelCommandGroup(
                        autoMotions.intakeAutoMotion(),
                        AutoBuilder.followPath(PathPlannerPath.fromPathFile("BrightSpeakertoRight"))),
                autoMotions.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                    AutoBuilder.followPath(PathPlannerPath.fromPathFile("BrightToBackRight")),
                    new SequentialCommandGroup(
                        new WaitCommand (0.5),
                        autoMotions.intakeAutoMotion()
                    )   
                ),
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("BbackRightToShoot")),
                autoMotions.shootingFarAutoMotion(),
                new ParallelCommandGroup(
                    AutoBuilder.followPath(PathPlannerPath.fromPathFile("BshootToBackSecond")),
                    new SequentialCommandGroup(
                        new WaitCommand (1.5),
                        autoMotions.intakeAutoMotion()
                    )
                )
        );
    }

    public static Command redCenterFourPiece (IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem,
            AutoMotions autoMotions) {
                return new SequentialCommandGroup (
                    new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.OUT_OF_AUTO_POSITION)
                );
            }
}
