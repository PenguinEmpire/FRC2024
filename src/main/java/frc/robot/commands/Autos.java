package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.PositionCommand.Position;
import frc.robot.commands.autonomous.AutoMotions;
import frc.robot.commands.autonomous.MoveCommandOdometry;
import frc.robot.commands.autonomous.MoveCommandVision;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class Autos extends Command {
    private DriveSubsystem driveSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private AutoMotions autoMotion;
    private VisionSubsystem visionSubsystem;

    public Autos(DriveSubsystem ds, AutoMotions aM, ShooterSubsystem sS, IntakeSubsystem iS, VisionSubsystem vS) {
        this.driveSubsystem = ds;
        this.shooterSubsystem = sS;
        this.intakeSubsystem = iS;
        this.autoMotion = aM;
        this.visionSubsystem = vS;
    }

    public Command test() {
        return Commands.race(
            autoMotion.runIntake(),
            autoMotion.runShooter(),
            new SequentialCommandGroup(
                new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.OUT_OF_AUTO_POSITION),
                new ParallelCommandGroup(
                        autoMotion.intakeAutoMotion(),
                        new MoveCommandOdometry(driveSubsystem, new Pose2d(0.809, 1.107, new Rotation2d(0.5)), true,
                                0.75),
                        new PositionCommand(shooterSubsystem, intakeSubsystem, PositionCommand.Position.SAFE_OR_SPEAKER)),
                new MoveCommandVision(driveSubsystem, true, 0.75, visionSubsystem),
                autoMotion.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                    autoMotion.intakeAutoMotion(),
                    new MoveCommandOdometry(driveSubsystem, new Pose2d(0.866, 0.452, new Rotation2d(0.3)), true, 0.75)
                ),
                new MoveCommandVision(driveSubsystem, true, 0.75, visionSubsystem),
                autoMotion.shootingMiddleAutoMotion(),
                new ParallelCommandGroup(
                    autoMotion.intakeAutoMotion(),
                    new MoveCommandOdometry(driveSubsystem, new Pose2d(1.01, -0.725, new Rotation2d(-0.5)), true, 0.75)
                ),
                new MoveCommandVision(driveSubsystem, true, 0.75, visionSubsystem),
                autoMotion.shootingMiddleAutoMotion()

        )
        );
    }
}
